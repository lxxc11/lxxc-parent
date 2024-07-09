package com.lvxc.user.service.impl;

import com.lvxc.user.entity.SysRolePlatform;
import com.lvxc.user.mapper.flw.SysRolePlatformMapper;
import com.lvxc.user.service.ISysRolePlatformService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 角色平台表
 * @Author: mengy
 * @Date: 2023-06-20
 * @Version: V1.0
 */
@Service
public class SysRolePlatformServiceImpl extends ServiceImpl<SysRolePlatformMapper, SysRolePlatform> implements ISysRolePlatformService {

    @Override
    public List<String> getPlatformIdsByUserId(String userId) {
        return baseMapper.getPlatformIdsByUserId(userId);
    }

}
