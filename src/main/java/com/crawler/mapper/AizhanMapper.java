package com.crawler.mapper;

import com.crawler.dto.SearchResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author
 * @Date 2022/7/20 15:29
 * @Description
 */
@Mapper
@Repository
public interface AizhanMapper {
    /**
     * 添加数据
     * @param result
     * @return
     */
    int addSearchData(SearchResult result);

    /**
     * 该日期对应的该查询关键词是否存在
     * @param searchContent
     * @param searchDate
     * @return
     */
    int selectByDateAndContent(String searchContent , String searchDate);
}
