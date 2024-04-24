package com.example.yzs.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 七牛云工具类
 */
public class QiniuUtils {
    public static String accessKey = "QWsMmyW6q4F3Ahl9j-YKLP7yjwiF8I40_-H9OPCY";
    public static String secretKey = "_w7_XJlP3GZPKpAA2gBeCQm2r9eeNVD1BUGMYJai";
    public static String bucket = "youzisang-test";
    public static String domain = "sbkhe2o39.hn-bkt.clouddn.com";
    private static boolean useHttps = false;

    public static void upload2Qiniu(String filePath, String fileName) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(filePath, fileName, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                // ignore
            }
        }
    }


    // 上传文件
    public static void upload2Qiniu(byte[] bytes, String fileName) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                // ignore
            }
        }
    }
    /**
     * 从七牛云获取指定图片的下载链接
     *
     * @param imgName 图片在七牛云上的名称
     * @return 图片的下载链接
     * @throws QiniuException 如果在生成下载链接时发生七牛异常
     */
    public static String download(String imgName) throws QiniuException {
        DownloadUrl url = new DownloadUrl(domain, useHttps, imgName);
        //生成凭证
        Auth auth = Auth.create(accessKey, secretKey);
        //设置过期时间
        long l = System.currentTimeMillis();
        //1小时，可以自定义链接过期时间
        long expireInSeconds = 3600;
        long deadline = System.currentTimeMillis()+expireInSeconds;
        //生成url
        String urlString = url.buildURL(auth, deadline);
        return urlString;
    }


    //图片回显
//    public static String getImgUrl(String fileName) {
//        // 七牛云存储空间的域名
//        return String.format("%s/%s", bucket, fileName);
//    }

    // 删除文件
    public static void deleteFileFromQiniu(String fileName) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            // 如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
