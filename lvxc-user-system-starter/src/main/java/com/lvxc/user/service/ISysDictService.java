package com.lvxc.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.SysDictDto;
import com.lvxc.user.domain.vo.DictVo;
import com.lvxc.user.domain.vo.SysDictListVo;
import com.lvxc.user.domain.vo.SysDictPageVo;
import com.lvxc.user.entity.SysDict;

import java.util.List;

/**
 * @Description: 用户中心-字典表
 * @Author: mengy
 * @Date: 2023-05-04
 * @Version: V1.0
 */
public interface ISysDictService extends IService<SysDict> {

    /**
     * 字典表-分页列表查询
     *
     * @param page
     * @param platformId
     * @return
     */
    IPage<SysDictPageVo> getPageList(Page<SysDict> page, String platformId);

    /**
     * 字典表-根据父级查询字典
     *
     * @param platformId
     * @param parentId
     * @param status
     * @return
     */
    List<SysDictListVo> getList(String platformId, String parentId, Boolean status);

    /**
     * 字典表-添加
     *
     * @param dto
     * @return
     */
    ResponseResult add(SysDictDto dto);

    /**
     * 字典表-编辑
     *
     * @param dto
     * @return
     */
    ResponseResult edit(SysDictDto dto);

    /**
     * 字典表-校验字典是否被使用
     *
     * @param id
     * @return
     */
    ResponseResult check(String id);

    /**
     * 字典表-通过id删除
     *
     * @param id
     * @return
     */
    ResponseResult delete(String id);

    /**
     * 用户中心-字典表-根据平台id获取所有字典数据
     *
     * @param platformId
     * @return
     */
    List<DictVo> getDictByPlatformId(String platformId);

}
