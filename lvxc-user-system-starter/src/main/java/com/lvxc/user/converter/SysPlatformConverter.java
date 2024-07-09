package com.lvxc.user.converter;

import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysPlatformDto;
import com.lvxc.user.domain.vo.SysPlatformVo;
import com.lvxc.user.entity.SysPlatform;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = CommonConstant.MAP_STRUCT_SPRING)
public interface SysPlatformConverter {

    List<SysPlatformVo> sysPlatformDaoToVo(List<SysPlatform> list);

    SysPlatform sysPlatformDtoToDao(SysPlatformDto dto);



}
