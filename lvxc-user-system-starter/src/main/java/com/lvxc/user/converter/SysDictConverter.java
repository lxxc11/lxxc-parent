package com.lvxc.user.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.SysDictDto;
import com.lvxc.user.domain.vo.DictVo;
import com.lvxc.user.domain.vo.SysDictListVo;
import com.lvxc.user.domain.vo.SysDictPageVo;
import com.lvxc.user.entity.SysDict;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = CommonConstant.MAP_STRUCT_SPRING)
public interface SysDictConverter {

    Page<SysDictPageVo> sysDictPageDaoToVo(Page<SysDict> pageList);

    SysDict sysDictDtoToDao(SysDictDto dto);

    List<SysDictListVo> sysDictListDaoToVo(List<SysDict> list);

    List<DictVo> sysDictListToDictList(List<SysDict> list);

}
