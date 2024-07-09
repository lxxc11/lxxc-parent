package com.lvxc.flowable.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author lvxc
 * @Date 2021/11/5 10:55
 **/
@Data
public class FlowProcDefDto implements Serializable {

  @ApiModelProperty("流程id")
  private String id;

  @ApiModelProperty("流程名称")
  private String name;

  @ApiModelProperty("流程key")
  private String key;

  @ApiModelProperty("流程分类")
  private String category;

  @ApiModelProperty("配置表单名称")
  private String formName;

  @ApiModelProperty("配置表单id")
  private Long formId;

  @ApiModelProperty("版本")
  private int version;

  @ApiModelProperty("部署ID")
  private String deploymentId;

  @ApiModelProperty("流程定义状态: 1:激活 , 2:中止")
  private int suspensionState;

  @ApiModelProperty("部署时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+08:00")
  private Date deploymentTime;
}
