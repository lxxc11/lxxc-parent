package com.lvxc.doc.config.util;

import com.aspose.words.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DocWordUtil {
    @Autowired
    private ObjectMapper objectMapper;

    private static DocWordUtil self;

    public static final String PDF  = "application/pdf";
    public static final String DOC  = "application/msword";
    public static final String DOCX  = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String XLSX  = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private static Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    private static final Map<String, String> FREEMARKER_ENCODE_STRS = Collections.unmodifiableMap(new HashMap<String, String>(){{
        put("&","&amp;");
        put("<","&lt;");
        put(">","&gt;");
    }});

    static{
        configuration.setDefaultEncoding("UTF-8");
        try {
            InputStream is = new FileInputStream(new File("./license.xml"));
            License aposeLic = new License();
            aposeLic.setLicense(is);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @PostConstruct
    private void init() {
        self = this;
    }

    public static void downloadWord(String templateFile, Map<String, Object> dataMap, String filename, HttpServletResponse response){
        OutputStream os = null;
        Template t=null;
        Writer out = null;
        try {
            response.reset();
            // 生成文件名
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String(filename.getBytes("GB2312"), "ISO8859-1") + ".doc");
            response.setContentType("application/msword");
            os = response.getOutputStream();

            configuration.setClassLoaderForTemplateLoading(self.getClass().getClassLoader(),"template");
            t = configuration.getTemplate(templateFile,"UTF-8"); //获取模板文件
            out = new BufferedWriter(new OutputStreamWriter(os));

            t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
            os.flush();
            os.close();
        } catch (TemplateException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void createWord(String templateFile, String filePath, Map<String, Object> dataMap, boolean previewPdf){
        createWord(templateFile,filePath,dataMap,previewPdf,false);
    }

    public static void createWordNew(String templateFile, String filePath, Object dataMap, boolean previewPdf){
        createWordNew(templateFile,filePath,dataMap,previewPdf,false);
    }
    /**
     * 创建word
     * @param templateFile
     * @param filePath
     * @param dataMap
     * @param previewPdf
     */
    public static File createWord(String templateFile, String filePath, Map<String, Object> dataMap, boolean previewPdf, boolean menu){
        Template t = null;
        OutputStream os = null;
        String xmlFilePath = filePath + ".doc";
        File outFile = new File(xmlFilePath); //导出文件
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        try{
            os = new FileOutputStream(outFile);
            try(Writer out = new BufferedWriter(new OutputStreamWriter(os))){
//                configuration.setDirectoryForTemplateLoading(new File("./template"));
                configuration.setClassLoaderForTemplateLoading(self.getClass().getClassLoader(),"templates");
                t = configuration.getTemplate(templateFile,"UTF-8"); //获取模板文件
                dataMap = self.objectMapper.readValue(format(self.objectMapper.writeValueAsString(dataMap)), Map.class);
                t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
            }
        } catch (TemplateException | IOException e) {
            log.error("生成word错误",e);
        } finally {
            try {
                os.flush();
                os.close();
            }catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        if(menu){
            createMenu(xmlFilePath);
        }

        FileOutputStream wordOs = null;

        try {
            Document doc = new Document(xmlFilePath);
            //更新目录
            doc.updateFields();
            if(previewPdf) {
                String pdfFilePath = filePath + ".pdf";
                File pdfFile = new File(pdfFilePath);
                try(FileOutputStream pdfOs = new FileOutputStream(pdfFile)){
                    doc.save(pdfOs, SaveFormat.PDF);
                }
                return pdfFile;
            }else {
                String wordFilePath = filePath + ".docx";
                File wordFile = new File(wordFilePath);
                wordOs = new FileOutputStream(wordFile);
                doc.save(wordOs, SaveFormat.DOCX);
                return wordFile;
            }
        } catch (Exception e){
            log.error("生成word异常",e);
        } finally {
            try {
                outFile.delete();
                if(wordOs!=null) {
                    wordOs.flush();
                    wordOs.close();
                }
            }catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 创建word
     * @param templateFile
     * @param filePath
     * @param dataMap
     * @param previewPdf
     */
    public static void createWordNew(String templateFile, String filePath, Object dataMap, boolean previewPdf, boolean menu){
        Template t = null;
        OutputStream os = null;
        String xmlFilePath = filePath + ".doc";
        File outFile = new File(xmlFilePath); //导出文件
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        try{
            os = new FileOutputStream(outFile);
            try(Writer out = new BufferedWriter(new OutputStreamWriter(os))){
//                configuration.setDirectoryForTemplateLoading(new File("./template"));
                configuration.setClassLoaderForTemplateLoading(self.getClass().getClassLoader(),"template");
                t = configuration.getTemplate(templateFile,"UTF-8"); //获取模板文件
                dataMap = self.objectMapper.readValue(format(self.objectMapper.writeValueAsString(dataMap)), Map.class);
                t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (TemplateException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                os.flush();
                os.close();
            }catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        if(menu){
            createMenu(xmlFilePath);
        }

        FileOutputStream wordOs = null;
        String wordFilePath = filePath + ".docx";
        try {
            Document doc = new Document(xmlFilePath);

            File wordFile = new File(wordFilePath);
            wordOs = new FileOutputStream(wordFile);
            doc.save(wordOs, SaveFormat.DOCX);

            if(previewPdf) {
                String pdfFilePath = filePath + ".pdf";
                File pdfFile = new File(pdfFilePath);
                try(FileOutputStream pdfOs = new FileOutputStream(pdfFile)){
                    doc.save(pdfOs, SaveFormat.PDF);
                }
            }
        }catch (IOException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        } finally {
            try {
                outFile.delete();
                if(wordOs!=null) {
                    wordOs.flush();
                    wordOs.close();
                }
            }catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }



    /**
     * 创建目录（基于Aspose）
     * @param filePath
     */
    public static void createMenu(String filePath){
        try {
            Document doc = new Document(filePath);
            DocumentBuilder builder = new DocumentBuilder(doc);
            builder.getCellFormat().setWidth(PreferredWidthType.AUTO);
            builder.moveToBookmark("Head");
            //插入分页符
            builder.insertBreak(BreakType.PAGE_BREAK);
            doc.updateFields();// 更新域，不更新的话页码是错的
            Font toc1Font = doc.getStyles().getByStyleIdentifier(StyleIdentifier.TOC_1).getFont();
            Font toc2Font = doc.getStyles().getByStyleIdentifier(StyleIdentifier.TOC_2).getFont();
            Font toc3Font = doc.getStyles().getByStyleIdentifier(StyleIdentifier.TOC_3).getFont();
            toc1Font.setName("仿宋");
            toc1Font.setSize(14);
            toc2Font.setName("仿宋");
            toc2Font.setSize(14);
            toc3Font.setName("仿宋");
            toc3Font.setSize(14);

            doc.save(filePath);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public static String format(String input) {
        for (Map.Entry<String, String> entry : FREEMARKER_ENCODE_STRS.entrySet()) {
            input = input.replaceAll(entry.getKey(), entry.getValue());
        }
        //上面的会把<>转义导致<w:br/>失效，单独处理\n换行逻辑
        input = input.replaceAll("\\\\n","<w:br/>");
        return input;
    }



}
