package com.lvxc.user.mapper.flw;

import com.lvxc.user.entity.SysRolePlatform;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 角色平台表
 * @Author: mengy
 * @Date: 2023-06-20
 * @Version: V1.0
 */
@Mapper
@Repository
public interface SysRolePlatformMapper extends BaseMapper<SysRolePlatform> {

    List<String> getPlatformIdsByUserId(@Param("userId") String userId);

}
