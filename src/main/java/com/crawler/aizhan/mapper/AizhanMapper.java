package com.crawler.aizhan.mapper;

import com.crawler.aizhan.dto.SearchResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author Ximenchuiyun
 * @Date 2022/7/17 17:33
 * @Description
 */
@Mapper
@Repository
public interface AizhanMapper {
    /**
     * 查询所有数据
     * @return
     */
    int selectNumBySearchContent(@Param("searchContent") String searchContent);

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
