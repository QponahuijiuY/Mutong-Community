package com.mutong.jcommunity.util;

import org.apache.maven.surefire.shade.org.apache.commons.lang3.CharUtils;
import org.apache.maven.surefire.shade.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-03 13:52
 * @time_complexity: O()
 */
@Component
@Repository
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符
    private static final String REPLACEMENT = "***";
    //根节点
    private TrieNode rootNode = new TrieNode();
    /**
     * 初始化构造器
     */
    @PostConstruct
    public void init(){

        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-works.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ) {
            String keyword;
            while ((keyword = reader.readLine()) != null ){
                //添加到前缀树
                this.addKeyword(keyword);
            }
        }catch (Exception e) {
            logger.error("加载敏感词操作异常" + e.getMessage());
        }
    }

    /**
     * 将一个敏感词添加到前缀树上去
     * @param keyword
     */
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0 ; i < keyword.length() ; i ++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null){
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            //指针指向子节点
            tempNode = subNode;
            //设置结束的标识
            if (i == keyword.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 带过滤的文本
     * @return 已经过滤后的文本
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }
        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        //过滤后的结果
        StringBuilder sb = new StringBuilder();
        while (position < text.length()){
            char c = text.charAt(position);
            //跳过特殊符号
            if (isSymbol(c)){
                //若指针1处于根节点,保留
                if (tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                //无论符号在开头还是在中间指针3都要向下走一步
                position++;
                continue;
            }
            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null){
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                //指针1指向根节点
                tempNode = rootNode;
            }else if (tempNode.isKeywordEnd()){
                //发现了敏感词
                sb.append(REPLACEMENT);
                //指针进入下一个位置
                begin = ++position;
                //指针1指向根节点
                tempNode = rootNode;
            }else {
                //检查下一个字符
                position++;
            }
        }
        //将最后一批字符进入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    /**
     * 判断是否是特殊字符
     * @param c 字符
     * @return trur or false
     */
    private boolean isSymbol(Character c){
        //0x2E80-0x9FFF 中文字符
        return CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF );
    }

    /**
     * 前缀树数据结构
     */
    private class TrieNode{
        //关键字结束标识
        private boolean isKeywordEnd = false;

        private Map<Character,TrieNode> subNodes = new HashMap<>();
        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
        //添加子节点
        public void addSubNode(Character c , TrieNode node){
            subNodes.put(c,node);
        }
        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
