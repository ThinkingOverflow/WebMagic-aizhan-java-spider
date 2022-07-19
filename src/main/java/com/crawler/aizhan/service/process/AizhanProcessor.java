package com.crawler.aizhan.service.process;

import com.crawler.aizhan.dto.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 */
@Component
public class AizhanProcessor implements PageProcessor {
    private Site site = Site.me().setDomain("baidurank.aizhan.com")
            .setRetryTimes(3).setSleepTime(1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
            //查询输入userid的 cookie 用于登录（不登录无法拉取到数据）
            .addCookie("userId" , "xxxxxx");

    @Override
    public void process(Page page) {
        //获取封装信息对应的 tr
        List<Selectable> trList = page.getHtml().xpath("//div[@class='baidurank-list']/table/tbody/tr").nodes();
        //获取查询的域名
        String searchContent = page.getHtml().xpath("//div[@class='search-wrap']/form/input[@type='text']/@value").toString();
        //将“www"去除避免重复将数据插入数据库
        if(searchContent.contains("www")){
            searchContent = searchContent.substring(4 , searchContent.length());
        }
        ArrayList<SearchResult> aizhanInfoList = Lists.newArrayList();

        //换个简单点的遍历方法
        for (Selectable tr : trList) {
            List<Selectable> tdList = tr.xpath("//td").nodes();
            if(tdList.size()==2 && "未找到信息!".equals(tdList.get(1).xpath("/td/text()").toString())){
                //拉取的url对应的信息不存在不存在，直接结束程序
                return;
            }
            int size = tdList.size();
            Selectable keyCharSele = tdList.get(size-5).xpath("//a/text()");
            Selectable rankSele = tdList.get(size-4).xpath("//span/text()");
            Selectable pcSearchNumSele = tdList.get(size-3).xpath("//a/text()");
            Selectable includeNumSele = tdList.get(size-2).xpath("//a/text()");
            Selectable pageTitleSele = tdList.get(size-1).xpath("//a/text()");

            String keyChar = keyCharSele==null ? "" : keyCharSele.toString().trim();
            String rank = rankSele==null ? "" : rankSele.toString().trim();
            String pcSearchNum = pcSearchNumSele==null ? "" : pcSearchNumSele.toString().trim();
            String includeNum = includeNumSele==null ? "" : includeNumSele.toString().trim();
            String pageTitle = pageTitleSele==null ? "" : pageTitleSele.toString().trim();

            SearchResult result = new SearchResult();
            result.setKeyChar(keyChar);
            result.setRank(rank);
            result.setPcSearchNum(pcSearchNum);
            result.setIncludeNum(includeNum);
            result.setPageTitle(pageTitle);
            result.setSearchContent(searchContent);
            //设置日期格式化样式为：yyyy-MM-dd (mysql中date类型对应 yyyy-MM-dd 格式)
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatDate = simpleDateFormat.format(new Date());
            result.setSearchDate(formatDate);

            aizhanInfoList.add(result);
        }

        //原来的遍历方法
//        for (int i = 0; i < trList.size(); i++) {
//            Selectable selectable = trList.get(i);
//            //先判断拉取的网站数据是否存在
//            Selectable notFindInfo = selectable.xpath("/tr/td[2]/text()");
//            if(Objects.nonNull(notFindInfo) && "未找到信息!".contains(notFindInfo.toString())){
//                //拉取的url对应的信息不存在不存在，直接结束程序
//                return;
//            }
//
//            //这个坐标用于解决第一个 <tr> 内有6个 <td> 的情况（其他都只有5个 <td>）
//            int coordinate = 1;
//
//            //遍历第一个 <tr> 的时候，从第二个 <td> 开始
//            if(i==0){
//                coordinate = 2;
//            }
//            Selectable keyCharSele = selectable.xpath("/tr/td[" + coordinate + "]/a[@class='gray']/text()");
//            Selectable rankSele = selectable.xpath("/tr/td[" + (coordinate+1) + "]/span[@class='blue']/text()");
//            Selectable pcSearchNumSele = selectable.xpath("/tr/td[" + (coordinate+2) + "]/a[@rel='nofollow']/text()");
//            Selectable includeNumSele = selectable.xpath("/tr/td[" + (coordinate+3) + "]/a[@class='gray']/text()");
//            Selectable pageTitleSele = selectable.xpath("/tr/td[" + (coordinate+4) + "]/a[@name='baiduLink']/text()");
//
//            String keyChar = keyCharSele==null ? "" : keyCharSele.toString();
//            String rank = rankSele==null ? "" : rankSele.toString();
//            String pcSearchNum = pcSearchNumSele==null ? "" : pcSearchNumSele.toString();
//            String includeNum = includeNumSele==null ? "" : includeNumSele.toString();
//            String pageTitle = pageTitleSele==null ? "" : pageTitleSele.toString();
//
//            SearchResult result = new SearchResult();
//            result.setKeyChar(keyChar);
//            result.setRank(rank);
//            result.setPcSearchNum(pcSearchNum);
//            result.setIncludeNum(includeNum);
//            result.setPageTitle(pageTitle);
//            result.setSearchContent(searchContent);
//            //设置日期格式化样式为：yyyy-MM-dd (mysql中date类型对应 yyyy-MM-dd 格式)
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String formatDate = simpleDateFormat.format(new Date());
//            result.setSearchDate(formatDate);
//
//            aizhanInfoList.add(result);
//        }

        //每次加载完一页，先把数据添加到 field 中，这样 pipeline 就可以拿到数据（等 process 执行完就会跳转到 pipeline 中去执行插入数据）
        page.putField("aizhanInfoList" , aizhanInfoList);

        //下面代码的功能：将下一页的数据放入查询链接列表
        List<Selectable> aNodes = page.getHtml().xpath("//div[@class='baidurank-pager']/ul/a").nodes();
        int nextPage = -1;
        for (int i = 0; i < aNodes.size(); i++) {
            Selectable classVal = aNodes.get(i).$("a", "class");
            if(StringUtils.isNotEmpty(classVal.toString())){
                nextPage = i +1;
                break;
            }
        }
        //不是第一页也没有超过最后一页（第一页重新进来的时候已经查找）
        if(nextPage > 0 && nextPage < aNodes.size()){
            page.addTargetRequest(aNodes.get(nextPage).$("a" , "href").toString());
        }else{
            System.out.println("finish");
        }


    }

    @Override
    public Site getSite() {
        return site;
    }
}
