package com.crawler.aizhan.service;

import com.crawler.aizhan.mapper.AizhanMapper;
import com.crawler.aizhan.service.pipeline.AizhanPipeline;
import com.crawler.aizhan.service.process.AizhanProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @Description
 */
@Service
@Slf4j
public class AizhanService {
    @Autowired
    private AizhanPipeline aizhanPipeline;

    @Autowired
    private AizhanProcessor aizhanProcessor;

    @Autowired
    private AizhanMapper aizhanMapper;

    private final String DOMAIN_NAME_PATTERN = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";

    /**
     * 抓取爱站网数据
     */
    public void getSearchData(){
        while (true){
            System.out.println();
            System.out.println("------------------------");
            System.out.print("请输入要查询的域名：");
            Scanner in = new Scanner(System.in);
            String searchContent = in.nextLine();

            if(!isURL(searchContent)){
                log.info("输入的域名无效");
                continue;
            }

            //判断当前日期是否已经查询过该域名，如果查询过，则无须继续查询（我们查询的时间粒度是“天”）
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String searchDate = simpleDateFormat.format(new Date());
            //排除域名前面有 www 的重复情况
            if(searchContent.contains("www")){
                searchContent = searchContent.substring(4 , searchContent.length());
            }
            int count = aizhanMapper.selectByDateAndContent(searchContent, searchDate);
            if(count!=0){
                log.info("当天已经查询过" + searchContent + "，请去数据库查询数据，无须重复抓取爱站网数据");
                continue;
            }

            //先查找第一页数据
            String url = "https://baidurank.aizhan.com/baidu/" + searchContent;
            Spider.create(aizhanProcessor)
                    .addUrl(url)
                    .addPipeline(aizhanPipeline)
                    .thread(1)
                    .run();
        }

    }

    /**
     * 验证输入的字符串是否是有效域名
     * @param str
     * @return
     */
    public boolean isURL(String str){
        Pattern pattern = Pattern.compile(DOMAIN_NAME_PATTERN);
        return pattern.matcher(str).find();
    }

}
