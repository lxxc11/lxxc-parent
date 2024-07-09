package com.lvxc.user.service.impl;

import com.lvxc.user.domain.vo.LoginDataSetIndex;
import com.lvxc.user.entity.SysDataSetIndex;
import com.lvxc.user.mapper.flw.SysDataSetIndexMapper;
import com.lvxc.user.service.ISysDataSetIndexService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 数据集指标表
 * @Author: mengy
 * @Date: 2023-05-06
 * @Version: V1.0
 */
@Service
public class SysDataSetIndexServiceImpl extends ServiceImpl<SysDataSetIndexMapper, SysDataSetIndex> implements ISysDataSetIndexService {

    @Override
    public List<LoginDataSetIndex> getDataSetIndexByRoleIds(List<String> roleIds) {
        return baseMapper.getDataSetIndexByRoleIds(roleIds);
    }

}
