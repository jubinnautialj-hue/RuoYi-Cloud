package com.ruoyi.flowable.service;

import java.io.InputStream;
import java.util.List;
import com.ruoyi.flowable.domain.ProcessDefinitionVo;

public interface IProcessDefinitionService
{
    public List<ProcessDefinitionVo> selectProcessDefinitionList(ProcessDefinitionVo processDefinition);

    public ProcessDefinitionVo selectProcessDefinitionById(String id);

    public InputStream getProcessDefinitionResource(String deploymentId, String resourceName);

    public InputStream getProcessDiagram(String processDefinitionId);

    public boolean activateProcessDefinitionById(String processDefinitionId);

    public boolean suspendProcessDefinitionById(String processDefinitionId);

    public boolean deleteDeployment(String deploymentId, boolean cascade);

    public ProcessDefinitionVo convertToProcessDefinitionVo(org.flowable.engine.repository.ProcessDefinition processDefinition);
}
