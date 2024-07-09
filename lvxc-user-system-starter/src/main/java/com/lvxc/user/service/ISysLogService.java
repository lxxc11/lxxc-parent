package com.lvxc.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.domain.dto.SysLogDto;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.entity.SysLog;

import javax.servlet.http.HttpServletResponse;

public interface ISysLogService extends IService<SysLog> {

    /**
     * 保存日志
     *
     * @param logContent
     * @param platformId
     * @param logType
     * @param operateType
     * @param method
     * @param loginUser
     * @param object
     * @param time
     */
    void addLog(String logContent, String platformId, Integer logType, Integer operateType,
                String method, LoginUser loginUser, Object object, long time);


    IPage<SysLog> pageList(Page<SysLog> page, SysLogDto dto);

    void batchDownLoad(HttpServletResponse response, SysLogDto dto) throws Exception;
}
