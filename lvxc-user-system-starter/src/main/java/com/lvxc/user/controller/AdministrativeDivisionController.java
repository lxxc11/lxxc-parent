package com.lvxc.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.entity.AdministrativeDivision;
import com.lvxc.user.service.AdministrativeDivisionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 行政区划表 前端控制器
 * </p>
 *
 * @author lvxc
 * @since 2023-08-31
 */
@Api(tags = "[行政区划表]模块")
@RestController
@RequestMapping("/administrative/division")
public class AdministrativeDivisionController {

    @Autowired
    private AdministrativeDivisionService administrativeDivisionService;


    @ApiOperation(value = "行政区划表:插入", notes = "行政区划表:插入")
    @PostMapping(value = "/insert")
    public ResponseResult<Boolean> insertAdministrativeDivision(@RequestBody AdministrativeDivision administrativeDivision) {
        return administrativeDivisionService.save(administrativeDivision) ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "行政区划表:删除", notes = "行政区划表:删除")
    @PostMapping(value = "/delete/{id}")
    public ResponseResult<Boolean> deleteAdministrativeDivisionById(@PathVariable("id") Serializable id) {
        return administrativeDivisionService.removeById(id) ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "行政区划表:修改", notes = "行政区划表:修改")
    @PostMapping(value = "/update")
    public ResponseResult<Boolean> updateAdministrativeDivision(@RequestBody AdministrativeDivision administrativeDivision) {
        return administrativeDivisionService.updateById(administrativeDivision) ? ResponseResult.success() : ResponseResult.error();
    }

    @ApiOperation(value = "行政区划表:分页", notes = "行政区划表:分页")
    @PostMapping(value = "/page")
    public ResponseResult<Page<AdministrativeDivision>> pageAdministrativeDivision(@RequestBody AdministrativeDivision administrativeDivision) {
        QueryWrapper<AdministrativeDivision> administrativeDivisionQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(administrativeDivision.getDistrictCode())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.DISTRICT_CODE, administrativeDivision.getDistrictCode());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getDistrictName())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.DISTRICT_NAME, administrativeDivision.getDistrictName());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getProvince())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.PROVINCE, administrativeDivision.getProvince());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getCity())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.CITY, administrativeDivision.getCity());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getArea())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.AREA, administrativeDivision.getArea());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getUpperLevelDistrictCode())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.UPPER_LEVEL_DISTRICT_CODE, administrativeDivision.getUpperLevelDistrictCode());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getPhoneAreaCode())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.PHONE_AREA_CODE, administrativeDivision.getPhoneAreaCode());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getGovResidence())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.GOV_RESIDENCE, administrativeDivision.getGovResidence());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getGeoDivision())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.GEO_DIVISION, administrativeDivision.getGeoDivision());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getProvinceCode())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.PROVINCE_CODE, administrativeDivision.getProvinceCode());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getCityCode())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.CITY_CODE, administrativeDivision.getCityCode());
        }
        if (StringUtils.isNotBlank(administrativeDivision.getAreaCode())) {
            administrativeDivisionQueryWrapper.like(AdministrativeDivision.AREA_CODE, administrativeDivision.getAreaCode());
        }
        return ResponseResult.success(administrativeDivisionService
                .page(administrativeDivision.page()
                        , administrativeDivisionQueryWrapper));
    }

    @ApiOperation(value = "行政区划表:列表", notes = "行政区划表:列表")
    @PostMapping(value = "/list")
    public ResponseResult<List<AdministrativeDivision>> listAdministrativeDivision(@RequestBody AdministrativeDivision administrativeDivision) {
        return ResponseResult.success(administrativeDivisionService
                .list(new QueryWrapper<>(administrativeDivision)));
    }

    @ApiOperation(value = "行政区划表:通过id查询", notes = "行政区划表:通过id查询")
    @GetMapping(value = "/find/{id}")
    public ResponseResult<AdministrativeDivision> findAdministrativeDivisionById(@PathVariable("id") Serializable id) {
        return ResponseResult.success(administrativeDivisionService.getById(id));
    }
}
