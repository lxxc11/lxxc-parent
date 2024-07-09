package com.lvxc.user.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysFunctionDto;
import com.lvxc.user.domain.vo.SysFunctionVo;
import com.lvxc.user.entity.SysFunction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = CommonConstant.MAP_STRUCT_SPRING)
public interface SysFunctionConverter {

    Page<SysFunctionVo> sysFunctionPageDaoToVo(Page<SysFunction> list);

    SysFunction sysFunctionDtoToDao(SysFunctionDto dto);

    List<SysFunctionVo> sysFunctionListDaoToVo(List<SysFunction> list);

}
