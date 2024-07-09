package com.lvxc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.converter.SysPlatformConverter;
import com.lvxc.user.domain.dto.SysPlatformDto;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.domain.vo.SysPlatformVo;
import com.lvxc.user.entity.SysPlatform;
import com.lvxc.user.mapper.flw.SysPlatformMapper;
import com.lvxc.user.service.ISysPlatformService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysPlatformServiceImpl extends ServiceImpl<SysPlatformMapper, SysPlatform> implements ISysPlatformService {

    @Autowired
    private SysPlatformConverter sysPlatformConverter;

    @Override
    public List<SysPlatformVo> getList() {
//        LambdaQueryWrapper<SysPlatform> query = new LambdaQueryWrapper<>();
//        query.eq(SysPlatform::getDelFlag, false);
//        query.eq(SysPlatform::getSuperPower, false);
//        query.orderByAsc(SysPlatform::getSortOrder).orderByDesc(SysPlatform::getUpdateTime);
//        List<SysPlatform> list = list(query);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = loginUser.getSuperFlag()?"":loginUser.getId();
        List<SysPlatform> list = baseMapper.getList(userId);
        List<SysPlatformVo> sysPlatformVos = sysPlatformConverter.sysPlatformDaoToVo(list);
        return sysPlatformVos;
    }

    @Override
    @Transactional
    public ResponseResult add(SysPlatformDto dto) {
        //添加校验
        ResponseResult result = check(dto, true);
        if (!result.isSuccess()) {
            return result;
        }
        SysPlatform sysPlatform = sysPlatformConverter.sysPlatformDtoToDao(dto);
        sysPlatform.setSuperPower(false);
        save(sysPlatform);
        return ResponseResult.success("新增成功！");
    }

    @Override
    @Transactional
    public ResponseResult edit(SysPlatformDto dto) {
        //添加校验
        ResponseResult result = check(dto, false);
        if (!result.isSuccess()) {
            return result;
        }
        SysPlatform sysPlatform = getById(dto.getId());
        sysPlatform.setSystemName(dto.getSystemName());
        sysPlatform.setSystemEnName(dto.getSystemEnName());
        sysPlatform.setUrl(dto.getUrl());
        sysPlatform.setDomainName(dto.getDomainName());
        sysPlatform.setLogo(dto.getLogo());
        sysPlatform.setStatus(dto.getStatus());
        sysPlatform.setSortOrder(dto.getSortOrder());
        updateById(sysPlatform);
        return ResponseResult.success("修改成功！");
    }

    @Override
    @Transactional
    public ResponseResult updateState(String id) {
        SysPlatform sysPlatform = getById(id);
        if (sysPlatform == null) {
            return ResponseResult.error("平台不存在！");
        }
        sysPlatform.setStatus(!sysPlatform.getStatus());
        updateById(sysPlatform);
        return ResponseResult.success("修改成功！");
    }

    public ResponseResult check(SysPlatformDto dto, boolean isAdd) {
        if (StringUtils.isEmpty(dto.getSystemName())) {
            return ResponseResult.error("系统名称不能为空！");
        }
        if (StringUtils.isEmpty(dto.getSystemEnName())) {
            return ResponseResult.error("英文名称不能为空！");
        }
        if (StringUtils.isEmpty(dto.getUrl())) {
            return ResponseResult.error("系统URL不能为空！");
        }
        if (StringUtils.isEmpty(dto.getDomainName())) {
            return ResponseResult.error("系统域名不能为空！");
        }
        if (dto.getStatus() == null) {
            return ResponseResult.error("系统状态不能为空！");
        }

        SysPlatform platform = null;
        LambdaQueryWrapper<SysPlatform> query = new LambdaQueryWrapper<>();
        query.eq(SysPlatform::getDelFlag, false);
        query.eq(SysPlatform::getSystemName, dto.getSystemName());
        platform = getOne(query);
        if (isAdd) {
            if (platform != null) {
                return ResponseResult.error("平台已存在！");
            }
        } else {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseResult.error("id不能为空");
            }
            SysPlatform sysPlatform = getById(dto.getId());
            if (sysPlatform == null) {
                return ResponseResult.error("菜单不存在");
            } else {
                if (platform != null && !StringUtils.equals(platform.getSystemName(), sysPlatform.getSystemName())) {
                    return ResponseResult.error("菜单已存在！");
                }
            }
        }
        return ResponseResult.success("校验成功");
    }

}
