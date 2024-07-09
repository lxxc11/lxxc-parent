package com.lvxc.user.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysPlatformVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称")
    private String systemName;

    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称")
    private String systemEnName;

    /**
     * url
     */
    @ApiModelProperty(value = "url")
    private String url;

    /**
     * 域名
     */
    @ApiModelProperty(value = "域名")
    private String domainName;

    /**
     * logo
     */
    @ApiModelProperty(value = "logo")
    private String logo;

    /**
     * 系统状态（false停用，true启用）
     */
    @ApiModelProperty(value = "系统状态（false停用，true启用）")
    private Boolean status;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

}
