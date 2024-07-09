package com.lvxc.user.service;

import com.lvxc.user.domain.vo.LoginDataSetIndex;
import com.lvxc.user.entity.SysDataSetIndex;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 数据集指标表
 * @Author: mengy
 * @Date: 2023-05-06
 * @Version: V1.0
 */
public interface ISysDataSetIndexService extends IService<SysDataSetIndex> {

    /**
     * 根据角色查询数据指标
     *
     * @param roleIds
     * @return
     */
    List<LoginDataSetIndex> getDataSetIndexByRoleIds(List<String> roleIds);

}
