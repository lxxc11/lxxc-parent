package com.lvxc.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.SysPlatformDto;
import com.lvxc.user.domain.vo.SysPlatformVo;
import com.lvxc.user.entity.SysPlatform;

import java.util.List;

public interface ISysPlatformService extends IService<SysPlatform> {

    /**
     * 平台管理-列表查询
     *
     * @return
     */
    List<SysPlatformVo> getList();

    /**
     * 平台管理-添加
     *
     * @param dto
     * @return
     */
    ResponseResult add(SysPlatformDto dto);

    /**
     * 平台管理-编辑
     *
     * @param dto
     * @return
     */
    ResponseResult edit(SysPlatformDto dto);

    /**
     * 平台管理-修改状态
     *
     * @param id
     * @return
     */
    ResponseResult updateState(String id);

}
