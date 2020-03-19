package com.mutong.jcommunity;

import com.mutong.jcommunity.elasticsearch.DiscussPostRepository;
import com.mutong.jcommunity.mapper.DiscussPostMapper;
import com.mutong.jcommunity.model.DiscussPost;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-15 22:49
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class ElasticSearchTests {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private DiscussPostRepository discussPostRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testInsert(){
        //从数据库里面查询数据,插入到ES里面
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(347));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(346));
    }

    @Test
    public void testInsertAll(){
        //一次插入所有的数据
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(102,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(111,0,100,0));
    }

    @Test
    public void testUpdate(){
        //修改查询到的数据,并且添加到ES里面
        DiscussPost post = discussPostMapper.selectDiscussPostById(180);
        post.setContent("我是景庚晨");
        discussPostRepository.save(post);
    }

    @Test
    public void testDelete(){
        //删除ES里面的数据
        discussPostRepository.deleteById(180);
    }
    @Test
    public void testDeleteAll(){
        //删除ES里面的全部数据
        discussPostRepository.deleteAll();
    }

    @Test
    public void testSearchByRepository(){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网","title","content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0,10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        Page<DiscussPost> page = discussPostRepository.search(searchQuery);
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getSize());
        for (DiscussPost post : page){
            System.out.println(post);
        }
    }

    @Test
    public void testSearchByTemplate(){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网","title","content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0,10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        Page<DiscussPost> page = elasticsearchTemplate.queryForPage(searchQuery, DiscussPost.class, new SearchResultMapper(){
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {

                SearchHits hits = response.getHits();
                if (hits.getTotalHits() <= 0){
                    return null;
                }
                List<DiscussPost> list = new ArrayList<>();
                for (SearchHit hit: hits){
                    DiscussPost post = new DiscussPost();

                    String id = hit.getSourceAsMap().get("id").toString();
                    post.setId(Integer.valueOf(id));

                    String userId = hit.getSourceAsMap().get("userId").toString();
                    post.setUserId(Integer.valueOf(userId));

                    String title = hit.getSourceAsMap().get("title").toString();
                    post.setTitle(title);

                    String content = hit.getSourceAsMap().get("content").toString();
                    post.setContent(content);

                    String status = hit.getSourceAsMap().get("status").toString();
                    post.setStatus(Integer.valueOf(status));

                    String createTime = hit.getSourceAsMap().get("createTime").toString();
                    post.setCreateTime(new Date(Long.valueOf(createTime)));

                    String commentCount = hit.getSourceAsMap().get("commentCount").toString();
                    post.setCommentCount(Integer.valueOf(commentCount));


                    //处理高亮显示
                    HighlightField titleField = hit.getHighlightFields().get("title");
                    if (titleField != null){
                        post.setTitle(titleField.getFragments()[0].toString());
                    }

                    HighlightField contentField = hit.getHighlightFields().get("content");
                    if (contentField != null){
                        post.setTitle(contentField.getFragments()[0].toString());
                    }
                    list.add(post);
                }
                return new AggregatedPageImpl(list, pageable,hits.getTotalHits(),response.getAggregations(), response.getScrollId(),hits.getMaxScore());
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }
        });


        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getSize());
        for (DiscussPost post : page){
            System.out.println(post);
        }
    }
}
