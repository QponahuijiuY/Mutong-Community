package com.mutong.jcommunity.controller;

import com.mutong.jcommunity.service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-18 10:59
 * @time_complexity: O()
 */
@Controller
public class DateController {
    @Autowired
    private DateService dateService;

    //统计页面
    @RequestMapping(value = "/data",method = {RequestMethod.GET,RequestMethod.POST})
    public String getDataPage(){
        return "/site/admin/data";
    }
    //处理网站uv
    @RequestMapping(value = "/data/uv",method = RequestMethod.POST)
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
        long uv = dateService.calculateUV(start,end);
        model.addAttribute("uvResult",uv);
        model.addAttribute("uvStartDate",start);
        model.addAttribute("uvEndDate",end);
        return "forward:/data";
    }
    //统计活跃用户
    @RequestMapping(value = "/data/dau",method = RequestMethod.POST)
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
        long dau = dateService.calculateDAU(start,end);
        model.addAttribute("dauResult",dau);
        model.addAttribute("dauStartDate",start);
        model.addAttribute("dauEndDate",end);
        return "forward:/data";
    }

}
