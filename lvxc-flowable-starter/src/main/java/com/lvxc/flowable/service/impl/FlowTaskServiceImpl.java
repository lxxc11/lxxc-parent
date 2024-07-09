package com.lvxc.flowable.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lvxc.flowable.common.constant.FlowCommentEnum;
import com.lvxc.flowable.common.constant.ProcessConstants;
import com.lvxc.flowable.common.util.FindNextNodeUtil;
import com.lvxc.flowable.common.util.FlowableUtils;
import com.lvxc.flowable.entity.dto.FlowCommentDto;
import com.lvxc.flowable.entity.dto.FlowNextDto;
import com.lvxc.flowable.entity.dto.FlowTaskDto;
import com.lvxc.flowable.entity.dto.FlowViewerDto;
import com.lvxc.flowable.entity.vo.FlowTaskVo;
import com.lvxc.flowable.factory.FlowServiceFactory;
import com.lvxc.flowable.service.FlowTaskService;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.entity.SysUser;
import com.lvxc.user.service.ISysUserService;
import com.lvxc.web.common.base.HsServerException;
import com.lvxc.web.common.base.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description
 * @Author lvxc
 * @Date 2021/11/9 13:43
 **/
@Service
@Slf4j
public class FlowTaskServiceImpl extends FlowServiceFactory implements FlowTaskService {

    @Autowired
    protected ISysUserService sysUserService;

//    @Autowired
//    protected FormMapper formMapper;

    @Override
    public ResponseResult<String> startProcessInstanceById(String procDefId, Map<String, Object> variables) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).latestVersion().singleResult();
            if (Objects.nonNull(processDefinition) && processDefinition.isSuspended()) {
                return ResponseResult.error(-1, "流程已被挂起,请先激活流程");
            }
            // 设置流程发起人Id到流程中
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            identityService.setAuthenticatedUserId(loginUser.getId());
            variables.put(ProcessConstants.PROCESS_INITIATOR, "");
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables);
            // 给第一步申请人节点设置任务执行人和意见 todo:第一个节点不设置为申请人节点有点问题？
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
            if (Objects.nonNull(task)) {
                taskService.addComment(task.getId(), processInstance.getProcessInstanceId(), FlowCommentEnum.NORMAL.getType(), loginUser.getRealName() + "发起流程申请");
                taskService.complete(task.getId(), variables);
            }
            return ResponseResult.success("流程启动成功");
        } catch (Exception e) {
            log.error("流程启动错误", e);
            return ResponseResult.error(-1, "流程启动错误");
        }
    }

    @Override
    public ResponseResult<Page<FlowTaskDto>> myProcess(Integer pageNum, Integer pageSize, String name) {
        Page<FlowTaskDto> page = new Page<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        HistoricProcessInstanceQuery hiq = historyService.createHistoricProcessInstanceQuery().startedBy(loginUser.getId()).notDeleted();
        if (StringUtils.isNotBlank(name)) {
            hiq.processDefinitionName(name);
        }
        hiq.orderByProcessInstanceStartTime().desc();
        List<HistoricProcessInstance> historicProcessInstances = hiq.listPage(pageNum - 1, pageSize);
        page.setTotal(hiq.count());
        List<FlowTaskDto> flowList = new ArrayList<>();
        for (HistoricProcessInstance hisIns : historicProcessInstances) {
            FlowTaskDto flowTask = new FlowTaskDto();
            flowTask.setCreateTime(hisIns.getStartTime());
            flowTask.setFinishTime(hisIns.getEndTime());
            flowTask.setProcInsId(hisIns.getId());

            // 计算耗时
            if (Objects.nonNull(hisIns.getEndTime())) {
                long time = hisIns.getEndTime().getTime() - hisIns.getStartTime().getTime();
                flowTask.setDuration(getDate(time));
            } else {
                long time = System.currentTimeMillis() - hisIns.getStartTime().getTime();
                flowTask.setDuration(getDate(time));
            }
            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(hisIns.getProcessDefinitionId()).singleResult();
            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setCategory(pd.getCategory());

            // 当前所处流程
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(hisIns.getId()).list();
            if (CollectionUtils.isNotEmpty(taskList)) {
                flowTask.setTaskId(taskList.get(0).getId());
            } else {
                List<HistoricTaskInstance> historicTaskInstance = historyService.createHistoricTaskInstanceQuery().processInstanceId(hisIns.getId()).orderByHistoricTaskInstanceEndTime().desc().list();
                flowTask.setTaskId(CollectionUtils.isNotEmpty(historicTaskInstance) ? historicTaskInstance.get(0).getId() : "");
            }
            flowList.add(flowTask);
        }
        page.setRecords(flowList);
        return ResponseResult.success(page);
    }

    @Override
    public ResponseResult<Map<String, Object>> flowRecord(String procInsId, String deployId) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(procInsId)) {
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInsId).orderByHistoricActivityInstanceStartTime().desc().list();
            List<FlowTaskDto> hisFlowList = new ArrayList<>();
            // 获取意见评论内容
            List<Comment> commentList = taskService.getProcessInstanceComments(procInsId);
            for (HistoricActivityInstance histIns : list) {
                if (StringUtils.isNotBlank(histIns.getTaskId())) {
                    FlowTaskDto flowTask = new FlowTaskDto();
                    flowTask.setTaskId(histIns.getTaskId());
                    flowTask.setTaskName(histIns.getActivityName());
                    flowTask.setCreateTime(histIns.getStartTime());
                    flowTask.setFinishTime(histIns.getEndTime());
                    if (StringUtils.isNotBlank(histIns.getAssignee())) {
                        SysUser sysUser = sysUserService.getById(Long.parseLong(histIns.getAssignee()));
                        flowTask.setAssigneeId(sysUser.getId());
                        flowTask.setAssigneeName(sysUser.getRealName());
//            flowTask.setDeptName(sysUser.getDept().getDeptName());
                    }
                    // 展示审批人员
                    List<HistoricIdentityLink> linksForTask = historyService.getHistoricIdentityLinksForTask(histIns.getTaskId());
                    StringBuilder stringBuilder = new StringBuilder();
                    for (HistoricIdentityLink identityLink : linksForTask) {
                        if ("candidate".equals(identityLink.getType())) {
                            if (StringUtils.isNotBlank(identityLink.getUserId())) {
                                SysUser sysUser = sysUserService.getById(Long.parseLong(identityLink.getUserId()));
                                stringBuilder.append(sysUser.getRealName()).append(",");
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(stringBuilder)) {
                        flowTask.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                    }

                    flowTask.setDuration(histIns.getDurationInMillis() == null || histIns.getDurationInMillis() == 0 ? null : getDate(histIns.getDurationInMillis()));
                    for (Comment comment : commentList) {
                        if (histIns.getTaskId().equals(comment.getTaskId())) {
                            flowTask.setComment(FlowCommentDto.builder().type(comment.getType()).comment(comment.getFullMessage()).build());
                            break;
                        }
                    }
                    hisFlowList.add(flowTask);
                }
            }
            map.put("flowList", hisFlowList);
        }
        // 第一次申请获取初始化表单
//    if (StringUtils.isNotBlank(deployId)) {
//      Form sysForm = formMapper.selectSysDeployFormByDeployId(deployId);
//      if (Objects.isNull(sysForm)) {
//        return ResponseResult.error(-1,"请先配置流程表单");
//      }
//      map.put("formData", sysForm.getData());
//    }
        return ResponseResult.success(map);
    }


    @Override
    public ResponseResult<Page<FlowTaskDto>> todoList(Integer pageNum, Integer pageSize) {
        Page<FlowTaskDto> page = new Page<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        TaskQuery taskQuery = taskService.createTaskQuery().active().includeProcessVariables().taskAssignee(loginUser.getId()).orderByTaskCreateTime().desc();
        page.setTotal(taskQuery.count());
        List<Task> taskList = taskQuery.listPage(pageNum - 1, pageSize);
        List<FlowTaskDto> flowList = new ArrayList<>();
        for (Task task : taskList) {
            FlowTaskDto flowTask = new FlowTaskDto();
            // 当前流程信息
            flowTask.setTaskId(task.getId());
            flowTask.setTaskDefKey(task.getTaskDefinitionKey());
            flowTask.setCreateTime(task.getCreateTime());
            flowTask.setProcDefId(task.getProcessDefinitionId());
            flowTask.setTaskName(task.getName());
            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(task.getProcessInstanceId());
            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            SysUser startUser = sysUserService.getById(Long.parseLong(historicProcessInstance.getStartUserId()));
//            SysUser startUser = sysUserService.selectUserById(Long.parseLong(task.getAssignee()));
            flowTask.setStartUserId(startUser.getRealName());
            flowTask.setStartUserName(startUser.getRealName());
//      flowTask.setStartDeptName(startUser.getDept().getDeptName());
            flowList.add(flowTask);
        }

        page.setRecords(flowList);
        return ResponseResult.success(page);
    }

    @Override
    public ResponseResult<Page<FlowTaskDto>> finishedList(Integer pageNum, Integer pageSize) {
        Page<FlowTaskDto> page = new Page<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery().includeProcessVariables().finished().taskAssignee(loginUser.getId()).orderByHistoricTaskInstanceEndTime().desc();
        List<HistoricTaskInstance> historicTaskInstanceList = taskInstanceQuery.listPage(pageNum - 1, pageSize);
        List<FlowTaskDto> hisTaskList = Lists.newArrayList();
        for (HistoricTaskInstance histTask : historicTaskInstanceList) {
            FlowTaskDto flowTask = new FlowTaskDto();
            // 当前流程信息
            flowTask.setTaskId(histTask.getId());
            // 审批人员信息
            flowTask.setCreateTime(histTask.getCreateTime());
            flowTask.setFinishTime(histTask.getEndTime());
            flowTask.setDuration(getDate(histTask.getDurationInMillis()));
            flowTask.setProcDefId(histTask.getProcessDefinitionId());
            flowTask.setTaskDefKey(histTask.getTaskDefinitionKey());
            flowTask.setTaskName(histTask.getName());

            // 流程定义信息
            if (StringUtils.isNotBlank(histTask.getProcessDefinitionId())) {
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(histTask.getProcessDefinitionId()).singleResult();
                flowTask.setDeployId(pd.getDeploymentId());
                flowTask.setProcDefName(pd.getName());
                flowTask.setProcDefVersion(pd.getVersion());
            }
            flowTask.setProcInsId(histTask.getProcessInstanceId());
            flowTask.setHisProcInsId(histTask.getProcessInstanceId());

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(histTask.getProcessInstanceId()).singleResult();
            SysUser startUser = sysUserService.getById(Long.parseLong(historicProcessInstance.getStartUserId()));
            flowTask.setStartUserId(startUser.getRealName());
            flowTask.setStartUserName(startUser.getRealName());
//      flowTask.setStartDeptName(startUser.getDept().getDeptName());
            hisTaskList.add(flowTask);
        }
        page.setTotal(hisTaskList.size());
        page.setRecords(hisTaskList);
//        Map<String, Object> result = new HashMap<>();
//        result.put("result",page);
//        result.put("finished",true);
        return ResponseResult.success(page);
    }

    @Override
    public ResponseResult<Map<String, Object>> processVariables(String taskId) {
        // 流程变量
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().includeProcessVariables().finished().taskId(taskId).singleResult();
        if (Objects.nonNull(historicTaskInstance)) {
            return ResponseResult.success(historicTaskInstance.getProcessVariables());
        } else {
            Map<String, Object> variables = taskService.getVariables(taskId);
            return ResponseResult.success(variables);
        }
    }

    @Override
    public ResponseResult<List<FlowViewerDto>> getFlowViewer(String procInsId) {
        List<FlowViewerDto> flowViewerList = new ArrayList<>();
        FlowViewerDto flowViewerDto;
        // 获得活动的节点
        List<HistoricActivityInstance> hisActIns = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInsId).orderByHistoricActivityInstanceStartTime().asc().list();
        for (HistoricActivityInstance activityInstance : hisActIns) {
            if (!"sequenceFlow".equals(activityInstance.getActivityType())) {
                flowViewerDto = new FlowViewerDto();
                flowViewerDto.setKey(activityInstance.getActivityId());
                flowViewerDto.setCompleted(!Objects.isNull(activityInstance.getEndTime()));
                flowViewerList.add(flowViewerDto);
            }
        }
        return ResponseResult.success(flowViewerList);
    }

    @Override
    public ResponseResult<FlowNextDto> getNextFlowNode(FlowTaskVo flowTaskVo) {
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        FlowNextDto flowNextDto = new FlowNextDto();
        if (Objects.nonNull(task)) {
            List<UserTask> nextUserTask = FindNextNodeUtil.getNextUserTasks(repositoryService, task, new HashMap<>());
            if (CollectionUtils.isNotEmpty(nextUserTask)) {
                for (UserTask userTask : nextUserTask) {
                    MultiInstanceLoopCharacteristics multiInstance = userTask.getLoopCharacteristics();
                    // 会签节点
                    if (Objects.nonNull(multiInstance)) {
                        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
                        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                        String appId = loginUser.getDepartId();
//                        if (StringUtils.isNotBlank(appId)) {
//                            sysUserQueryWrapper.eq(SysUser.APP_ID, appId);
//                        }
                        //todo 获取部门下的其他用户
                        List<SysUser> list = sysUserService.list(sysUserQueryWrapper);

                        flowNextDto.setVars(ProcessConstants.PROCESS_MULTI_INSTANCE_USER);
                        flowNextDto.setType(ProcessConstants.PROCESS_MULTI_INSTANCE);
                        flowNextDto.setUserList(list);
                    } else {

//            // 读取自定义节点属性 判断是否是否需要动态指定任务接收人员、组
//            String dataType = userTask.getAttributeValue(ProcessConstants.NAMASPASE,
//                ProcessConstants.PROCESS_CUSTOM_DATA_TYPE);
                        String userType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_CUSTOM_USER_TYPE);

//            if (ProcessConstants.DATA_TYPE.equals(dataType)) {
                        // 指定单个人员
                        if (ProcessConstants.USER_TYPE_ASSIGNEE.equals(userType)) {
                            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
                            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                            String appId = loginUser.getDepartId();
//                            if (StringUtils.isNotBlank(appId)) {
//                                sysUserQueryWrapper.eq(SysUser.APP_ID, appId);
//                            }
                            //TODO 获取部门下的其他用户
                            List<SysUser> list = sysUserService.list(sysUserQueryWrapper);

                            flowNextDto.setVars(ProcessConstants.PROCESS_APPROVAL);
                            flowNextDto.setType(ProcessConstants.USER_TYPE_ASSIGNEE);
                            flowNextDto.setUserList(list);
                        }
                        // 候选人员(多个)
//              if (ProcessConstants.USER_TYPE_USERS.equals(userType)) {
//                List<SysUser> list = sysUserService.selectUserList(new SysUser());
//
//                flowNextDto.setVars(ProcessConstants.PROCESS_APPROVAL);
//                flowNextDto.setType(ProcessConstants.USER_TYPE_USERS);
//                flowNextDto.setUserList(list);
//              }
//              // 候选组
//              if (ProcessConstants.USER_TYPE_ROUPS.equals(userType)) {
//                List<SysRole> sysRoles = sysRoleService.selectRoleAll();
//
//                flowNextDto.setVars(ProcessConstants.PROCESS_APPROVAL);
//                flowNextDto.setType(ProcessConstants.USER_TYPE_ROUPS);
//                flowNextDto.setRoleList(sysRoles);
//              }
//            }
                    }
                }
            } else {
                return ResponseResult.success(new FlowNextDto());
            }
        }
        return ResponseResult.success(flowNextDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult complete(FlowTaskVo taskVo) {
        Task task = taskService.createTaskQuery().taskId(taskVo.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            return ResponseResult.error("任务不存在");
        }
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.addComment(taskVo.getTaskId(), taskVo.getInstanceId(), FlowCommentEnum.DELEGATE.getType(), taskVo.getComment());
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            //创建子任务
            TaskEntity taskEntity = createSubTask(taskVo, loginUser.getId());
            taskService.complete(taskEntity.getId());

            taskService.resolveTask(taskVo.getTaskId(), taskVo.getValues());
        } else {
            taskService.addComment(taskVo.getTaskId(), taskVo.getInstanceId(), FlowCommentEnum.NORMAL.getType(), taskVo.getComment());
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            taskService.setAssignee(taskVo.getTaskId(), loginUser.getId());
            taskService.complete(taskVo.getTaskId(), taskVo.getValues());
        }
        return ResponseResult.success();
    }

    protected TaskEntity createSubTask(FlowTaskVo ptask, String assignee) {
        TaskEntity task = null;
        if (ptask != null) {
            //1.生成子任务
            task = (TaskEntity) taskService.newTask(IdUtil.fastUUID());
            task.setAssignee(assignee);
            task.setParentTaskId(ptask.getTaskId());
            task.setProcessInstanceId(ptask.getInstanceId());
            task.setCreateTime(new Date());
            taskService.saveTask(task);
        }
        return task;
    }

    @Override
    public ResponseResult<List<UserTask>> findReturnTaskList(FlowTaskVo flowTaskVo) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息，暂不考虑子流程情况
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 获取当前任务节点元素
        UserTask source = null;
        if (flowElements != null) {
            for (FlowElement flowElement : flowElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = (UserTask) flowElement;
                }
            }
        }
        // 获取节点的所有路线
        List<List<UserTask>> roads = FlowableUtils.findRoad(source, null, null, null);
        // 可回退的节点列表
        List<UserTask> userTaskList = new ArrayList<>();
        for (List<UserTask> road : roads) {
            if (userTaskList.size() == 0) {
                // 还没有可回退节点直接添加
                userTaskList = road;
            } else {
                // 如果已有回退节点，则比对取交集部分
                userTaskList.retainAll(road);
            }
        }
        return ResponseResult.success(userTaskList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void taskReturn(FlowTaskVo flowTaskVo) {
        if (taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult().isSuspended()) {
            throw new HsServerException(-1, "任务处于挂起状态");
        }
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        // 获取跳转的节点元素
        FlowElement target = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 当前任务节点元素
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
                // 跳转的节点元素
                if (flowElement.getId().equals(flowTaskVo.getTargetKey())) {
                    target = flowElement;
                }
            }
        }

        // 从当前节点向前扫描
        // 如果存在路线上不存在目标节点，说明目标节点是在网关上或非同一路线上，不可跳转
        // 否则目标节点相对于当前节点，属于串行
        Boolean isSequential = FlowableUtils.iteratorCheckSequentialReferTarget(source, flowTaskVo.getTargetKey(), null, null);
        if (!isSequential) {
            throw new HsServerException(-1, "当前节点相对于目标节点，不属于串行关系，无法回退");
        }

        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(target, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置回退意见
        for (String currentTaskId : currentTaskIds) {
            taskService.addComment(currentTaskId, task.getProcessInstanceId(), FlowCommentEnum.REBACK.getType(), flowTaskVo.getComment());
        }

        try {
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetKey 跳转到的节点(1)
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdsToSingleActivityId(currentIds, flowTaskVo.getTargetKey()).changeState();
        } catch (FlowableObjectNotFoundException e) {
            throw new HsServerException(-1, "未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new HsServerException(-1, "无法取消或开始活动");
        }
    }

    @Override
    public void taskReject(FlowTaskVo flowTaskVo) {
        if (taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult().isSuspended()) {
            throw new HsServerException(-1, "任务处于挂起状态");
        }
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    // 获取节点信息
                    source = flowElement;
                }
            }
        }

        // 目的获取所有跳转到的节点 targetIds
        // 获取当前节点的所有父级用户任务节点
        // 深度优先算法思想：延边迭代深入
        List<UserTask> parentUserTaskList = FlowableUtils.iteratorFindParentUserTasks(source, null, null);
        if (parentUserTaskList == null || parentUserTaskList.size() == 0) {
            throw new HsServerException(-1, "当前节点为初始任务节点，不能驳回");
        }
        // 获取活动 ID 即节点 Key
        List<String> parentUserTaskKeyList = new ArrayList<>();
        parentUserTaskList.forEach(item -> parentUserTaskKeyList.add(item.getId()));
        // 获取全部历史节点活动实例，即已经走过的节点历史，数据采用开始时间升序
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
        // 数据清洗，将回滚导致的脏数据清洗掉
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(allElements, historicTaskInstanceList);
        // 此时历史任务实例为倒序，获取最后走的节点
        List<String> targetIds = new ArrayList<>();
        // 循环结束标识，遇到当前目标节点的次数
        int number = 0;
        StringBuilder parentHistoricTaskKey = new StringBuilder();
        for (String historicTaskInstanceKey : lastHistoricTaskInstanceList) {
            // 当会签时候会出现特殊的，连续都是同一个节点历史数据的情况，这种时候跳过
            if (parentHistoricTaskKey.toString().equals(historicTaskInstanceKey)) {
                continue;
            }
            parentHistoricTaskKey = new StringBuilder(historicTaskInstanceKey);
            if (historicTaskInstanceKey.equals(task.getTaskDefinitionKey())) {
                number++;
            }
            // 在数据清洗后，历史节点就是唯一一条从起始到当前节点的历史记录，理论上每个点只会出现一次
            // 在流程中如果出现循环，那么每次循环中间的点也只会出现一次，再出现就是下次循环
            // number == 1，第一次遇到当前节点
            // number == 2，第二次遇到，代表最后一次的循环范围
            if (number == 2) {
                break;
            }
            // 如果当前历史节点，属于父级的节点，说明最后一次经过了这个点，需要退回这个点
            if (parentUserTaskKeyList.contains(historicTaskInstanceKey)) {
                targetIds.add(historicTaskInstanceKey);
            }
        }

        // 目的获取所有需要被跳转的节点 currentIds
        // 取其中一个父级任务，因为后续要么存在公共网关，要么就是串行公共线路
        UserTask oneUserTask = parentUserTaskList.get(0);
        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需驳回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(oneUserTask, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 规定：并行网关之前节点必须需存在唯一用户任务节点，如果出现多个任务节点，则并行网关节点默认为结束节点，原因为不考虑多对多情况
        if (targetIds.size() > 1 && currentIds.size() > 1) {
            throw new HsServerException(-1, "任务出现多对多情况，无法撤回");
        }

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置驳回意见
        currentTaskIds.forEach(item -> taskService.addComment(item, task.getProcessInstanceId(), FlowCommentEnum.REJECT.getType(), flowTaskVo.getComment()));

        try {
            // 如果父级任务多于 1 个，说明当前节点不是并行节点，原因为不考虑多对多情况
            if (targetIds.size() > 1) {
                // 1 对 多任务跳转，currentIds 当前节点(1)，targetIds 跳转到的节点(多)
                runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveSingleActivityIdToActivityIds(currentIds.get(0), targetIds).changeState();
            }
            // 如果父级任务只有一个，因此当前任务可能为网关中的任务
            if (targetIds.size() == 1) {
                // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetIds.get(0) 跳转到的节点(1)
                runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdsToSingleActivityId(currentIds, targetIds.get(0)).changeState();
            }
        } catch (FlowableObjectNotFoundException e) {
            throw new HsServerException(-1, "未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new HsServerException(-1, "无法取消或开始活动");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delegateTask(FlowTaskVo flowTaskVo) {
        taskService.addComment(flowTaskVo.getTaskId(), flowTaskVo.getInstanceId(), FlowCommentEnum.DELEGATE.getType(), flowTaskVo.getComment());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        taskService.setAssignee(flowTaskVo.getTaskId(), loginUser.getId());
        taskService.delegateTask(flowTaskVo.getTaskId(), flowTaskVo.getAssignee());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignTask(FlowTaskVo flowTaskVo) {
        taskService.setAssignee(flowTaskVo.getTaskId(), flowTaskVo.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void claim(FlowTaskVo flowTaskVo) {
        taskService.claim(flowTaskVo.getTaskId(), flowTaskVo.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unClaim(FlowTaskVo flowTaskVo) {
        taskService.unclaim(flowTaskVo.getTaskId());
    }

    @Override
    public ResponseResult<String> deleteProcIns(String procInsId) {
        try {
            historyService.deleteHistoricProcessInstance(procInsId);
        } catch (Exception e) {
            log.error("删除流程实例报错,实例id为：" + procInsId, e);
            return ResponseResult.error(-1, e.getMessage());
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult stopProcess(FlowTaskVo flowTaskVo) {
        List<Task> task = taskService.createTaskQuery().processInstanceId(flowTaskVo.getInstanceId()).list();
        if (CollectionUtils.isEmpty(task)) {
            throw new HsServerException(-1, "流程未启动或已执行完成，取消申请失败");
        }

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowTaskVo.getInstanceId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        if (Objects.nonNull(bpmnModel)) {
            Process process = bpmnModel.getMainProcess();
            List<EndEvent> endNodes = process.findFlowElementsOfType(EndEvent.class, false);
            if (CollectionUtils.isNotEmpty(endNodes)) {
                Authentication.setAuthenticatedUserId(loginUser.getId());
//                taskService.addComment(task.getId(), processInstance.getProcessInstanceId(), FlowComment.STOP.getType(),
//                        StringUtils.isBlank(flowTaskVo.getComment()) ? "取消申请" : flowTaskVo.getComment());
                String endId = endNodes.get(0).getId();
                List<Execution> executions = runtimeService.createExecutionQuery().parentId(processInstance.getProcessInstanceId()).list();
                List<String> executionIds = new ArrayList<>();
                executions.forEach(execution -> executionIds.add(execution.getId()));
                runtimeService.createChangeActivityStateBuilder().moveExecutionsToSingleActivityId(executionIds, endId).changeState();
            }
        }

        return ResponseResult.success();
    }

    /**
     * 流程完成时间处理
     *
     * @param ms
     * @return
     */
    private String getDate(long ms) {

        long day = ms / (24 * 60 * 60 * 1000);
        long hour = (ms / (60 * 60 * 1000) - day * 24);
        long minute = ((ms / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (ms / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟";
        }
        if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        }
        if (minute > 0) {
            return minute + "分钟";
        }
        if (second > 0) {
            return second + "秒";
        } else {
            return 0 + "秒";
        }
    }
}
