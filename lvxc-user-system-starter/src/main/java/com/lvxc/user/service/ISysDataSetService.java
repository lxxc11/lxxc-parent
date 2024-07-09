package com.lvxc.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.IndexDto;
import com.lvxc.user.domain.dto.SysDataSetDto;
import com.lvxc.user.domain.vo.LoginDataSet;
import com.lvxc.user.domain.vo.SysDataSetIndexVo;
import com.lvxc.user.domain.vo.SysDataSetVo;
import com.lvxc.user.entity.SysDataSet;

import java.util.List;
import java.util.Map;

public interface ISysDataSetService extends IService<SysDataSet> {

    /**
     * 数据集管理-列表查询
     *
     * @param menuId
     * @return
     */
    List<SysDataSetVo> getList(String menuId);

    /**
     * 数据集管理-添加
     *
     * @param dto
     * @return
     */
    ResponseResult add(SysDataSetDto dto);

    /**
     * 数据集管理-编辑
     *
     * @param dto
     * @return
     */
    ResponseResult edit(SysDataSetDto dto);

    /**
     * 数据集管理-自定义指标新增
     *
     * @param dto
     * @return
     */
    ResponseResult addIndex(IndexDto dto);

    /**
     * 数据集管理-自定义指标修改
     *
     * @param dto
     * @return
     */
    ResponseResult editIndex(IndexDto dto);

    /**
     * 数据集管理-指标通过id删除
     *
     * @param id
     * @return
     */
    ResponseResult deleteIndex(String id);

    /**
     * 数据集管理-通过id删除
     *
     * @param id
     * @return
     */
    ResponseResult delete(String id);

    /**
     * 获取指标树形结构
     *
     * @param allMap
     * @param treeMenuList
     * @param isDict
     */
    void getTreeIndexList(Map<String, List<SysDataSetIndexVo>> allMap, List<SysDataSetIndexVo> treeMenuList, boolean isDict);

    /**
     * 根据角色查询数据集
     *
     * @param roleIds
     * @return
     */
    List<LoginDataSet> getDataSetByRoleIds(List<String> roleIds);

}
