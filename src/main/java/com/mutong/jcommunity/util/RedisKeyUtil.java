package com.mutong.jcommunity.util;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-11 14:57
 * @time_complexity: O()
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";//点赞评论和帖子1代表
    private static final String PREFIX_USER_LIKE = "like:user";//某人获得的点赞
    private static final String PREFIX_FOLLOWEE = "followee";//被关注,我关注别人
    private static final String PREFIX_FOLLOWER = "follower";//粉丝,别人关注我

    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";
    /**
     * 某个实体的赞
     * @param entityType 1代表帖子,  2代表评论
     * @param entityId 帖子id,或者评论id
     * @return
     */
    public static String getEntityLikeKey(int entityType, int entityId){
        // 给帖子点赞like:entity: 1 : id(帖子id)
        // 给评论点赞like:entity: 2 : id(评论id)
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 某个用户的赞
     * @param userId
     * @return
     */
    public static String getUserLikeKey(int userId){
        // like:user : id(用户id)
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * 某个用户关注的实体,我关注别人,某个用于点赞了实体
     * @param userId 用户id,指明了谁在关注
     * @param entityType 1点赞帖子, 2点赞评论
     * @return
     */
    public static String getFolloweeKey(int userId, int entityType) {
        // followee: userid:1 点赞帖子,
        // followee: userid:2 点赞评论
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 某个实体拥有的粉丝
     * @param entityType 1,代表帖子 2 代表评论
     * @param entityId 粉丝的id
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId) {
        // follower : 1:
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 登录验证码
     * @param owner 指明这个kapcha是谁的
     * @return
     */
    public static String getKaptchaKey(String owner){
        //kapcha : ownwe
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    /**
     * 登录的凭证
     * @param ticket
     * @return
     */
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }


    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }
}

