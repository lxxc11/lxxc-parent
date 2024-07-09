package com.lvxc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.converter.SysFunctionConverter;
import com.lvxc.user.domain.dto.SysFunctionDto;
import com.lvxc.user.domain.vo.LoginFunction;
import com.lvxc.user.domain.vo.SysFunctionVo;
import com.lvxc.user.entity.SysFunction;
import com.lvxc.user.mapper.flw.SysFunctionMapper;
import com.lvxc.user.mapper.flw.SysRoleMenuFunctionMapper;
import com.lvxc.user.service.ISysFunctionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysFunctionServiceImpl extends ServiceImpl<SysFunctionMapper, SysFunction> implements ISysFunctionService {

    @Autowired
    private SysFunctionConverter sysFunctionConverter;

    @Autowired
    private SysRoleMenuFunctionMapper sysRoleMenuFunctionMapper;

    @Override
    public IPage<SysFunctionVo> getPageList(Page<SysFunction> page, String menuId) {
        LambdaQueryWrapper<SysFunction> query = new LambdaQueryWrapper<>();
        query.eq(SysFunction::getDelFlag, false);
        query.eq(SysFunction::getMenuId, menuId);
        query.orderByDesc(SysFunction::getUpdateTime);
        Page<SysFunction> pageList = page(page, query);
        Page<SysFunctionVo> list = sysFunctionConverter.sysFunctionPageDaoToVo(pageList);
        return list;
    }

    @Override
    public ResponseResult getList(String menuId) {
        LambdaQueryWrapper<SysFunction> query = new LambdaQueryWrapper<>();
        query.eq(SysFunction::getDelFlag, false);
        query.eq(SysFunction::getStatus, true);
        query.eq(SysFunction::getMenuId, menuId);
        query.orderByDesc(SysFunction::getUpdateTime);
        List<SysFunction> list = list(query);
        List<SysFunctionVo> functionList = sysFunctionConverter.sysFunctionListDaoToVo(list);
        return ResponseResult.success(functionList);
    }

    @Override
    @Transactional
    public ResponseResult add(SysFunctionDto dto) {
        // 添加校验
        ResponseResult result = check(dto, true);
        if (!result.isSuccess()) {
            return result;
        }
        SysFunction sysFunction = sysFunctionConverter.sysFunctionDtoToDao(dto);
        save(sysFunction);
        return ResponseResult.success("新增成功！");
    }

    @Override
    @Transactional
    public ResponseResult edit(SysFunctionDto dto) {
        // 修改校验
        ResponseResult result = check(dto, false);
        if (!result.isSuccess()) {
            return result;
        }
        SysFunction sysFunction = getById(dto.getId());
        sysFunction.setFunctionName(dto.getFunctionName());
        sysFunction.setFunctionTag(dto.getFunctionTag());
        sysFunction.setUrl(dto.getUrl());
        sysFunction.setStatus(dto.getStatus());
        sysFunction.setIcon(dto.getIcon());
        updateById(sysFunction);
        return ResponseResult.success("修改成功！");
    }

    @Override
    @Transactional
    public ResponseResult delete(String id) {
        SysFunction sysFunction = getById(id);
        if (sysFunction == null) {
            return ResponseResult.error("功能点不存在！");
        }
        sysFunction.setDelFlag(true);
        updateById(sysFunction);
        //删除与角色关联的功能点记录
        Map<String,Object> map = new HashMap<>();
        map.put("function_id",id);
        map.put("menu_id",sysFunction.getMenuId());
        sysRoleMenuFunctionMapper.deleteByMap(map);
        return ResponseResult.success("删除成功！");
    }

    @Override
    public List<LoginFunction> getFunctionListByRoleIds(List<String> roleIds) {
        return baseMapper.getFunctionListByRoleIds(roleIds);
    }

    public ResponseResult check(SysFunctionDto dto, boolean isAdd) {
        if (StringUtils.isEmpty(dto.getMenuId())) {
            return ResponseResult.error("菜单id不能为空！");
        }
        if (StringUtils.isEmpty(dto.getFunctionName())) {
            return ResponseResult.error("功能点名称不能为空！");
        }
        if (dto.getStatus() == null) {
            return ResponseResult.error("功能点状态不能为空！");
        }


        SysFunction function = null;
        LambdaQueryWrapper<SysFunction> query = new LambdaQueryWrapper<>();
        query.eq(SysFunction::getDelFlag, false);
        query.eq(SysFunction::getMenuId, dto.getMenuId());
        query.eq(SysFunction::getFunctionName, dto.getFunctionName());
        function = getOne(query);
        if (isAdd) {
            if (function != null) {
                return ResponseResult.error("功能点已存在！");
            }
        } else {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseResult.error("id不能为空");
            }
            SysFunction sysFunction = getById(dto.getId());
            if (sysFunction == null) {
                return ResponseResult.error("功能点不存在");
            } else {
                if (function != null && !StringUtils.equals(function.getFunctionName(), sysFunction.getFunctionName())) {
                    return ResponseResult.error("功能点已存在！");
                }
            }
        }
        return ResponseResult.success("校验成功");
    }


}
