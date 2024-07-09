package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.domain.dto.SysLogDto;
import com.lvxc.user.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysLogMapper extends BaseMapper<SysLog> {
    IPage<SysLog> pageList(Page<SysLog> page, @Param("dto") SysLogDto dto);

    List<SysLog> getList( @Param("dto") SysLogDto dto);
}
