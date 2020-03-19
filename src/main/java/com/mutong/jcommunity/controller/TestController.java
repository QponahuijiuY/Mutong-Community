package com.mutong.jcommunity.controller;

import com.mutong.jcommunity.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-03 17:48
 * @time_complexity: O()
 */
@Controller
@RequestMapping("/test")
public class TestController {

    //ajax示例
    @RequestMapping(value = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name , int age){

        return CommunityUtil.getJSONString(0,"操作成功");
    }
}
