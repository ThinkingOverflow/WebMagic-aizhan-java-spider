package com.crawler.aizhan.service.pipeline;

import com.crawler.aizhan.dto.SearchResult;
import com.crawler.aizhan.mapper.AizhanMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;

/**
 * @Author Ximenchuiyun
 * @Date 2022/7/17 17:58
 * @Description
 */
@Component
public class AizhanPipeline implements Pipeline {
    @Autowired
    private AizhanMapper aizhanMapper;

    @Override
    public void process(ResultItems resultItems, Task task) {
        ArrayList<SearchResult> aizhanInfoList = resultItems.get("aizhanInfoList");
        if(CollectionUtils.isNotEmpty(aizhanInfoList)){
            aizhanInfoList.stream().forEach(SearchResult -> {
                aizhanMapper.addSearchData(SearchResult);
            });
        }
    }
}
