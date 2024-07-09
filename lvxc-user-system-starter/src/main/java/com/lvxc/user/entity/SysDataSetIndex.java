package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 数据集指标表
 * @Author: mengy
 * @Date: 2023-05-06
 * @Version: V1.0
 */
@Data
@TableName("sys_data_set_index")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_data_set_index对象", description = "数据集指标表")
public class SysDataSetIndex extends Entity {

    /**
     * 数据集id
     */
    @ApiModelProperty(value = "数据集id")
    private String dataSetId;
    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
    private String parentId;
    /**
     * 指标id
     */
    @ApiModelProperty(value = "指标id")
    private String indexId;
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
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

}
