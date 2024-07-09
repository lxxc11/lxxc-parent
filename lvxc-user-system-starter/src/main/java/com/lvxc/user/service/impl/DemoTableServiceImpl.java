package com.lvxc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.common.utils.excel.EasyExcelUtils;
import com.lvxc.common.utils.excel.MyEasyExceBaseListener;
import com.lvxc.user.domain.dto.demo.DemoTableDto;
import com.lvxc.user.entity.DemoTable;
import com.lvxc.user.mapper.flw.DemoTableMapper;
import com.lvxc.user.service.IDemoTableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * demo表结构 服务实现类
 * </p>
 *
 * @author lvxc
 * @since 2023-08-16
 */
@Service
public class DemoTableServiceImpl extends ServiceImpl<DemoTableMapper, DemoTable> implements IDemoTableService {
    @Override
    public String batchDownLoad(HttpServletResponse response, DemoTableDto demoTableDto) throws IOException {
        QueryWrapper<DemoTable> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(demoTableDto.getId())){
            queryWrapper.in("id",demoTableDto.getId().split(","));
        }
        List<DemoTable> demoTables = baseMapper.selectList(queryWrapper);
        EasyExcelUtils.downloadExcelData(response,demoTables,"data");
        return  "success";
    }

    @Override
    public void batchUpLoad(MultipartFile file, HttpServletRequest request) {
        List<DemoTable> commonModelData = EasyExcelUtils.getUploadExcelData(file,new MyEasyExceBaseListener<>(),  DemoTable.class);
        saveBatch(commonModelData);
    }
}
