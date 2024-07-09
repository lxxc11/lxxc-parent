package com.lvxc.user.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysRoleDto;
import com.lvxc.user.domain.vo.DateSetVo;
import com.lvxc.user.domain.vo.LoginRoleVo;
import com.lvxc.user.domain.vo.SysRolePageVo;
import com.lvxc.user.domain.vo.SysRoleVo;
import com.lvxc.user.entity.SysRole;
import com.lvxc.user.entity.SysRoleMenuData;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = CommonConstant.MAP_STRUCT_SPRING)
public interface SysRoleConverter {

    SysRole sysRoleDtoToDao(SysRoleDto dto);

    SysRoleVo sysRoleDaoToVo(SysRole role);

    Page<SysRolePageVo> sysRolePageDaoToVo(Page<SysRole> pageList);

    List<SysRolePageVo> sysRoleListDaoToVo(List<SysRole> list);

    List<LoginRoleVo> sysRoleDaoToLoginVo(List<SysRole> roles);

    List<DateSetVo> sysRoleMenuDataToDateSetVo(List<SysRoleMenuData> list);

}
