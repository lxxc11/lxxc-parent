package com.lvxc.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysRoleDto;
import com.lvxc.user.domain.vo.SysRolePageVo;
import com.lvxc.user.entity.SysRole;
import com.lvxc.user.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 用户中心-角色表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "角色管理")
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 分页列表查询
     *
     * @param keyWord
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "用户中心-角色表-分页列表查询")
    @ApiOperation(value = "用户中心-角色表-分页列表查询", notes = "用户中心-角色表-分页列表查询")
    @GetMapping(value = "/pageList")
    public ResponseResult<?> getPageList(String keyWord,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysRole> page = new Page<>(pageNo, pageSize);
        IPage<SysRolePageVo> pageList = sysRoleService.getPageList(page, keyWord);
        return ResponseResult.success(pageList);
    }

    /**
     * 列表查询
     *
     * @return
     */
    @AutoLog(value = "用户中心-角色表-全量列表查询")
    @ApiOperation(value = "用户中心-角色表-全量列表查询", notes = "用户中心-角色表-全量列表查询")
    @GetMapping(value = "/getList")
    public ResponseResult<?> getList() {
        List<SysRolePageVo> pageList = sysRoleService.getList();
        return ResponseResult.success(pageList);
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-角色表-添加", operateType = CommonConstant.OPERATE_TYPE_2)
    @ApiOperation(value = "用户中心-角色表-添加", notes = "用户中心-角色表-添加")
    @PostMapping(value = "/add")
    public ResponseResult<?> add(@RequestBody SysRoleDto dto) {
        return sysRoleService.add(dto);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户中心-角色表-通过id查询")
    @ApiOperation(value = "用户中心-角色表-通过id查询", notes = "用户中心-角色表-通过id查询")
    @GetMapping(value = "/queryById")
    public ResponseResult<?> queryById(@RequestParam(name = "id") String id) {
        return sysRoleService.queryById(id);
    }

    /**
     * 编辑
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-角色表-编辑", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "用户中心-角色表-编辑", notes = "用户中心-角色表-编辑")
    @PostMapping(value = "/edit")
    public ResponseResult<?> edit(@RequestBody SysRoleDto dto) {
        return sysRoleService.edit(dto);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户中心-角色表-通过id删除", operateType = CommonConstant.OPERATE_TYPE_4)
    @ApiOperation(value = "用户中心-角色表-通过id删除", notes = "用户中心-角色表-通过id删除")
    @GetMapping(value = "/delete")
    public ResponseResult<?> delete(@RequestParam(name = "id") String id) {
        return sysRoleService.delete(id);
    }


}
