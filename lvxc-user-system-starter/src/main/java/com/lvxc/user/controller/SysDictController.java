package com.lvxc.user.controller;

import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.SysDictDto;
import com.lvxc.user.domain.vo.SysDictListVo;
import com.lvxc.user.domain.vo.SysDictPageVo;
import com.lvxc.user.entity.SysDict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @Description: 用户中心-字典表
 * @Author: mengy
 * @Date: 2023-05-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "用户中心-字典表")
@RestController
@RequestMapping("/dict")
public class SysDictController {

    @Autowired
    private ISysDictService sysDictService;

    /**
     * 分页列表查询
     *
     * @param platformId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "用户中心-字典表-分页列表查询")
    @ApiOperation(value = "用户中心-字典表-分页列表查询", notes = "用户中心-字典表-分页列表查询")
    @GetMapping(value = "/pageList")
    public ResponseResult<?> queryPageList(@RequestParam(name = "platformId") String platformId,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (StringUtils.isEmpty(platformId)) {
            return ResponseResult.error("平台id不能为空！");
        }
        Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
        IPage<SysDictPageVo> pageList = sysDictService.getPageList(page, platformId);
        return ResponseResult.success(pageList);
    }

    /**
     * 根据父级查询字典
     *
     * @param platformId
     * @param parentId
     * @param status
     * @return
     */
    @AutoLog(value = "用户中心-字典表-根据父级查询字典")
    @ApiOperation(value = "用户中心-字典表-根据父级查询字典", notes = "用户中心-字典表-分页列表查询")
    @GetMapping(value = "/getList")
    public ResponseResult<?> getList(@RequestParam(name = "platformId") String platformId,
                                     @RequestParam(name = "parentId") String parentId,
                                     @RequestParam(name = "status", required = false) Boolean status) {
        List<SysDictListVo> list = sysDictService.getList(platformId, parentId, status);
        return ResponseResult.success(list);
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-字典表-添加")
    @ApiOperation(value = "用户中心-字典表-添加", notes = "用户中心-字典表-添加")
    @PostMapping(value = "/add")
    public ResponseResult<?> add(@RequestBody SysDictDto dto) {
        return sysDictService.add(dto);
    }

    /**
     * 编辑
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-字典表-编辑")
    @ApiOperation(value = "用户中心-字典表-编辑", notes = "用户中心-字典表-编辑")
    @PostMapping(value = "/edit")
    public ResponseResult<?> edit(@RequestBody SysDictDto dto) {
        return sysDictService.edit(dto);
    }

    /**
     * 校验字典是否被使用
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户中心-字典表-校验字典是否被使用")
    @ApiOperation(value = "用户中心-字典表-校验字典是否被使用", notes = "用户中心-字典表-校验字典是否被使用")
    @GetMapping(value = "/check")
    public ResponseResult<?> check(@RequestParam(name = "id") String id) {
        return sysDictService.check(id);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户中心-字典表-通过id删除")
    @ApiOperation(value = "用户中心-字典表-通过id删除", notes = "用户中心-字典表-通过id删除")
    @GetMapping(value = "/delete")
    public ResponseResult<?> delete(@RequestParam(name = "id") String id) {
        return sysDictService.delete(id);
    }

    /**
     * 通过id删除
     *
     * @param platformId
     * @return
     */
    @AutoLog(value = "用户中心-字典表-根据平台id获取所有字典数据")
    @ApiOperation(value = "用户中心-字典表-根据平台id获取所有字典数据", notes = "用户中心-字典表-根据平台id获取所有字典数据")
    @GetMapping(value = "/getDictByPlatformId")
    public ResponseResult<?> getDictByPlatformId(@RequestParam(name = "platformId") String platformId) {
        return ResponseResult.success(sysDictService.getDictByPlatformId(platformId));
    }

}
