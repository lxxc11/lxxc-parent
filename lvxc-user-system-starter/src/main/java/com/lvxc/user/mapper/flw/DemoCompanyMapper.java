package com.lvxc.user.mapper.flw;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.entity.DemoCompany;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 企业demo表 Mapper 接口
 * </p>
 *
 * @author lvxc
 * @since 2023-08-31
 */
public interface DemoCompanyMapper extends BaseMapper<DemoCompany> {

    IPage<DemoCompany> getPageList(@Param("page") Page<DemoCompany> page, @Param("dto")DemoCompany demoCompany);

    List<DemoCompany> getList(@Param("dto")DemoCompany demoCompanyDto);
}
