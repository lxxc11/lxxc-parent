package com.lvxc.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.flowable.entity.dto.FlowNextDto;
import com.lvxc.flowable.entity.dto.FlowTaskDto;
import com.lvxc.flowable.entity.dto.FlowViewerDto;
import com.lvxc.flowable.entity.vo.FlowTaskVo;
import com.lvxc.web.common.base.ResponseResult;
import org.flowable.bpmn.model.UserTask;

import java.util.List;
import java.util.Map;

public interface FlowTaskService {

  ResponseResult<Page<FlowTaskDto>> myProcess(Integer pageNum, Integer pageSize, String name);

  ResponseResult<String> startProcessInstanceById(String procDefId, Map<String, Object> variables);

  ResponseResult<Map<String, Object>> flowRecord(String procInsId, String deployId);

  ResponseResult<Page<FlowTaskDto>> todoList(Integer pageNum, Integer pageSize);

  ResponseResult<Page<FlowTaskDto>> finishedList(Integer pageNum, Integer pageSize);

  ResponseResult<Map<String, Object>> processVariables(String taskId);

  ResponseResult<List<FlowViewerDto>> getFlowViewer(String procInsId);

  ResponseResult<FlowNextDto> getNextFlowNode(FlowTaskVo flowTaskVo);

  ResponseResult complete(FlowTaskVo flowTaskVo);

  ResponseResult<List<UserTask>> findReturnTaskList(FlowTaskVo flowTaskVo);

  void taskReturn(FlowTaskVo flowTaskVo);

  void taskReject(FlowTaskVo flowTaskVo);

  void delegateTask(FlowTaskVo flowTaskVo);

  void assignTask(FlowTaskVo flowTaskVo);

  void claim(FlowTaskVo flowTaskVo);

  void unClaim(FlowTaskVo flowTaskVo);

  ResponseResult stopProcess(FlowTaskVo flowTaskVo);

  ResponseResult<String> deleteProcIns(String procInsId);
}
