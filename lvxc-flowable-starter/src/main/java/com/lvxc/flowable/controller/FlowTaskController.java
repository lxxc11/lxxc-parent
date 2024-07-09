package com.lvxc.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.flowable.entity.dto.FlowNextDto;
import com.lvxc.flowable.entity.dto.FlowTaskDto;
import com.lvxc.flowable.entity.dto.FlowViewerDto;
import com.lvxc.flowable.entity.vo.FlowTaskVo;
import com.lvxc.flowable.service.FlowTaskService;
import com.lvxc.web.common.base.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@Api(tags = "工作流流程任务管理")
@RestController
@RequestMapping("/flowable/task")
public class FlowTaskController {

    @Autowired
    private FlowTaskService flowTaskService;

    @ApiOperation(value = "根据流程定义id启动流程实例")
    @PostMapping("/start/{procDefId}")
    public ResponseResult<String> start(@ApiParam(value = "流程定义id") @PathVariable(value = "procDefId") String procDefId, @ApiParam(value = "变量集合,json对象") @RequestBody Map<String, Object> variables) {
        return flowTaskService.startProcessInstanceById(procDefId, variables);

    }

    @ApiOperation(value = "我发起的流程")
    @GetMapping(value = "/myProcess")
    public ResponseResult<Page<FlowTaskDto>> myProcess(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum, @ApiParam(value = "流程名称", required = false) @RequestParam String name, @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize) {
        return flowTaskService.myProcess(pageNum, pageSize, name);
    }

    @ApiOperation(value = "流程历史流转记录", response = FlowTaskDto.class)
    @GetMapping(value = "/flowRecord")
    public ResponseResult<Map<String, Object>> flowRecord(String procInsId, String deployId) {
        return flowTaskService.flowRecord(procInsId, deployId);
    }

    @ApiOperation(value = "获取待办列表")
    @GetMapping(value = "/todoList")
    public ResponseResult<Page<FlowTaskDto>> todoList(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum, @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize) {
        return flowTaskService.todoList(pageNum, pageSize);
    }

    @ApiOperation(value = "获取已办任务")
    @GetMapping(value = "/finishedList")
    public ResponseResult<Page<FlowTaskDto>> finishedList(@ApiParam(value = "当前页码", required = true) @RequestParam Integer pageNum, @ApiParam(value = "每页条数", required = true) @RequestParam Integer pageSize) {
        return flowTaskService.finishedList(pageNum, pageSize);
    }

    @ApiOperation(value = "获取流程变量", response = FlowTaskDto.class)
    @GetMapping(value = "/processVariables/{taskId}")
    public ResponseResult<Map<String, Object>> processVariables(@ApiParam(value = "流程任务Id") @PathVariable(value = "taskId") String taskId) {
        return flowTaskService.processVariables(taskId);
    }

    /**
     * 生成流程图
     *
     * @param procInsId 任务ID
     */
    @ApiOperation(value = "各节点完成状态")
    @GetMapping("/flowViewer/{procInsId}")
    public ResponseResult<List<FlowViewerDto>> getFlowViewer(@PathVariable("procInsId") String procInsId) {
        return flowTaskService.getFlowViewer(procInsId);
    }

    @ApiOperation(value = "获取下一节点")
    @PostMapping(value = "/nextFlowNode")
    public ResponseResult<FlowNextDto> getNextFlowNode(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.getNextFlowNode(flowTaskVo);
    }

    @ApiOperation(value = "审批任务")
    @PostMapping(value = "/complete")
    public ResponseResult complete(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.complete(flowTaskVo);
    }

    @ApiOperation(value = "获取所有可回退的节点")
    @PostMapping(value = "/returnList")
    public ResponseResult<List<UserTask>> findReturnTaskList(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.findReturnTaskList(flowTaskVo);
    }

    @ApiOperation(value = "退回任务")
    @PostMapping(value = "/return")
    public ResponseResult taskReturn(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.taskReturn(flowTaskVo);
        return ResponseResult.success();
    }

    @ApiOperation(value = "驳回任务")
    @PostMapping(value = "/reject")
    public ResponseResult taskReject(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.taskReject(flowTaskVo);
        return ResponseResult.success();
    }

    @ApiOperation(value = "委派任务")
    @PostMapping(value = "/delegate")
    public ResponseResult delegate(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.delegateTask(flowTaskVo);
        return ResponseResult.success();
    }

    @ApiOperation(value = "转办任务")
    @PostMapping(value = "/assign")
    public ResponseResult assign(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.assignTask(flowTaskVo);
        return ResponseResult.success();
    }

    @ApiOperation(value = "认领/签收任务")
    @PostMapping(value = "/claim")
    public ResponseResult claim(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.claim(flowTaskVo);
        return ResponseResult.success();
    }

    @ApiOperation(value = "取消认领/签收任务")
    @PostMapping(value = "/unClaim")
    public ResponseResult unClaim(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.unClaim(flowTaskVo);
        return ResponseResult.success();
    }

    @ApiOperation(value = "删除流程实例")
    @DeleteMapping(value = "/delete")
    public ResponseResult<String> delete(@ApiParam(value = "流程实例ID", required = true) @RequestParam String procInsId) {
        ResponseResult<String> result = flowTaskService.deleteProcIns(procInsId);
        return result;
    }

    @ApiOperation(value = "取消申请", response = FlowTaskDto.class)
    @PostMapping(value = "/stopProcess")
    public ResponseResult stopProcess(@RequestBody FlowTaskVo flowTaskVo) {
        return flowTaskService.stopProcess(flowTaskVo);
    }


}
