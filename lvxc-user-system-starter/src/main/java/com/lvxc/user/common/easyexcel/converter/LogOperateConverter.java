package com.lvxc.user.common.easyexcel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.lvxc.user.common.constants.MapConstant;

public class LogOperateConverter implements Converter<Integer> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Integer key, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String value = MapConstant.logOperateMap.get(key);
        return new WriteCellData<>(value);
    }
}
