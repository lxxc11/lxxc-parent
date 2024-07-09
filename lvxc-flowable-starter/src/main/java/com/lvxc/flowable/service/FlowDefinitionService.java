package com.lvxc.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.flowable.entity.dto.FlowProcDefDto;
import com.lvxc.flowable.entity.vo.DeployFormVo;
import com.lvxc.web.common.base.ResponseResult;

import java.io.IOException;
import java.io.InputStream;

public interface FlowDefinitionService {

  void importFile(String name, String category, InputStream in);

  Page<FlowProcDefDto> list(String name, Integer pageNum, Integer pageSize);

  int insertSysDeployForm(DeployFormVo deployFormVo);

  void delete(String deployId);

  ResponseResult<String> readXml(String deployId) throws IOException;;

  void updateState(Integer state, String deployId);
}
