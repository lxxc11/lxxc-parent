package com.lvxc.user.domain.dto;

import com.lvxc.user.entity.SysLog;
import lombok.Data;

@Data
public class SysLogDto extends SysLog {

    private String beginTime ;

    private String endTime;
}
