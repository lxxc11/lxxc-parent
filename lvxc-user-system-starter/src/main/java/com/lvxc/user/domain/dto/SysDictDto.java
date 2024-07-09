package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysDictDto {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 平台id
     */
    @ApiModelProperty(value = "平台id")
    private String platformId;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private String parentId;
    /**
     * 字典key
     */
    @ApiModelProperty(value = "字典key")
    private String key;
    /**
     * 字典label
     */
    @ApiModelProperty(value = "字典label")
    private String label;
    /**
     * 字典value
     */
    @ApiModelProperty(value = "字典value")
    private String value;
    /**
     * 菜单状态（false停用 true启用）
     */
    @ApiModelProperty(value = "菜单状态（false停用 true启用）")
    private Boolean status;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

}
