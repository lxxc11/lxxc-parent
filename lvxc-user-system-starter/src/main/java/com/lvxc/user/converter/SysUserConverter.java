package com.lvxc.user.converter;

import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysUserDto;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.domain.vo.SysUserVo;
import com.lvxc.user.entity.SysUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = CommonConstant.MAP_STRUCT_SPRING)
public interface SysUserConverter {

    SysUser sysUserDtoToDao(SysUserDto dto);

    SysUserVo sysUserDaoToVo(SysUser sysUser);

    LoginUser sysToLogin(SysUser sysUser);

}
