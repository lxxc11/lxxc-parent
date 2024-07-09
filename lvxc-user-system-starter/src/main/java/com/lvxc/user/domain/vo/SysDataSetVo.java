package com.lvxc.user.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SysDataSetVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private String menuId;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    /**
     * 数据集名称
     */
    @ApiModelProperty(value = "数据集名称")
    private String dataName;
    /**
     * 数据集内容id
     */
    @ApiModelProperty(value = "数据集内容id")
    private String contentId;
    /**
     * 数据集内容
     */
    @ApiModelProperty(value = "数据集内容")
    private String content;
    /**
     * 指标列表
     */
    @ApiModelProperty(value = "指标列表")
    private List<SysDataSetIndexVo> indexList;

}
