package com.lvxc.flowable.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.flowable.entity.dto.FlowProcDefDto;
import com.lvxc.flowable.entity.vo.DeployFormVo;
import com.lvxc.flowable.factory.FlowServiceFactory;
import com.lvxc.flowable.service.FlowDefinitionService;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.web.common.base.ResponseResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author lvxc
 * @Date 2021/11/5 10:37
 **/
@Service
public class FlowDefinitionServiceImpl extends FlowServiceFactory implements FlowDefinitionService {

    //  @Resource
//  protected FormMapper formMapper;
    private static final String BPMN_FILE_SUFFIX = ".bpmn";

    @Override
    public void importFile(String name, String category, InputStream in) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String appId = loginUser.getDepartId();
        Deployment deploy = repositoryService.createDeployment().addInputStream(name + BPMN_FILE_SUFFIX, in).name(name).category(category).tenantId(appId).deploy();
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        repositoryService.setProcessDefinitionCategory(definition.getId(), category);
    }

    @Override
    public Page<FlowProcDefDto> list(String name, Integer pageNum, Integer pageSize) {
        Page<FlowProcDefDto> page = new Page<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String appId = loginUser.getDepartId();
        // 流程定义列表数据查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(appId)
                .orderByProcessDefinitionVersion().desc();
        if(StringUtils.isNotBlank(name)) {
            processDefinitionQuery.processDefinitionNameLike("%"+name+"%");
        }
        page.setTotal(processDefinitionQuery.count());
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageNum - 1, pageSize);

        List<FlowProcDefDto> dataList = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            FlowProcDefDto reProcDef = new FlowProcDefDto();
            BeanUtils.copyProperties(processDefinition, reProcDef);
//      Form sysForm = formMapper.selectSysDeployFormByDeployId(deploymentId);
//      if (Objects.nonNull(sysForm)) {
//        reProcDef.setFormName(sysForm.getName());
//        reProcDef.setFormId(sysForm.getId());
//      }
            // 流程定义时间
            reProcDef.setDeploymentTime(deployment.getDeploymentTime());
            dataList.add(reProcDef);
        }
        page.setRecords(dataList);
        return page;
    }

    @Override
    public int insertSysDeployForm(DeployFormVo deployFormVo) {
//    return formMapper.insertDeployForm(deployFormVo);
        return 0;
    }

    @Override
    public void delete(String deployId) {
        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        repositoryService.deleteDeployment(deployId, true);
        // TODO 删除关联表单
    }

    @Override
    public ResponseResult<String> readXml(String deployId) throws IOException {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
        String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return ResponseResult.success(result);
    }

    @Override
    public void updateState(Integer state, String deployId) {
        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        // 发布
        if (state == 1) {
            repositoryService.activateProcessDefinitionById(procDef.getId(), true, null);
        }
        // 下线
        if (state == 2) {
            repositoryService.suspendProcessDefinitionById(procDef.getId(), true, null);
        }
    }
}
