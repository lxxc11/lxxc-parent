package com.lvxc.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.SysLogDto;
import com.lvxc.user.entity.SysLog;
import com.lvxc.user.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Api(tags = "日志管理")
@RestController
@RequestMapping("/log")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService ;

    @ApiOperation(value = "用户中心-日志表-分页列表查询", notes = "用户中心-日志表-分页列表查询")
    @GetMapping(value = "/pageList")
    public ResponseResult<?> queryPageList(SysLogDto dto,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysLog> page = new Page<>(pageNo, pageSize);
        IPage<SysLog> pageList = sysLogService.pageList(page,dto);
        return ResponseResult.success(pageList);
    }

    @ApiOperation(value = "用户中心-日志表-导出", notes = "用户中心-日志表-导出")
    @PostMapping(value = "/batchDownLoad")
    public com.lvxc.web.common.base.ResponseResult<?> batchDownLoad(@RequestBody SysLogDto dto, HttpServletResponse response) {
        try {
            sysLogService.batchDownLoad(response, dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
