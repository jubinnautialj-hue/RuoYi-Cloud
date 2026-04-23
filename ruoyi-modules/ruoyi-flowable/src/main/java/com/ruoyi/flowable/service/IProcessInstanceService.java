package com.ruoyi.flowable.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import com.ruoyi.flowable.domain.ProcessInstanceVo;

public interface IProcessInstanceService
{
    public List<ProcessInstanceVo> selectRunningProcessInstanceList(ProcessInstanceVo processInstance);

    public List<ProcessInstanceVo> selectHistoryProcessInstanceList(ProcessInstanceVo processInstance);

    public ProcessInstanceVo selectProcessInstanceById(String processInstanceId);

    public ProcessInstanceVo startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables);

    public ProcessInstanceVo startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    public boolean suspendProcessInstanceById(String processInstanceId);

    public boolean activateProcessInstanceById(String processInstanceId);

    public boolean deleteProcessInstanceById(String processInstanceId, String deleteReason);

    public InputStream getProcessInstanceDiagram(String processInstanceId);

    public List<Map<String, Object>> getProcessInstanceActivityList(String processInstanceId);

    public ProcessInstanceVo convertToProcessInstanceVo(org.flowable.engine.runtime.ProcessInstance processInstance);

    public ProcessInstanceVo convertToHistoricProcessInstanceVo(org.flowable.engine.history.HistoricProcessInstance historicProcessInstance);
}
