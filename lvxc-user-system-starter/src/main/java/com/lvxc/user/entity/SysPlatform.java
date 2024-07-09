package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 平台管理
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
@TableName("sys_platform")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_platform对象", description = "平台管理")
public class SysPlatform extends Entity {

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

    /**
     * 是否被超级管理员管理
     */
    @ApiModelProperty(value = "是否被超级管理员管理")
    private Boolean superPower;

}
