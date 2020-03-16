package com.mutong.jcommunity.elasticsearch;

import com.mutong.jcommunity.model.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-15 22:47
 * @time_complexity: O()
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
}
