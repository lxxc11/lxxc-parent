package com.lvxc.user.controller.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lvxc.user.domain.dto.demo.DemoTableDto;
import com.lvxc.user.entity.DemoTable;
import com.lvxc.user.service.IDemoTableService;
import com.lvxc.web.common.base.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * demo表结构 前端控制器
 * </p>
 *
 * @author lvxc
 * @since 2023-08-16
 */
@Api(tags = "[demo表结构]模块")
@RestController
@RequestMapping("/demoTable")
public class DemoTableController {

    @Autowired
    private IDemoTableService iDemoTableService;


    @ApiOperation(value = "demo表结构:插入", notes = "demo表结构:插入")
    @PostMapping(value = "/insert")
    @ApiOperationSupport(ignoreParameters = {"demoTable.id", "demoTable.createBy", "demoTable.updateBy", "demoTable.createTime", "demoTable.updateTime", "demoTable.current", "demoTable.pageSize", "demoTable.logicDelete"})
    public ResponseResult<Boolean> insertDemoTable(@RequestBody DemoTable demoTable) {
        return iDemoTableService.save(demoTable) ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "demo表结构:删除", notes = "demo表结构:删除")
    @PostMapping(value = "/delete/{id}")
    public ResponseResult<Boolean> deleteDemoTableById(@PathVariable("id") Serializable id) {
        return iDemoTableService.removeById(id) ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "demo表结构:修改", notes = "demo表结构:修改")
    @ApiOperationSupport(ignoreParameters = {"demoTable.createBy", "demoTable.updateBy", "demoTable.createTime", "demoTable.updateTime", "demoTable.current", "demoTable.pageSize", "demoTable.logicDelete"})
    @PostMapping(value = "/update")
    public ResponseResult<Boolean> updateDemoTable(@RequestBody DemoTable demoTable) {
        return iDemoTableService.updateById(demoTable) ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "demo表结构:分页", notes = "demo表结构:分页")
    @ApiOperationSupport(ignoreParameters = {"demoTable.id", "demoTable.createBy", "demoTable.updateBy", "demoTable.createTime", "demoTable.updateTime", "demoTable.current", "demoTable.pageSize", "demoTable.logicDelete"})
    @PostMapping(value = "/page")
    public ResponseResult<Page<DemoTable>> pageDemoTable(@RequestBody DemoTable demoTable) {
        QueryWrapper<DemoTable> demoTableQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(demoTable.getId())) {
            demoTableQueryWrapper.like(DemoTable.ID, demoTable.getId());
        }
        if (StringUtils.isNotBlank(demoTable.getTextContent())) {
            demoTableQueryWrapper.like(DemoTable.TEXT_CONTENT, demoTable.getTextContent());
        }
        if (StringUtils.isNotBlank(demoTable.getPwdContent())) {
            demoTableQueryWrapper.like(DemoTable.PWD_CONTENT, demoTable.getPwdContent());
        }
        if (StringUtils.isNotBlank(demoTable.getCellContent())) {
            demoTableQueryWrapper.like(DemoTable.CELL_CONTENT, demoTable.getCellContent());
        }
        if (StringUtils.isNotBlank(demoTable.getCheckboxContent())) {
            demoTableQueryWrapper.like(DemoTable.CHECKBOX_CONTENT, demoTable.getCheckboxContent());
        }
        if (StringUtils.isNotBlank(demoTable.getRadioboxContent())) {
            demoTableQueryWrapper.like(DemoTable.RADIOBOX_CONTENT, demoTable.getRadioboxContent());
        }
        if (StringUtils.isNotBlank(demoTable.getInputfieldContent())) {
            demoTableQueryWrapper.like(DemoTable.INPUTFIELD_CONTENT, demoTable.getInputfieldContent());
        }
        if (StringUtils.isNotBlank(demoTable.getRichtextContent())) {
            demoTableQueryWrapper.like(DemoTable.RICHTEXT_CONTENT, demoTable.getRichtextContent());
        }
        return ResponseResult.success(iDemoTableService
                .page(demoTable.page()
                        , demoTableQueryWrapper));
    }

    @ApiOperation(value = "demo表结构:列表", notes = "demo表结构:列表")
    @ApiOperationSupport(ignoreParameters = {"demoTable.id", "demoTable.createBy", "demoTable.updateBy", "demoTable.createTime", "demoTable.updateTime", "demoTable.current", "demoTable.pageSize", "demoTable.logicDelete"})
    @PostMapping(value = "/list")
    public ResponseResult<List<DemoTable>> listDemoTable(@RequestBody DemoTable demoTable) {
        return ResponseResult.success(iDemoTableService
                .list(new QueryWrapper<>(demoTable)));
    }

    @ApiOperation(value = "demo表结构:通过id查询", notes = "demo表结构:通过id查询")
    @GetMapping(value = "/find/{id}")
    public ResponseResult<DemoTable> findDemoTableById(@PathVariable("id") Serializable id) {
        return ResponseResult.success(iDemoTableService.getById(id));
    }

    @ApiOperation(value = "demo表结构-导出", notes = "demo表结构-导出")
    @PostMapping(value = "/batchDownLoad")
    public ResponseResult<String> batchDownLoad(@RequestBody DemoTableDto demoTableDto, HttpServletResponse response) {
        String s = null;
        try {
            s = iDemoTableService.batchDownLoad(response,demoTableDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.success(s);
    }

    @ApiOperation(value = "demo表结构-导入", notes = "demo表结构-导入")
    @PostMapping(value = "/batchUpLoad")
    public ResponseResult<String> batchUpLoad( @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        iDemoTableService.batchUpLoad(file,request);
        return ResponseResult.success();
    }

}
