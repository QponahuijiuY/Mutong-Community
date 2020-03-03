package com.mutong.jcommunity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-03 11:07
 * @time_complexity: O()
 */

/**
 * 注解声明在方法上
 */
@Target(ElementType.METHOD)
/**
 * 注解运行在程序运行时
 */
@Retention(RetentionPolicy.RUNTIME)

public @interface LoginRequired {
}
