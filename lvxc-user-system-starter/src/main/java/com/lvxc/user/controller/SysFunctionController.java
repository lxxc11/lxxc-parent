package com.lvxc.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysFunctionDto;
import com.lvxc.user.domain.vo.SysFunctionVo;
import com.lvxc.user.entity.SysFunction;
import com.lvxc.user.service.ISysFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 用户中心-功能点管理
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "功能点管理")
@RestController
@RequestMapping("/function")
public class SysFunctionController {

    @Autowired
    private ISysFunctionService sysFunctionService;

    /**
     * 列表查询
     *
     * @return
     */
    @AutoLog(value = "功能点管理-分页列表查询")
    @ApiOperation(value = "功能点管理-分页列表查询", notes = "功能点管理-分页列表查询")
    @GetMapping(value = "/getPageList")
    public ResponseResult<?> getPageList(@RequestParam(name = "menuId") String menuId,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysFunction> page = new Page<>(pageNo, pageSize);
        IPage<SysFunctionVo> pageList = sysFunctionService.getPageList(page, menuId);
        return ResponseResult.success(pageList);
    }

    /**
     * 列表查询
     *
     * @return
     */
    @AutoLog(value = "功能点管理-全量列表查询")
    @ApiOperation(value = "功能点管理-全量列表查询", notes = "功能点管理-全量列表查询")
    @GetMapping(value = "/getList")
    public ResponseResult<?> getList(@RequestParam(name = "menuId") String menuId) {
        return sysFunctionService.getList(menuId);
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "功能点管理-添加", operateType = CommonConstant.OPERATE_TYPE_2)
    @ApiOperation(value = "功能点管理-添加", notes = "功能点管理-添加")
    @PostMapping(value = "/add")
    public ResponseResult<?> add(@RequestBody SysFunctionDto dto) {
        return sysFunctionService.add(dto);
    }

    /**
     * 编辑
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "功能点管理-编辑", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "功能点管理-编辑", notes = "功能点管理-编辑")
    @PostMapping(value = "/edit")
    public ResponseResult<?> edit(@RequestBody SysFunctionDto dto) {
        return sysFunctionService.edit(dto);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "功能点管理-通过id删除", operateType = CommonConstant.OPERATE_TYPE_4)
    @ApiOperation(value = "功能点管理-通过id删除", notes = "功能点管理-通过id删除")
    @GetMapping(value = "/delete")
    public ResponseResult<?> delete(@RequestParam(name = "id") String id) {
        return sysFunctionService.delete(id);
    }


}
