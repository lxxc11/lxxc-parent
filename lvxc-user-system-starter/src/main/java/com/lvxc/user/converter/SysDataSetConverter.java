package com.lvxc.user.converter;

import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.IndexDto;
import com.lvxc.user.domain.dto.SysDataSetDto;
import com.lvxc.user.domain.vo.LoginDataSet;
import com.lvxc.user.domain.vo.SysDataSetIndexVo;
import com.lvxc.user.domain.vo.SysDataSetVo;
import com.lvxc.user.entity.SysDataSet;
import com.lvxc.user.entity.SysDataSetIndex;
import com.lvxc.user.entity.SysDict;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = CommonConstant.MAP_STRUCT_SPRING)
public interface SysDataSetConverter {

    SysDataSet sysDataSetDtoToDao(SysDataSetDto dto);

    List<SysDataSetVo> sysDataSetListDaoToVo(List<SysDataSet> list);

    List<LoginDataSet> sysDataSetDaoToLoginDataSet(List<SysDataSet> list);

    @Mappings({
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "delFlag", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "indexId", source = "id")
    })
    SysDataSetIndex sysDictToSysDataSet(SysDict sysDict);

    List<SysDataSetIndex> sysDictsToSysDataSets(List<SysDict> list);

    List<SysDataSetIndexVo> sysDataSetIndexDaoToVo(List<SysDataSetIndex> list);

    SysDataSetIndex sysDataSetIndexDtoToDao(IndexDto dao);

}
