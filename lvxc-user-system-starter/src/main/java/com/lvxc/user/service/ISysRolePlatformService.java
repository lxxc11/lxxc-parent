package com.lvxc.user.service;

import com.lvxc.user.entity.SysRolePlatform;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 角色平台表
 * @Author: mengy
 * @Date: 2023-06-20
 * @Version: V1.0
 */
public interface ISysRolePlatformService extends IService<SysRolePlatform> {

    List<String> getPlatformIdsByUserId(String userId);

}
