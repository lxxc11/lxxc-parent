package com.lvxc.flowable.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author lvxc
 * @Date 2021/11/5 15:00
 **/
@Data
public class DeployFormVo implements Serializable {

  private Long id;

  @ApiModelProperty("表单id")
  private Long formId;

  @ApiModelProperty("流程id")
  private String deployId;
}
