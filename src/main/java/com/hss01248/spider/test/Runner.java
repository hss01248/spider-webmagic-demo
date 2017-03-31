package com.hss01248.spider.test;

import com.hss01248.spider.douban.DoubanImageBean;
import com.hss01248.spider.douban.DoubanPipline;
import org.apache.log4j.PropertyConfigurator;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

/**
 * Created by Administrator on 2017/3/31 0031.
 */
public class Runner {

    public static void main(String[] args) {
        PropertyConfigurator.configure("E:\\ASprogects\\hss01248\\springMVCDemo\\config\\log4j.properties");
        Site site = Site.me().setRetryTimes(3).setSleepTime(500)
                .setUserAgent("User-Agent:Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");
        OOSpider.create(site, new DoubanPipline(), DoubanImageBean.class)
                .addUrl("https://www.douban.com/group/duqi/").thread(5).run();

    }
}
