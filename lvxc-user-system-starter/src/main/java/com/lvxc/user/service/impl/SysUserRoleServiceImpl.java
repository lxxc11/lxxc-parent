package com.lvxc.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.user.entity.SysUserRole;
import com.lvxc.user.mapper.flw.SysUserRoleMapper;
import com.lvxc.user.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
}
