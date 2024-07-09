package com.lvxc.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.common.utils.excel.EasyExcelUtils;
import com.lvxc.user.common.SpringContextUtils;
import com.lvxc.user.domain.dto.SysLogDto;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.entity.SysLog;
import com.lvxc.user.mapper.flw.SysLogMapper;
import com.lvxc.user.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public void addLog(String logContent, String platformId, Integer logType, Integer operateType,
                       String method, LoginUser loginUser, Object object, long time) {
        SysLog sysLog = new SysLog();
        //注解上的描述,操作日志内容
        sysLog.setPlatformId(platformId);
        sysLog.setLogType(logType);
        sysLog.setLogContent(logContent);
        sysLog.setOperateType(operateType);

        try {
            //获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        sysLog.setMethod(method);
        sysLog.setRequestParam(object != null ? JSONArray.fromObject(object).toString() : "[]");
        sysLog.setRequestType("POST");
        sysLog.setCostTime(time);

        //获取登录用户信息
        if (loginUser == null) {
            try {
                loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        if (loginUser != null) {
            sysLog.setUserName(loginUser.getUserName());
            sysLog.setRealName(loginUser.getRealName());
            sysLog.setCreateBy(loginUser.getId());
            sysLog.setUpdateBy(loginUser.getId());
        }
        save(sysLog);
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        return ip;
    }


    @Override
    public IPage<SysLog> pageList(Page<SysLog> page, SysLogDto dto) {
        return baseMapper.pageList(page,dto);
    }

    @Override
    public void batchDownLoad(HttpServletResponse response, SysLogDto dto) throws Exception{
        List<SysLog> sysLogs = baseMapper.getList(dto);
        for (int i = 0; i < sysLogs.size(); i++) {
            sysLogs.get(i).setSerialNumber(i + 1);
        }
        EasyExcelUtils.downloadExcelData(response, sysLogs, "日志导出");
    }
}
