package com.lvxc.user.controller;

import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysMenuDto;
import com.lvxc.user.domain.vo.SysMenuVo;
import com.lvxc.user.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 用户中心-菜单管理
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 列表查询
     *
     * @return
     */
    @AutoLog(value = "菜单管理-列表查询")
    @ApiOperation(value = "菜单管理-列表查询", notes = "菜单管理-列表查询")
    @GetMapping(value = "/getList")
    public ResponseResult<?> getList(@RequestParam(name = "platformId") String platformId) {
        List<SysMenuVo> list = sysMenuService.getList(platformId);
        return ResponseResult.success(list);
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "菜单管理-添加", operateType = CommonConstant.OPERATE_TYPE_2)
    @ApiOperation(value = "菜单管理-添加", notes = "菜单管理-添加")
    @PostMapping(value = "/add")
    public ResponseResult<?> add(@RequestBody SysMenuDto dto) {
        return sysMenuService.add(dto);
    }

    /**
     * 编辑
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "菜单管理-编辑", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "菜单管理-编辑", notes = "菜单管理-编辑")
    @PostMapping(value = "/edit")
    public ResponseResult<?> edit(@RequestBody SysMenuDto dto) {
        return sysMenuService.edit(dto);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "菜单管理-通过id删除", operateType = CommonConstant.OPERATE_TYPE_4)
    @ApiOperation(value = "菜单管理-通过id删除", notes = "菜单管理-通过id删除")
    @GetMapping(value = "/delete")
    public ResponseResult<?> delete(@RequestParam(name = "id") String id) {
        return sysMenuService.delete(id);
    }

    /**
     * 校验菜单
     *
     * @return
     */
    @AutoLog(value = "菜单管理-校验菜单")
    @ApiOperation(value = "菜单管理-校验菜单", notes = "菜单管理-校验菜单")
    @GetMapping(value = "/checkMenu")
    public ResponseResult<?> checkMenu(@RequestParam(name = "platformId") String platformId,
                                       @RequestParam(name = "menuName") String menuName) {
        if (StringUtils.isEmpty(menuName)) {
            return ResponseResult.success("校验通过！");
        }
        return sysMenuService.checkMenu(platformId, menuName);
    }

    /**
     * 获取当前用户菜单列表
     *
     * @return
     */
    @AutoLog(value = "菜单管理-获取当前用户菜单列表")
    @ApiOperation(value = "菜单管理-获取当前用户菜单列表", notes = "菜单管理-获取当前用户菜单列表")
    @GetMapping(value = "/getMenuList")
    public ResponseResult<?> getMenuList() {
        return sysMenuService.getMenuList();
    }

    /**
     * 根据平台id获取菜单信息
     *
     * @return
     */
    @AutoLog(value = "菜单管理-根据平台id获取菜单信息")
    @ApiOperation(value = "菜单管理-根据平台id获取菜单信息", notes = "菜单管理-根据平台id获取菜单信息")
    @GetMapping(value = "/getListByPlatformId")
    public ResponseResult<?> getListByPlatformId(@RequestParam(name = "platformId") String platformId) {
        List<SysMenuVo> list = sysMenuService.getListByPlatformId(platformId);
        return ResponseResult.success(list);
    }

}
