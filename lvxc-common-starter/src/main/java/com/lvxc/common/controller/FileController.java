package com.lvxc.common.controller;

import cn.hutool.core.util.IdUtil;
import com.lvxc.common.utils.MinioUtil;
import com.lvxc.web.common.base.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Log4j2
@Api(tags = "通用接口-文件")
@RestController
@RequestMapping("/common/file")
public class FileController {

    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public ResponseResult<?> companyFileUpload(HttpServletRequest request, @RequestParam(required = false) String path, @RequestParam(required = false) String bucketName,@RequestParam(required = false) Boolean cover ) {
        // 暂时关闭bucketName 参数
        bucketName = null;
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取上传文件对象
        MultipartFile file = multipartRequest.getFile("file");
        String fileName = file.getOriginalFilename();
        String url = "";
        try {
            String relative =  "";
            InputStream inputStream = file.getInputStream();
            if(cover != null && cover){
                relative = "lvxc";
            }else{
                Long snowFlakeId = IdUtil.getSnowflakeNextId();
                relative = snowFlakeId.toString();
            }
            if(StringUtils.isNotBlank(path)){
                relative = relative +"/"+path;
            }
            url = MinioUtil.upload(inputStream, relative+"/"+fileName,StringUtils.isNotBlank(bucketName)?bucketName:MinioUtil.getBucketName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(url);
    }

    /**
     * 文件下载
     *
     * @param response 响应
     * @param bucketName 桶名，可以不传，不传默认为配置文件中配置的桶名
     * @param objectName 文件名（带后缀），例如：6d3bf74b4c88ffff0cf29ff7c467126.jpg，如果在某个文件夹中，则带上文件夹名称，例如：template/6d3bf74b4c88ffff0cf29ff7c467126.jpg
     */
    @SneakyThrows
    @ApiOperation(value = "文件下载")
    @PostMapping("/download")
    public void download(HttpServletResponse response, @RequestParam(required = false) String bucketName, String objectName) {
        InputStream inputStream = MinioUtil.getMinioFile(bucketName, objectName);
        byte[] b = new byte[1024];
        int len;
        while ((len = inputStream.read(b)) > 0) {
            response.getOutputStream().write(b, 0, len);
        }
    }
}
