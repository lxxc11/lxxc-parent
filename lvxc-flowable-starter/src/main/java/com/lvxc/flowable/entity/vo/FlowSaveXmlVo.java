package com.lvxc.flowable.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author lvxc
 * @Date 2021/11/5 10:43
 **/
@Data
public class FlowSaveXmlVo implements Serializable {

  /**
   * 流程名称
   */
  @ApiModelProperty("流程名称")
  private String name;

  /**
   * 流程分类
   */
  @ApiModelProperty("流程分类")
  private String category;

  /**
   * xml 文件
   */
  @ApiModelProperty("xml 文件")
  private String xml;
}
