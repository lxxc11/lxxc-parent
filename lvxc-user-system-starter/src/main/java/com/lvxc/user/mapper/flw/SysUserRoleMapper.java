package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvxc.user.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}
