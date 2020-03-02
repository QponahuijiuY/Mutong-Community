package com.mutong.jcommunity.util;

import org.apache.maven.surefire.shade.org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-01 16:29
 * @time_complexity: O()
 */
public class CommunityUtil {
    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
