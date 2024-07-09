package com.lvxc.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.common.utils.excel.EasyExcelUtils;
import com.lvxc.common.utils.excel.MyEasyExceBaseListener;
import com.lvxc.user.entity.DemoCompany;
import com.lvxc.user.mapper.flw.DemoCompanyMapper;
import com.lvxc.user.service.DemoCompanyService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 企业demo表 服务实现类
 * </p>
 *
 * @author lvxc
 * @since 2023-08-31
 */
@Service
public class DemoCompanyServiceImpl extends ServiceImpl<DemoCompanyMapper, DemoCompany> implements DemoCompanyService {
    @Override
    public String batchDownLoad(HttpServletResponse response, DemoCompany demoCompany)  throws IOException {
        List<DemoCompany> demoCompanys = baseMapper.getList(demoCompany);
        EasyExcelUtils.downloadExcelData(response,demoCompanys,"data");
        return  "success";
    }

    @Override
    public void batchUpLoad(MultipartFile file, HttpServletRequest request) {
        List<DemoCompany> commonModelData = EasyExcelUtils.getUploadExcelData(file,new MyEasyExceBaseListener<>(),  DemoCompany.class);
        saveBatch(commonModelData);
    }

    @Override
    public IPage<DemoCompany> getPageList(Page<DemoCompany> page, DemoCompany demoCompany) {
        return baseMapper.getPageList(page, demoCompany);
    }
}
