package com.lvxc.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.entity.DemoCompany;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 企业demo表 服务类
 * </p>
 *
 * @author lvxc
 * @since 2023-08-31
 */
public interface DemoCompanyService extends IService<DemoCompany> {

    String batchDownLoad(HttpServletResponse response, DemoCompany demoCompanyDto)  throws IOException;

    void batchUpLoad(MultipartFile file, HttpServletRequest request);

    IPage<DemoCompany> getPageList(Page<DemoCompany> page, DemoCompany demoCompany);
}
