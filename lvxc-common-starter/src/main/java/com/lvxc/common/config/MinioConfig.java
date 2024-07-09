package com.lvxc.common.config;

import com.lvxc.common.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Minio文件上传配置文件
 */
@Slf4j
@Configuration
public class MinioConfig {
    @Value(value = "${minio.minio_url}")
    private String minioUrl;
    @Value(value = "${minio.download_url}")
    private String downloadUrl;
    @Value(value = "${minio.minio_name}")
    private String minioName;
    @Value(value = "${minio.minio_pass}")
    private String minioPass;
    @Value(value = "${minio.bucketName}")
    private String bucketName;
    @Value(value = "#{'${minio.contentType:application/pdf,image/gif,image/png,image/bmp,image/jpeg,application/x-font-ttf,text/css,application/font-woff}'.replace(' ','').split(',')}")
    private List<String> contentType;
    @Bean
    public void initMinio(){
        if(!minioUrl.startsWith("http")){
            minioUrl = "http://" + minioUrl;
        }
        MinioUtil.setMinioUrl(minioUrl);
        MinioUtil.setDownloadUrl(downloadUrl);
        MinioUtil.setMinioName(minioName);
        MinioUtil.setMinioPass(minioPass);
        MinioUtil.setBucketName(bucketName);
        MinioUtil.setContentType(contentType);
    }

}
