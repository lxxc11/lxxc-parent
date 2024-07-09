package com.lvxc.user.controller.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.entity.DemoCompany;
import com.lvxc.user.service.DemoCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 企业demo表 前端控制器
 * </p>
 *
 * @author lvxc
 * @since 2023-08-31
 */
@Api(tags = "[企业demo表]模块")
@RestController
@RequestMapping("/demo/company")
public class DemoCompanyController {

    @Autowired
    private DemoCompanyService demoCompanyService;


    @ApiOperation(value = "企业demo表:插入", notes = "企业demo表:插入")
    @PostMapping(value = "/insert")
    public ResponseResult<Boolean> insertDemoCompany(@RequestBody DemoCompany demoCompany) {
        return demoCompanyService.save(demoCompany) ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "企业demo表:删除", notes = "企业demo表:删除")
    @PostMapping(value = "/delete/{id}")
    public ResponseResult<Boolean> deleteDemoCompanyById(@PathVariable("id") Serializable id) {
        return demoCompanyService.removeById(id) ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "企业demo表:修改", notes = "企业demo表:修改")
    @PostMapping(value = "/update")
    public ResponseResult<Boolean> updateDemoCompany(@RequestBody DemoCompany demoCompany) {
        return demoCompanyService.updateById(demoCompany) ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "企业demo表:分页", notes = "企业demo表:分页")
    @PostMapping(value = "/page")
    public ResponseResult<?> pageDemoCompany(@RequestBody DemoCompany demoCompany) {
        Page<DemoCompany> page = new Page<>(demoCompany.getCurrent(),demoCompany.getPageSize());
        IPage<DemoCompany> pageList = demoCompanyService.getPageList(page, demoCompany);
        return ResponseResult.success(pageList);
    }

    @ApiOperation(value = "企业demo表:列表", notes = "企业demo表:列表")
    @PostMapping(value = "/list")
    public ResponseResult<List<DemoCompany>> listDemoCompany(@RequestBody DemoCompany demoCompany) {
        return ResponseResult.success(demoCompanyService
                .list(new QueryWrapper<>(demoCompany)));
    }

    @ApiOperation(value = "企业demo表:通过id查询", notes = "企业demo表:通过id查询")
    @GetMapping(value = "/find/{id}")
    public ResponseResult<DemoCompany> findDemoCompanyById(@PathVariable("id") Serializable id) {
        return ResponseResult.success(demoCompanyService.getById(id));
    }

    @ApiOperation(value = "企业demo表-导出", notes = "企业demo表-导出")
    @PostMapping(value = "/batchDownLoad")
    public ResponseResult<String> batchDownLoad(@RequestBody DemoCompany demoCompany, HttpServletResponse response) {
        String s = null;
        try {
            s = demoCompanyService.batchDownLoad(response,demoCompany);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.success(s);
    }

    @ApiOperation(value = "demo表结构-导入", notes = "demo表结构-导入")
    @PostMapping(value = "/batchUpLoad")
    public ResponseResult<String> batchUpLoad(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        demoCompanyService.batchUpLoad(file,request);
        return ResponseResult.success();
    }
}
