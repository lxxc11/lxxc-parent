package com.lvxc.user.mapper.flw;

import com.lvxc.user.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 用户中心-字典表
 * @Author: mengy
 * @Date: 2023-05-04
 * @Version: V1.0
 */
@Mapper
@Repository
public interface SysDictMapper extends BaseMapper<SysDict> {

}
