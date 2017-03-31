package com.hss01248.spider.douban;

import com.hss01248.spider.util.MyLog;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.function.Consumer;

/**
 * Created by Administrator on 2017/3/31 0031.
 */
public class DoubanPipline implements PageModelPipeline<DoubanImageBean> {
    public void process(DoubanImageBean doubanImageBean, Task task) {
        MyLog.e(doubanImageBean.getTitle());
        doubanImageBean.getPics().forEach(new Consumer<String>() {
            public void accept(String s) {
                MyLog.e(s);
            }
        });

    }
}
