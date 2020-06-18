package com.mutong.jcommunity;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-13 21:46
 * @time_complexity: O()
 */
public class CommunityServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JcommunityApplication.class);
    }
}
