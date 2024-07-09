package com.lvxc.common.utils;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
public class NetFileUtil {
    public static String read(String fileURI) {
        try (InputStream in = new URL(fileURI).openConnection().getInputStream()) {
            FastByteArrayOutputStream stream = IoUtil.read(in);
            byte[] bytes = stream.toByteArray();
            String charset = getByteCharset(bytes);
            System.out.println("文件的编码格式为：" + charset);
            return new String(bytes, charset);
        } catch (IOException e) {
            log.error("读取失败:" + e.getMessage());
            throw new RuntimeException("读取文件异常：" + e.getMessage());
        }
    }

    //判断byte编码类型
    public static String getByteCharset(byte[] bytes) {
        String DEFAULT_ENCODING = "UTF-8";
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        return encoding;
    }
}
