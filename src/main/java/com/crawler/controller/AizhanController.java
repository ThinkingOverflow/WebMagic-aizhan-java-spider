package com.crawler.controller;

import com.crawler.service.AizhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author
 * @Date 2022/7/20 15:27
 * @Description
 */

@RestController
public class AizhanController {
    @Autowired
    private AizhanService service;

    /**
     * 抓取爱站网数据
     */
    @RequestMapping("/aizhan")
    public void getSearchData(){
        service.getSearchData();
    }
}
