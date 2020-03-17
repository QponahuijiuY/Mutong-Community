package com.mutong.jcommunity.util;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-01 16:30
 * @time_complexity: O()
 */
public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态登陆凭证的超时时间
     */
    int DEFAULT_EXPIRED_SECOND = 3600 * 12;

    /**
     * 记住我状态下的登陆凭证超时时间
     */
    int REMEMBER_EXPIRED_SECOND = 3600 * 12 * 100;

    /**
     * 实体类型:帖子
     */
    int ENTITY_TYPE_POST = 1;


    /**
     * 实体类型:评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型:用户
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * 主题:评论
     */
    String TOPIC_COMMENT = "comment";
    /**
     * 主题:关注
     */
    String TOPIC_FOLLOW = "follow";
    /**
     * 主题:点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 发帖
     */
    String TOPIC_PUBLISH = "publish";

    /**
     * 删除
     */
    String TOPIC_DELETE = "delete";

    /**
     * 系统用户id
     */
    int SYSTEM_USER_ID = 1;

    /**
     * 权限:普通用户
     */
    String AUTGORITY_USER = "user";

    /**
     * 权限:管理员
     */
    String AUTGORITY_ADMIN = "admin";

    /**
     * 权限:版主
     */
    String AUTGORITY_MODERATOR = "moderator";
}
