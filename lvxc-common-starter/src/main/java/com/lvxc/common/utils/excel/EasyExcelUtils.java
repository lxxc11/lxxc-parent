package com.lvxc.common.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.lvxc.common.dto.ExcelSheetListDto;
import com.lvxc.common.utils.excel.converter.ObjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class EasyExcelUtils {

    /**
     * 获取excel表格里读取到的数据
     *
     * @param file
     * @param excelClass
     * @param <T>
     * @return
     */
    public static <T> List<T> getUploadExcelData(MultipartFile file, MyEasyExceBaseListener<T> myEasyExcelReadListener, Class<? extends T> excelClass) {
        String name = file.getOriginalFilename();
        Assert.isTrue(name.endsWith(ExcelTypeEnum.XLSX.getValue()), "错误的文件类型");
        ExcelReader reader = null;
        try {
            reader = new ExcelReaderBuilder()
                    .file(file.getInputStream()).excelType(ExcelTypeEnum.XLSX).build();
            ReadSheet sheetOne = new ExcelReaderSheetBuilder().headRowNumber(1).registerReadListener(myEasyExcelReadListener).registerConverter(new ObjectConverter())
                    .sheetNo(0).head(excelClass).build();
            reader.read(sheetOne);
            List<T> list = myEasyExcelReadListener.getList();
            reader.finish();
            return list;
        } catch (IOException e) {
            log.error("文件上传失败");
        }
        return null;
    }


    public static void downloadExcelData(HttpServletResponse response, List<?> list, String sheetName) throws IOException {
        if (CollectionUtils.isEmpty(list)) {
            log.warn("导出数据为空!");
            return;
        }
        Class<?> clazz = list.get(0).getClass();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(StringUtils.isNotBlank(sheetName) ? sheetName + ".xlsx" : "download.xlsx", StandardCharsets.UTF_8.toString()));
        EasyExcel.write(response.getOutputStream(), clazz).registerConverter(new ObjectConverter()).excelType(ExcelTypeEnum.XLSX).sheet(0, sheetName).doWrite(list);
    }

    //excel多个sheet导出
    public static void exportSheets(HttpServletResponse response, List<ExcelSheetListDto> sheetListDtos, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(StringUtils.isNotBlank(fileName) ? fileName + ".xlsx" : "download.xlsx", StandardCharsets.UTF_8.toString()));
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        for (int i = 0; i < sheetListDtos.size(); i++) {
            ExcelSheetListDto sheetDto = sheetListDtos.get(i);
            WriteSheet studentInfo = EasyExcel.writerSheet(i, sheetDto.getSheetName())
                    .head(sheetDto.getTClass()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .build();
            excelWriter.write(sheetDto.getDataList(), studentInfo);
        }
        excelWriter.finish();
    }

}
