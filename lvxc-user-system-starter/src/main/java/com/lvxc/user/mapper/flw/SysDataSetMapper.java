package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvxc.user.domain.vo.LoginDataSet;
import com.lvxc.user.entity.SysDataSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysDataSetMapper extends BaseMapper<SysDataSet> {

    List<LoginDataSet> getDataSetByRoleIds(@Param("list") List<String> roleIds);

}
