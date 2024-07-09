package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.domain.dto.SysRoleDto;
import com.lvxc.user.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户id查询角色
     *
     * @param userId
     * @return
     */
    List<SysRole> getRoleByUserId(@Param("userId") String userId);

    Page<SysRole> getPageList(@Param("page") Page<SysRole> page, @Param("dto") SysRoleDto dto);
}
