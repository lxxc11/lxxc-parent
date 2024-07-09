package com.lvxc.user.converter;

import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysMenuDto;
import com.lvxc.user.domain.vo.SysMenuVo;
import com.lvxc.user.entity.SysMenu;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = CommonConstant.MAP_STRUCT_SPRING)
public interface SysMenuConverter {

    List<SysMenuVo> sysMenuListDaoToVo(List<SysMenu> list);

    SysMenu sysMenuDtoToDao(SysMenuDto dto);

}
