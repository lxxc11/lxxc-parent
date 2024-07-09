package com.lvxc.common.utils;

import com.lvxc.web.common.base.HsServerException;
import com.lvxc.web.common.base.ResultEnum;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author caoyq
 * @Date 2023/9/7 11:40
 * @PackageName:com.lvxc.common.utils
 * @ClassName: ZipDownloadUtils
 * @Description 文件zip包压缩下载
 * @Version 1.0
 */
public class ZipDownloadUtils {


    /**
     * 需要压缩的文件夹
     */
    private final static String DOWN_LOAD_DIR = "download";
    /**
     * 打包后的文件夹
     */
    private final static String DOWN_LOAD_ZIP = "downloadZip";
    /**
     * 后缀名
     */
    private final static String ZIP = ".zip";

    /**
     * 根据文件资源地址集合压缩zip包
     * @param collection 资源集合
     * @param fileName 压缩包名称
     * @param request 请求
     * @param response 响应
     */
    public static void dowLoadZip(Collection<String> collection, String fileName, HttpServletRequest request, HttpServletResponse response) {
        for (String url : collection) {
            //下载查出的图片
            downloadFile(url);
        }
        // 获取文件名，如果未传递文件名，则使用默认文件名
        String zipName = (StringUtils.isNotBlank(fileName) ?
                fileName.contains(ZIP) ? fileName : fileName + ZIP : "defaultFileName" + ZIP);
        //把下载的图片文件夹压缩
        downloadZip(zipName, request, response);
    }


    /**
     * 通过图片url下载图片到指定文件夹
     *
     * @param downloadUrl 图片url
     */
    public static void downloadFile(String downloadUrl) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //获取连接
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3 * 1000);
            //设置请求头
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36");
            //获取输入流
            inputStream = connection.getInputStream();

            File fileDir = new File(DOWN_LOAD_DIR);
            if (!fileDir.exists()) {//如果文件夹不存在
                fileDir.mkdir();//创建文件夹
            }

            //截取文件名称，可以把 / 换成需要的规则
            String filePath = DOWN_LOAD_DIR + "/" + downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            File file = new File(filePath);
            file.createNewFile();//创建文件，存在覆盖

            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            new HsServerException(ResultEnum.EXECUTE_FAIL.getCode(), "文件下载出错：" + e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                throw new HsServerException(ResultEnum.EXECUTE_FAIL.getCode(), "关闭流出错：" + e);
            }
        }
    }


    /**
     * @param zipName 文件名
     */
    public static void downloadZip(String zipName, HttpServletRequest request, HttpServletResponse res) {
        OutputStream out = null;
        File zip = null;
        try {
            //创建压缩文件需要的空的zip包
            String zipFilePath = DOWN_LOAD_ZIP + File.separator + zipName;

            File fileDir = new File(DOWN_LOAD_ZIP);
            if (!fileDir.exists()) {//如果文件夹不存在
                fileDir.mkdir();//创建文件夹
            }

            //压缩文件
            zip = new File(zipFilePath);
            zip.createNewFile();//创建文件，存在覆盖

            //创建zip文件输出流
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
            zipFile(DOWN_LOAD_DIR, zos);
            zos.close();

            //将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFilePath));
            byte[] buff = new byte[bis.available()];
            bis.read(buff);
            bis.close();

            //IO流实现下载的功能
            res.setCharacterEncoding("UTF-8"); //设置编码字符
            res.setContentType("application/octet-stream;charset=UTF-8"); //设置下载内容类型application/octet-stream（二进制流，未知文件类型）；

            //防止文件名乱码
            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            if (userAgent.contains("firefox")) {//火狐浏览器
                zipName = new String(zipName.getBytes(), "ISO8859-1");
            } else {
                zipName = URLEncoder.encode(zipName, "UTF-8");//其他浏览器
            }

            res.setHeader("Content-disposition", "attachment;filename=" + zipName);//设置下载的压缩文件名称
            out = res.getOutputStream();   //创建页面返回方式为输出流，会自动弹出下载框
            out.write(buff);//输出数据文件

        } catch (Exception e) {
            throw new HsServerException(ResultEnum.EXECUTE_FAIL.getCode(), "压缩包下载出错：" + e);
        } finally {
            if (out != null) {
                try {
                    out.flush();//释放缓存
                    out.close();//关闭输出流
                } catch (IOException e) {
                    throw new HsServerException(ResultEnum.EXECUTE_FAIL.getCode(), "释放或关闭输出流失败");
                }

            }

            //下载完后删除文件夹和压缩包
            File fileDir = new File(DOWN_LOAD_DIR);
            try {
                org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory(fileDir);
            } catch (IOException e) {
                throw new HsServerException(ResultEnum.EXECUTE_FAIL.getCode(), "删除临时文件夹失败");
            }
            if (zip != null) {
                zip.delete();
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param filePath 需要压缩的文件夹
     * @param zos      zip文件输出流
     */
    private static void zipFile(String filePath, ZipOutputStream zos) throws IOException {
        File inputFile = new File(filePath);  //根据文件路径创建文件
        if (inputFile.exists()) { //判断文件是否存在
            if (inputFile.isFile()) {  //判断是否属于文件，还是文件夹
                //创建输入流读取文件
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));

                //将文件写入zip内，即将文件进行打包
                zos.putNextEntry(new ZipEntry(inputFile.getName()));

                //写入文件的方法，同上
                int size = 0;
                byte[] buffer = new byte[1024];  //设置读取数据缓存大小
                while ((size = bis.read(buffer)) > 0) {
                    zos.write(buffer, 0, size);
                }
                //关闭输入输出流
                zos.closeEntry();
                bis.close();

            } else {  //如果是文件夹，获取文件夹中内容一一写入zip
                try {
                    File[] files = inputFile.listFiles();
                    for (File fileTem : files) {
                        zipFile(fileTem.toString(), zos);
                    }
                } catch (Exception e) {
                    throw new HsServerException(ResultEnum.EXECUTE_FAIL.getCode(), "压缩文件出错：" + e);
                }
            }
        }
    }



}
