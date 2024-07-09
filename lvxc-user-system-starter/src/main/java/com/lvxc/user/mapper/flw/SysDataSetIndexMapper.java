package com.lvxc.user.mapper.flw;

import com.lvxc.user.domain.vo.LoginDataSetIndex;
import com.lvxc.user.entity.SysDataSetIndex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 数据集指标表
 * @Author: mengy
 * @Date: 2023-05-06
 * @Version: V1.0
 */
@Mapper
@Repository
public interface SysDataSetIndexMapper extends BaseMapper<SysDataSetIndex> {

    List<LoginDataSetIndex> getDataSetIndexByRoleIds(@Param("list") List<String> roleIds);

}
