package com.lvxc.common.utils;

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.internal.Mimetypes;
import com.lvxc.web.common.base.HsServerException;
import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * minio文件上传工具类
 */
public class MinioUtil {
    private static String minioUrl;
    private static String downloadUrl;
    private static String minioName;
    private static String minioPass;
    private static String bucketName;
    private static List<String> contentType;

    public static void setMinioUrl(String minioUrl) {
        MinioUtil.minioUrl = minioUrl;
    }

    public static void setDownloadUrl(String downloadUrl) {
        MinioUtil.downloadUrl = downloadUrl;
    }

    public static void setMinioName(String minioName) {
        MinioUtil.minioName = minioName;
    }

    public static void setMinioPass(String minioPass) {
        MinioUtil.minioPass = minioPass;
    }

    public static void setBucketName(String bucketName) {
        MinioUtil.bucketName = bucketName;
    }

    public static String getMinioUrl() {
        return minioUrl;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static void setContentType(List<String> contentType) {
        MinioUtil.contentType = contentType;
    }

    private static MinioClient minioClient = null;

    /**
     * 初始化客户端
     *
     * @param minioUrl
     * @param minioName
     * @param minioPass
     * @return
     */
    private static MinioClient initMinio(String minioUrl, String minioName, String minioPass) {
        if (minioClient == null) {
            try {
                minioClient = MinioClient.builder().endpoint(minioUrl)
                        .credentials(minioName, minioPass)
                        .build();
                minioClient.ignoreCertCheck();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
        return minioClient;
    }


    /**
     * 上传文件到minio
     *
     * @param stream
     * @param relativePath
     * @return
     */
    public static String upload(InputStream stream, String relativePath, String customBucket) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, ErrorResponseException, XmlParserException, ServerException, InvalidResponseException {
        return upload(stream, relativePath, customBucket, getContentType(relativePath));
    }

    private static String getContentType(String relativePath) {
        String returnFileName = "application/octet-stream";
        //支持预览文件格式集合
        String mimetype = Mimetypes.getInstance().getMimetype(relativePath);
        if(contentType.contains(mimetype)){
            returnFileName = mimetype;
        }
        return returnFileName;
    }

    /**
     * 上传文件到minio
     *
     * @param stream
     * @param relativePath
     * @return
     */
    public static String upload(InputStream stream, String relativePath, String customBucket, String contentType) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, ErrorResponseException, XmlParserException, ServerException, InvalidResponseException {
        String newBucket = bucketName;
        if (StringUtils.isNotBlank(customBucket)) {
            newBucket = customBucket;
        }
        initMinio(minioUrl, minioName, minioPass);
        initBucket(newBucket);
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .stream(stream, stream.available(), PutObjectArgs.MAX_PART_SIZE)
                    .contentType(contentType)
                    .bucket(newBucket)
                    .object(relativePath)
                    .build();
            minioClient.putObject(args);
        } catch (Exception e) {
            throw new HsServerException(-1,"上传文件到MINIO失败");
        }
        stream.close();

        return downloadUrl + "/" + newBucket + "/" + relativePath;
    }



    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public static String upload(MultipartFile file, String bizPath, String customBucket) {
        return upload(file, bizPath, customBucket, getContentType(file.getOriginalFilename()), null);
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public static String upload(MultipartFile file, String bizPath, String customBucket, String contentType, String customName) {
        String file_url = "";
        String newBucket = bucketName;
        if (StringUtils.isNotBlank(customBucket)) {
            newBucket = customBucket;
        }
        try {
            initMinio(minioUrl, minioName, minioPass);
            // 初始化bucket，检查存储桶是否已经存在
            initBucket(newBucket);
            InputStream stream = file.getInputStream();
            // 获取文件名
            String orgName = file.getOriginalFilename();
            orgName = FileUtil.getName(orgName);
//            orgName = CommonUtil.getFileName(orgName);
            String objectName = bizPath + "/" + orgName;
            if (StringUtils.isNotBlank(customName)) {
                objectName = bizPath + "/" + customName;
            }
            // 使用putObject上传一个本地文件到存储桶中。
            PutObjectArgs args = PutObjectArgs.builder()
                    .stream(stream, stream.available(), PutObjectArgs.MAX_PART_SIZE)
                    .contentType(contentType)
                    .bucket(newBucket)
                    .object(objectName)
                    .build();
            minioClient.putObject(args);

            stream.close();
            file_url = minioUrl + "/" + newBucket + "/" + objectName;
        } catch (Exception e) {
            throw new HsServerException(-1,"上传文件到MINIO失败");
        }
        return file_url;
    }


    public static void initBucket(String bucket) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, ErrorResponseException, XmlParserException, ServerException, InvalidResponseException {
        if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
//            log.info("Bucket already exists.");
        } else {
            // 创建一个名为ota的存储桶
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
//            setReadOnly(bucket);
        }
    }


    public static void setReadOnly(String bucketName) {
        try {
            //检查存储桶是否已经存在
            SetBucketPolicyArgs.builder().bucket(bucketName).config("readonly").build();
        } catch (Exception e) {
            throw new HsServerException(-1,"上传文件到MINIO失败");
        }
    }

    /**
     * 微信小程序在上传文件时会将文件名进行加密处理，从而导致文件名展示有问题，该方法可以把指定文件的文件名修改成新的文件名
     * @param multipartFile 待处理的文件
     * @param name 新的文件名
     * @return 已修改成新的文件名的文件
     * @throws IOException
     */
    public static MultipartFile renameFile(MultipartFile multipartFile, String name) throws IOException {
        InputStream inputStream = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String prefix = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            // 临时文件
            File file = File.createTempFile(prefix, "." + suffix);
            multipartFile.transferTo(file);
            String fileName = name + "." + suffix;
            // 重命名
            file = FileUtil.rename(file, fileName, true, true);
            inputStream = new FileInputStream(file);
            // File转换成MultipartFile
            multipartFile = new MockMultipartFile(fileName, fileName,
                    ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
            return multipartFile;
        } catch (Exception e) {
            throw new HsServerException(-1,"修改文件名失败");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 获取文件流
     *
     * @param transferBucketName
     * @param objectName
     * @return
     */
    public static InputStream getMinioFile(String transferBucketName, String objectName) {
        InputStream inputStream = null;
        try {
            initMinio(minioUrl, minioName, minioPass);
            if (StringUtils.isBlank(transferBucketName)) {
                transferBucketName = bucketName;
            }
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(transferBucketName).object(objectName).build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new HsServerException(500, "文件获取失败！");
        }
        return inputStream;
    }
}
