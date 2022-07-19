package com.crawler.aizhan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author Ximenchuiyun
 * @Date 2022/7/17 17:08
 * @Description
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SearchResult {
    /**
     * Id
     */
    private Integer id;

    /**
     * 关键字
     */

    private String keyChar;

    /**
     * 排名
     */
    private String rank;

    /**
     * (PC)搜索量
     */
    private String pcSearchNum;

    /**
     * 收录量
     */
    private String includeNum;

    /**
     * 网页标题
     */
    private String pageTitle;

    /**
     * 用户查询的内容（网址）
     */
    private String searchContent;

    /**
     * 查询日期
     */
    private String searchDate;
}
