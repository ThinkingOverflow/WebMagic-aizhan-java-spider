package com.crawler.aizhan.controller;

import com.crawler.aizhan.service.AizhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
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
