package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 角色平台表
 * @Author: mengy
 * @Date: 2023-06-20
 * @Version: V1.0
 */
@Data
@TableName("sys_role_platform")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_role_platform对象", description = "角色平台表")
public class SysRolePlatform extends Entity {

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private String roleId;
    /**
     * 平台id
     */
    @ApiModelProperty(value = "平台id")
    private String platformId;

}

