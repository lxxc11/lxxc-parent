package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvxc.user.domain.vo.LoginFunction;
import com.lvxc.user.entity.SysFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysFunctionMapper extends BaseMapper<SysFunction> {

    List<LoginFunction> getFunctionListByRoleIds(@Param("list") List<String> roleIds);

}
