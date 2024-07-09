package com.lvxc.doc.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "数据库表基本信息")
public class DsTableVO {
    @ApiModelProperty("中文表名")
    private String name1;
    @ApiModelProperty("英文表名")
    private String name2;
    @ApiModelProperty("属性")
    private String name3;
    @ApiModelProperty("数据类型")
    private String name4;
    @ApiModelProperty("长度")
    private String name5;
    @ApiModelProperty("主键约束")
    private String name6;
    @ApiModelProperty("是否非空")
    private String name7;
    @ApiModelProperty("注释")
    private String name8;
}
