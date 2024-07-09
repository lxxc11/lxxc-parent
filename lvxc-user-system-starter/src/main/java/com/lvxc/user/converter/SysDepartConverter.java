package com.lvxc.user.converter;

import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysDepartDto;
import com.lvxc.user.domain.vo.PartTimeDepartVo;
import com.lvxc.user.domain.vo.SysDepartVo;
import com.lvxc.user.entity.SysDepart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = CommonConstant.MAP_STRUCT_SPRING)
public interface SysDepartConverter {

    List<SysDepartVo> SysDepartListDaoToVo(List<SysDepart> allList);

    SysDepart sysDepartDtoToDao(SysDepartDto dto);

    List<PartTimeDepartVo> sysDepartDaoToPartTimeDepartsVo(List<SysDepart> list);

}
