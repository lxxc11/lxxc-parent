package com.lvxc.user.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class LoginDataSet {

    /**
     * 数据集id
     */
    @ApiModelProperty(value = "数据集id")
    private String id;
    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private String menuId;
    /**
     * 数据集名称
     */
    @ApiModelProperty(value = "数据集名称")
    private String dataName;
    /**
     * 数据集id
     */
    @ApiModelProperty(value = "对应字典id")
    private String contentId;
    /**
     * 数据集内容
     */
    @ApiModelProperty(value = "数据集内容")
    private String content;
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
     * 指标
     */
    @ApiModelProperty(value = "指标")
    private List<LoginDataSetIndex> indexList;

}
