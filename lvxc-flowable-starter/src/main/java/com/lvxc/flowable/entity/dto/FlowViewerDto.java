package com.lvxc.flowable.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FlowViewerDto implements Serializable {

  private String key;
  private boolean completed;
}
