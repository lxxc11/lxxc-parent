package com.lvxc.common.utils;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Component
public class WordUtil {

    private static boolean license;

    static {
        try {
            InputStream is = WordUtil.class.getClassLoader().getResourceAsStream("license.xml");
            License lic = new License();
            lic.setLicense(is);
            license = true;
        } catch (Exception e) {
            license = false;
            log.info("license验证不通过");
            e.printStackTrace();
        }
    }

    /**
     * 通过aspose，将word转pdf
     *
     * @param response 响应
     * @param inputStream 文件流
     */
    public static void wordToPdf(HttpServletResponse response, InputStream inputStream) {
        if (!license) {
            log.info("license验证不通过");
            return;
        }
        try {
            OutputStream outputStream = response.getOutputStream();
            Document doc = new Document(inputStream);
            doc.save(outputStream, SaveFormat.PDF);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Autowired
//    private ResourceLoader resourceLoader;
//    private static WordUtil self;
//    private static Configuration configuration;
//    private static final Map<String, String> FREEMARKER_ESCAPE_CHARS = Collections.unmodifiableMap(new HashMap<String, String>() {{
//        put("&", "&amp;");
//        put("<", "&lt;");
//        put(">", "&gt;");
//    }});
//
//    @PostConstruct
//    private void init() {
//        self = this;
//        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
//        configuration.setDefaultEncoding("UTF-8");
//        try {
//            Resource resource = resourceLoader.getResource("classpath:license.xml");
//            InputStream is = resource.getInputStream();
//            License asposeLic = new License();
//            asposeLic.setLicense(is);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    public static void createWordOutputStream(String templateFile, Map<String, Object> dataMap, OutputStream os) {
//        Template t;
//        Writer out;
//        try {
//            Resource templateResource = self.resourceLoader.getResource("classpath:template/");
//            configuration.setDirectoryForTemplateLoading(templateResource.getFile());
//            t = configuration.getTemplate(templateFile); //获取模板文件
//            out = new BufferedWriter(new OutputStreamWriter(os));
//
//            t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
//            os.flush();
//            os.close();
//        } catch (TemplateException e) {
//            log.error(e.getMessage());
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    public static void createWordFile(String templateFile, String filePath, Map<String, Object> dataMap, boolean previewPdf) {
//        createWordFile(templateFile, filePath, dataMap, previewPdf, false);
//    }
//
//    /**
//     * 创建word
//     *
//     * @param templateFile
//     * @param filePath
//     * @param dataMap
//     * @param previewPdf
//     */
//    public static void createWordFile(String templateFile, String filePath, Map<String, Object> dataMap, boolean previewPdf, boolean menu) {
//        Template t = null;
//        OutputStream os = null;
//        String xmlFilePath = filePath + ".xml";
//        File outFile = new File(xmlFilePath); //导出文件
//        if (!outFile.getParentFile().exists()) {
//            outFile.getParentFile().mkdirs();
//        }
//
//        try {
//            Resource templateResource = self.resourceLoader.getResource("classpath:template/");
//            os = new FileOutputStream(outFile);
//            try (Writer out = new BufferedWriter(new OutputStreamWriter(os))) {
//                configuration.setDirectoryForTemplateLoading(templateResource.getFile());
//                t = configuration.getTemplate(templateFile, "UTF-8"); //获取模板文件
//                dataMap = JSON.parseObject(escape(JSON.toJSONString(dataMap)), Map.class);
//                t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
//            }
//        } catch (FileNotFoundException e) {
//            log.error(e.getMessage());
//        } catch (TemplateException e) {
//            log.error(e.getMessage());
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        } finally {
//            try {
//                os.flush();
//                os.close();
//            } catch (IOException e) {
//                log.error(e.getMessage());
//            }
//        }
//
//        if (menu) {
//            createMenu(xmlFilePath);
//        }
//
//        FileOutputStream wordOs = null;
//        String wordFilePath = filePath + ".docx";
//        try {
//            Document doc = new Document(xmlFilePath);
//
//            File wordFile = new File(wordFilePath);
//            wordOs = new FileOutputStream(wordFile);
//            doc.save(wordOs, SaveFormat.DOCX);
//
//            if (previewPdf) {
//                String pdfFilePath = filePath + ".pdf";
//                File pdfFile = new File(pdfFilePath);
//                try (FileOutputStream pdfOs = new FileOutputStream(pdfFile)) {
//                    doc.save(pdfOs, SaveFormat.PDF);
//                }
//            }
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        } finally {
//            try {
//                outFile.delete();
//                if (wordOs != null) {
//                    wordOs.flush();
//                    wordOs.close();
//                }
//            } catch (IOException e) {
//                log.error(e.getMessage());
//            }
//        }
//    }
//
//    public static void word2pdf(InputStream inputStream, String filePath) {
//        FileOutputStream pdfOs = null;
//        try {
//            Document doc = new Document(inputStream);
//            String pdfFilePath = filePath;
//            File pdfFile = new File(pdfFilePath);
//            pdfOs = new FileOutputStream(pdfFile);
//            doc.save(pdfOs, SaveFormat.PDF);
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        } finally {
//            try {
//                if (pdfOs != null) {
//                    pdfOs.flush();
//                    pdfOs.close();
//                }
//            } catch (IOException e) {
//                log.error(e.getMessage());
//            }
//        }
//    }
//
//    /**
//     * 创建目录（基于Aspose）
//     *
//     * @param filePath
//     */
//    public static void createMenu(String filePath) {
//        try {
//            Document doc = new Document(filePath);
//            DocumentBuilder builder = new DocumentBuilder(doc);
//            builder.getCellFormat().setWidth(PreferredWidthType.AUTO);
//            builder.moveToBookmark("Head");
//            //插入分页符
//            builder.insertBreak(BreakType.PAGE_BREAK);
//            doc.updateFields();// 更新域，不更新的话页码是错的
//            Font toc1Font = doc.getStyles().getByStyleIdentifier(StyleIdentifier.TOC_1).getFont();
//            Font toc2Font = doc.getStyles().getByStyleIdentifier(StyleIdentifier.TOC_2).getFont();
//            Font toc3Font = doc.getStyles().getByStyleIdentifier(StyleIdentifier.TOC_3).getFont();
//            toc1Font.setName("仿宋");
//            toc1Font.setSize(14);
//            toc2Font.setName("仿宋");
//            toc2Font.setSize(14);
//            toc3Font.setName("仿宋");
//            toc3Font.setSize(14);
//
//            doc.save(filePath);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 转义xml的特殊字符
//     * @param content
//     * @return
//     */
//    public static String escape(String content) {
//        for (Map.Entry<String, String> entry : FREEMARKER_ESCAPE_CHARS.entrySet()) {
//            content = content.replaceAll(entry.getKey(), entry.getValue());
//        }
//        return content;
//    }
}