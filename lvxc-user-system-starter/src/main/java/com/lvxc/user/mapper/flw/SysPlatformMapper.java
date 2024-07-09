package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvxc.user.entity.SysPlatform;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysPlatformMapper extends BaseMapper<SysPlatform> {
    List<SysPlatform> getList(String userId);
}
