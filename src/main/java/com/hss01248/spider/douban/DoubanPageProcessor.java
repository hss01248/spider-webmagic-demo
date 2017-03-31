package com.hss01248.spider.douban;

import com.hss01248.spider.util.FileDownloader;
import org.apache.log4j.PropertyConfigurator;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by Administrator on 2017/3/31 0031.
 */
public class DoubanPageProcessor implements PageProcessor {



    private Site site = Site.me().setRetryTimes(3).setSleepTime(500)
            .setUserAgent("User-Agent:Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");

    public void process(Page page) {
        if(!page.getUrl().toString().contains("topic")){//列表页
            page.addTargetRequests(page.getHtml().xpath("//td[@class='title']/a/@href").all());

        }else {//详情页
            if(page.getHtml().xpath("//div[@class='topic-figure cc']").all().size()>0){
                List<String> urls = page.getHtml().xpath("//div[@class='topic-figure cc']/img/@src").all();
                page.putField("urls", urls);
                for(String url : urls){
                    FileDownloader.downloadFile(url,"G:\\douban");
                }
                page.putField("title", page.getHtml().xpath("//div[@id='content']/h1/text()").toString());
            }
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("E:\\ASprogects\\hss01248\\springMVCDemo\\config\\log4j.properties");

        /*String[] urls = new String[369];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = "https://www.douban.com/group/duqi/discussion?start="+25*i;
        }*/

        Spider.create(new DoubanPageProcessor())
                .addUrl("https://www.douban.com/group/duqi/")//urls
               // .addPipeline(new ConsolePipeline())
                .addPipeline(new FilePipeline("G:\\webmagic2\\"))
                .thread(5).run();
    }
}
