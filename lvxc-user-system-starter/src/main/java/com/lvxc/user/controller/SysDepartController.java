package com.lvxc.user.controller;

import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysDepartDto;
import com.lvxc.user.service.ISysDepartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 用户中心-部门表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "部门管理")
@RestController
@RequestMapping("/depart")
public class SysDepartController {

    @Autowired
    private ISysDepartService sysDepartService;

    /**
     * 树结构列表查询
     *
     * @param keyWord
     * @return
     */
    @AutoLog(value = "用户中心-部门表-树结构列表查询")
    @ApiOperation(value = "用户中心-部门表-树结构列表查询", notes = "用户中心-部门表-树结构列表查询")
    @GetMapping(value = "/getList")
    public ResponseResult<?> getList(String keyWord) {
        return sysDepartService.getList(keyWord);
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-部门表-添加", operateType = CommonConstant.OPERATE_TYPE_2)
    @ApiOperation(value = "用户中心-部门表-添加", notes = "用户中心-部门表-添加")
    @PostMapping(value = "/add")
    public ResponseResult<?> add(@RequestBody SysDepartDto dto) {
        return sysDepartService.add(dto);
    }

    /**
     * 编辑
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-部门表-编辑", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "用户中心-部门表-编辑", notes = "用户中心-部门表-编辑")
    @PostMapping(value = "/edit")
    public ResponseResult<?> edit(@RequestBody SysDepartDto dto) {
        return sysDepartService.edit(dto);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户中心-部门表-通过id删除", operateType = CommonConstant.OPERATE_TYPE_4)
    @ApiOperation(value = "用户中心-部门表-通过id删除", notes = "用户中心-部门表-通过id删除")
    @GetMapping(value = "/delete")
    public ResponseResult<?> delete(@RequestParam(name = "id") String id) {
        return sysDepartService.delete(id);
    }

    /**
     * 获取部门用户树形接口
     *
     * @return
     */
    @AutoLog(value = "用户中心-部门表-获取部门用户树形接口")
    @ApiOperation(value = "用户中心-部门表-获取部门用户树形接口", notes = "用户中心-部门表-获取部门用户树形接口")
    @GetMapping(value = "/getDepartUser")
    public ResponseResult<?> getDepartUser() {
        return sysDepartService.getDepartUser();
    }


}
