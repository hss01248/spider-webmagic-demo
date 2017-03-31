package com.hss01248.spider.util;

import com.hss01248.spider.threadpool.ThreadPoolFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/3/31 0031.
 */
public class FileDownloader {

    private static PoolingHttpClientConnectionManager poolConnManager;

   private static HttpClient httpclient;

    public static void init()
    {
        try {
            //需要通过以下代码声明对https连接支持
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy())
                    .build();
            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();
            //初始化连接管理器
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // Increase max total connection to 200
            poolConnManager.setMaxTotal(50);
            // Increase default max connection per route to 20
            poolConnManager.setDefaultMaxPerRoute(50);
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //获取连接
    public static CloseableHttpClient getConnection()
    {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(15000)
                .setConnectTimeout(15000).setSocketTimeout(500000).build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig).build();
        if(poolConnManager!=null&&poolConnManager.getTotalStats()!=null)
        {
            //log.info("now client pool "+poolConnManager.getTotalStats().toString());
        }
        return httpClient;
    }







    public static void downloadFile(final String url, final String dirPath){
        ThreadPoolFactory.getDownLoadPool().execute(new Runnable() {
            public void run() {
                downloadFile(url,dirPath,UrlUtil.guessFileName(url));
            }
        });



    }

    /**
     * 从网络上下载图片
     */
    public static void downloadFile(String url, String dirPath,
                                    String filePath) {
        if(httpclient==null){
            init();
            httpclient= getConnection();
        }



        HttpGet httpget = new HttpGet(url);

        httpget.setHeader(
                        "User-Agent",
                        "User-Agent:Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");

        try {
            HttpResponse resp = httpclient.execute(httpget);
            if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
                HttpEntity entity = resp.getEntity();

                InputStream in = entity.getContent();

                savePicToDisk(in, dirPath, filePath);

                System.out.println("保存图片 "+filePath+" 成功....");
            }else {
                System.out.println("code :"+resp.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //httpget.completed();

            httpget.releaseConnection();
        }
    }

    /**
     * 将图片写到 硬盘指定目录下
     * @param in
     * @param dirPath
     * @param filePath
     */
    private static void savePicToDisk(InputStream in, String dirPath,
                                      String filePath) {

        try {
            File dir = new File(dirPath);
            if (dir == null || !dir.exists()) {
                dir.mkdirs();
            }

            //文件真实路径
            String realPath = dirPath.concat(filePath);
            File file = new File(realPath);
            if (file == null || !file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
