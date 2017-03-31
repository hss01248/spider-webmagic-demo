package com.hss01248.spider.douban;

import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

@TargetUrl("https://www.douban.com/group/topic/\\d/")
@HelpUrl("https://www.douban.com/group/duqi/discussion?start=\\d")
//@ExtractBy(value = "//div[@class='topic-content']",multi = true)
public class DoubanImageBean {

    @ExtractBy(value = "//div[@id='content']/h1/text()")
    private String title;
    @ExtractBy(value = "//span[@class='from']/a/text()")
    private String username;



    @ExtractBy(value ="//div[@class='topic-figure cc']/img/@src", notNull = true)
    private List<String> pics;//以"###"分隔
    @ExtractBy(value = "//div[@class='info']/div/a/text()")
    private String tag;





    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
