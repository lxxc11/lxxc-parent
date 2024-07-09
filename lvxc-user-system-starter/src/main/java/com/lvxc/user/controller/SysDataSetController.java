package com.lvxc.user.controller;

import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.IndexDto;
import com.lvxc.user.domain.dto.SysDataSetDto;
import com.lvxc.user.domain.vo.SysDataSetVo;
import com.lvxc.user.service.ISysDataSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 用户中心-数据集管理
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "数据集管理")
@RestController
@RequestMapping("/data/set")
public class SysDataSetController {

    @Autowired
    private ISysDataSetService sysDataSetService;

    /**
     * 列表查询
     *
     * @return
     */
    @AutoLog(value = "数据集管理-列表查询")
    @ApiOperation(value = "数据集管理-列表查询", notes = "数据集管理-列表查询")
    @GetMapping(value = "/getList")
    public ResponseResult<?> getList(@RequestParam(name = "menuId") String menuId) {
        List<SysDataSetVo> list = sysDataSetService.getList(menuId);
        return ResponseResult.success(list);
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "数据集管理-添加", operateType = CommonConstant.OPERATE_TYPE_2)
    @ApiOperation(value = "数据集管理-添加", notes = "数据集管理-添加")
    @PostMapping(value = "/add")
    public ResponseResult<?> add(@RequestBody SysDataSetDto dto) {
        return sysDataSetService.add(dto);
    }

    /**
     * 编辑
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "数据集管理-编辑", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "数据集管理-编辑", notes = "数据集管理-编辑")
    @PostMapping(value = "/edit")
    public ResponseResult<?> edit(@RequestBody SysDataSetDto dto) {
        return sysDataSetService.edit(dto);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "数据集管理-通过id删除", operateType = CommonConstant.OPERATE_TYPE_4)
    @ApiOperation(value = "数据集管理-通过id删除", notes = "数据集管理-通过id删除")
    @GetMapping(value = "/delete")
    public ResponseResult<?> delete(@RequestParam(name = "id") String id) {
        return sysDataSetService.delete(id);
    }

    /**
     * 自定义指标新增
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "数据集管理-自定义指标新增", operateType = CommonConstant.OPERATE_TYPE_2)
    @ApiOperation(value = "数据集管理-自定义指标新增", notes = "数据集管理-自定义指标新增")
    @PostMapping(value = "/addIndex")
    public ResponseResult<?> addIndex(@RequestBody IndexDto dto) {
        return sysDataSetService.addIndex(dto);
    }

    /**
     * 自定义指标修改
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "数据集管理-自定义指标修改", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "数据集管理-自定义指标修改", notes = "数据集管理-自定义指标修改")
    @PostMapping(value = "/editIndex")
    public ResponseResult<?> editIndex(@RequestBody IndexDto dto) {
        return sysDataSetService.editIndex(dto);
    }

    /**
     * 指标通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "数据集管理-指标通过id删除", operateType = CommonConstant.OPERATE_TYPE_4)
    @ApiOperation(value = "数据集管理-指标通过id删除", notes = "数据集管理-指标通过id删除")
    @GetMapping(value = "/deleteIndex")
    public ResponseResult<?> deleteIndex(@RequestParam(name = "id") String id) {
        return sysDataSetService.deleteIndex(id);
    }

}
