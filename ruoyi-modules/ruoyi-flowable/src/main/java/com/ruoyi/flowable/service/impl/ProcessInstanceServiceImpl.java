package com.ruoyi.flowable.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.flowable.domain.ProcessInstanceVo;
import com.ruoyi.flowable.service.IProcessInstanceService;

@Service
public class ProcessInstanceServiceImpl implements IProcessInstanceService
{
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IdentityService identityService;

    @Override
    public List<ProcessInstanceVo> selectRunningProcessInstanceList(ProcessInstanceVo processInstance)
    {
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
        if (StringUtils.isNotBlank(processInstance.getProcessDefinitionKey()))
        {
            query.processDefinitionKey(processInstance.getProcessDefinitionKey());
        }
        if (StringUtils.isNotBlank(processInstance.getProcessDefinitionName()))
        {
            query.processDefinitionNameLike("%" + processInstance.getProcessDefinitionName() + "%");
        }
        if (StringUtils.isNotBlank(processInstance.getBusinessKey()))
        {
            query.processInstanceBusinessKey(processInstance.getBusinessKey());
        }
        if (StringUtils.isNotBlank(processInstance.getStartUserId()))
        {
            query.startedBy(processInstance.getStartUserId());
        }
        query.orderByProcessInstanceStartTime().desc();
        List<ProcessInstance> instances = query.list();
        List<ProcessInstanceVo> instanceVos = new ArrayList<>();
        for (ProcessInstance instance : instances)
        {
            instanceVos.add(convertToProcessInstanceVo(instance));
        }
        return instanceVos;
    }

    @Override
    public List<ProcessInstanceVo> selectHistoryProcessInstanceList(ProcessInstanceVo processInstance)
    {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        if (StringUtils.isNotBlank(processInstance.getProcessDefinitionKey()))
        {
            query.processDefinitionKey(processInstance.getProcessDefinitionKey());
        }
        if (StringUtils.isNotBlank(processInstance.getProcessDefinitionName()))
        {
            query.processDefinitionNameLike("%" + processInstance.getProcessDefinitionName() + "%");
        }
        if (StringUtils.isNotBlank(processInstance.getBusinessKey()))
        {
            query.processInstanceBusinessKey(processInstance.getBusinessKey());
        }
        if (StringUtils.isNotBlank(processInstance.getStartUserId()))
        {
            query.startedBy(processInstance.getStartUserId());
        }
        if (processInstance.isEnded())
        {
            query.finished();
        }
        query.orderByProcessInstanceStartTime().desc();
        List<HistoricProcessInstance> instances = query.list();
        List<ProcessInstanceVo> instanceVos = new ArrayList<>();
        for (HistoricProcessInstance instance : instances)
        {
            instanceVos.add(convertToHistoricProcessInstanceVo(instance));
        }
        return instanceVos;
    }

    @Override
    public ProcessInstanceVo selectProcessInstanceById(String processInstanceId)
    {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (instance != null)
        {
            return convertToProcessInstanceVo(instance);
        }
        HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (historicInstance != null)
        {
            return convertToHistoricProcessInstanceVo(historicInstance);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstanceVo startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables)
    {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        if (processDefinition == null)
        {
            return null;
        }

        if (variables == null)
        {
            variables = new HashMap<>();
        }
        if (StringUtils.isNotBlank(businessKey))
        {
            variables.put("businessKey", businessKey);
        }

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, businessKey, variables);
        return convertToProcessInstanceVo(processInstance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstanceVo startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables)
    {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();
        if (processDefinition == null)
        {
            return null;
        }

        if (variables == null)
        {
            variables = new HashMap<>();
        }
        if (StringUtils.isNotBlank(businessKey))
        {
            variables.put("businessKey", businessKey);
        }

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        return convertToProcessInstanceVo(processInstance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean suspendProcessInstanceById(String processInstanceId)
    {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance != null && !processInstance.isSuspended())
        {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activateProcessInstanceById(String processInstanceId)
    {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance != null && processInstance.isSuspended())
        {
            runtimeService.activateProcessInstanceById(processInstanceId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProcessInstanceById(String processInstanceId, String deleteReason)
    {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
        return true;
    }

    @Override
    public InputStream getProcessInstanceDiagram(String processInstanceId)
    {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance == null)
        {
            return null;
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);

        ProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivities, new ArrayList<>(), "宋体", "宋体", "宋体", null, false);
    }

    @Override
    public List<Map<String, Object>> getProcessInstanceActivityList(String processInstanceId)
    {
        List<Map<String, Object>> activityList = new ArrayList<>();
        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        for (HistoricActivityInstance activity : activities)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("id", activity.getId());
            map.put("activityId", activity.getActivityId());
            map.put("activityName", activity.getActivityName());
            map.put("activityType", activity.getActivityType());
            map.put("assignee", activity.getAssignee());
            map.put("startTime", activity.getStartTime());
            map.put("endTime", activity.getEndTime());
            map.put("duration", activity.getDurationInMillis());
            map.put("processInstanceId", activity.getProcessInstanceId());
            map.put("executionId", activity.getExecutionId());
            map.put("processDefinitionId", activity.getProcessDefinitionId());
            map.put("taskId", activity.getTaskId());
            map.put("deleteReason", activity.getDeleteReason());
            activityList.add(map);
        }
        return activityList;
    }

    @Override
    public ProcessInstanceVo convertToProcessInstanceVo(ProcessInstance processInstance)
    {
        ProcessInstanceVo vo = new ProcessInstanceVo();
        vo.setId(processInstance.getId());
        vo.setName(processInstance.getName());
        vo.setBusinessKey(processInstance.getBusinessKey());
        vo.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        vo.setProcessDefinitionName(processInstance.getProcessDefinitionName());
        vo.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        vo.setStartUserId(processInstance.getStartUserId());
        vo.setStartTime(processInstance.getStartTime());
        vo.setTenantId(processInstance.getTenantId());
        vo.setSuspended(processInstance.isSuspended());
        vo.setEnded(processInstance.isEnded());
        vo.setStatus(processInstance.isSuspended() ? "挂起" : "运行中");
        return vo;
    }

    @Override
    public ProcessInstanceVo convertToHistoricProcessInstanceVo(HistoricProcessInstance historicProcessInstance)
    {
        ProcessInstanceVo vo = new ProcessInstanceVo();
        vo.setId(historicProcessInstance.getId());
        vo.setName(historicProcessInstance.getName());
        vo.setBusinessKey(historicProcessInstance.getBusinessKey());
        vo.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
        vo.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
        vo.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
        vo.setStartUserId(historicProcessInstance.getStartUserId());
        vo.setStartTime(historicProcessInstance.getStartTime());
        vo.setEndTime(historicProcessInstance.getEndTime());
        vo.setDuration(historicProcessInstance.getDurationInMillis());
        vo.setTenantId(historicProcessInstance.getTenantId());
        vo.setDeleteReason(historicProcessInstance.getDeleteReason());
        vo.setEnded(historicProcessInstance.getEndTime() != null);
        vo.setStatus(historicProcessInstance.getEndTime() != null ? "已结束" : "运行中");
        return vo;
    }
}
