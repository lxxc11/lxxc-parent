package com.lvxc.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.SysDepartDto;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.entity.SysDepart;

import java.util.List;

public interface ISysDepartService extends IService<SysDepart> {

    /**
     * 用户中心-部门表-树结构列表查询
     *
     * @param keyWord
     * @return
     */
    ResponseResult getList(String keyWord);

    /**
     * 用户中心-部门表-添加
     *
     * @param dto
     * @return
     */
    ResponseResult add(SysDepartDto dto);

    /**
     * 用户中心-部门表-编辑
     *
     * @param dto
     * @return
     */
    ResponseResult edit(SysDepartDto dto);

    /**
     * 用户中心-部门表-通过id删除
     *
     * @param id
     * @return
     */
    ResponseResult delete(String id);

    /**
     * 根据用户id查询部门
     *
     * @param userId
     * @param type
     * @return
     */
    List<SysDepart> getDepartByUserId(String userId, Integer type);

    /**
     * 用户中心-部门表-获取部门用户树形接口
     *
     * @return
     */
    ResponseResult getDepartUser();

    void setRegion(SysDepart sysDepart, LoginUser loginUser);

}
