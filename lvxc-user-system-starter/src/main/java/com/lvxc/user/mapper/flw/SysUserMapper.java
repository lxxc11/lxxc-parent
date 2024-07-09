package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.domain.dto.SysUserPageDto;
import com.lvxc.user.domain.vo.SysUserPageVo;
import com.lvxc.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 用户中心-用户表-分页列表查询
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<SysUserPageVo> getPageList(@Param("page") Page<SysUserPageVo> page, @Param("dto") SysUserPageDto dto);

}
