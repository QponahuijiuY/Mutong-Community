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

}
