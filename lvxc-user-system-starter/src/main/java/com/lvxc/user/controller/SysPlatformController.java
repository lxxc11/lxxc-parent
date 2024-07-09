package com.lvxc.user.controller;

import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysPlatformDto;
import com.lvxc.user.domain.vo.SysPlatformVo;
import com.lvxc.user.service.ISysPlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 用户中心-平台管理
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "平台管理")
@RestController
@RequestMapping("/platform")
public class SysPlatformController {

    @Autowired
    private ISysPlatformService sysPlatformService;

    /**
     * 列表查询
     *
     * @return
     */
    @AutoLog(value = "平台管理-列表查询")
    @ApiOperation(value = "平台管理-列表查询", notes = "平台管理-列表查询")
    @GetMapping(value = "/getList")
    public ResponseResult<?> getList() {
        List<SysPlatformVo> list = sysPlatformService.getList();
        return ResponseResult.success(list);
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "平台管理-添加", operateType = CommonConstant.OPERATE_TYPE_2)
    @ApiOperation(value = "平台管理-添加", notes = "平台管理-添加")
    @PostMapping(value = "/add")
    public ResponseResult<?> add(@RequestBody SysPlatformDto dto) {
        return sysPlatformService.add(dto);
    }

    /**
     * 编辑
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "平台管理-编辑", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "平台管理-编辑", notes = "平台管理-编辑")
    @PostMapping(value = "/edit")
    public ResponseResult<?> edit(@RequestBody SysPlatformDto dto) {
        return sysPlatformService.edit(dto);
    }

    /**
     * 修改状态
     *
     * @return
     */
    @AutoLog(value = "平台管理-修改状态", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "平台管理-修改状态", notes = "平台管理-修改状态")
    @GetMapping(value = "/updateState")
    public ResponseResult<?> updateState(@RequestParam(name = "id") String id) {
        return sysPlatformService.updateState(id);
    }

}
