package com.ruoyi.flowable.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.flowable.domain.ProcessDefinitionVo;
import com.ruoyi.flowable.service.IProcessDefinitionService;

@Service
public class ProcessDefinitionServiceImpl implements IProcessDefinitionService
{
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public List<ProcessDefinitionVo> selectProcessDefinitionList(ProcessDefinitionVo processDefinition)
    {
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        if (StringUtils.isNotBlank(processDefinition.getKey()))
        {
            query.processDefinitionKey(processDefinition.getKey());
        }
        if (StringUtils.isNotBlank(processDefinition.getName()))
        {
            query.processDefinitionNameLike("%" + processDefinition.getName() + "%");
        }
        if (StringUtils.isNotBlank(processDefinition.getCategory()))
        {
            query.processDefinitionCategory(processDefinition.getCategory());
        }
        query.orderByProcessDefinitionVersion().desc();
        List<ProcessDefinition> processDefinitions = query.list();
        List<ProcessDefinitionVo> processDefinitionVos = new ArrayList<>();
        for (ProcessDefinition pd : processDefinitions)
        {
            processDefinitionVos.add(convertToProcessDefinitionVo(pd));
        }
        return processDefinitionVos;
    }

    @Override
    public ProcessDefinitionVo selectProcessDefinitionById(String id)
    {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        if (processDefinition != null)
        {
            return convertToProcessDefinitionVo(processDefinition);
        }
        return null;
    }

    @Override
    public InputStream getProcessDefinitionResource(String deploymentId, String resourceName)
    {
        return repositoryService.getResourceAsStream(deploymentId, resourceName);
    }

    @Override
    public InputStream getProcessDiagram(String processDefinitionId)
    {
        ProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        if (processDefinition != null)
        {
            InputStream bpmnXmlStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
            return diagramGenerator.generateDiagram(bpmnXmlStream, "png", new ArrayList<>(), new ArrayList<>(), "宋体", "宋体", "宋体", null, false);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activateProcessDefinitionById(String processDefinitionId)
    {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        if (processDefinition != null && processDefinition.isSuspended())
        {
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean suspendProcessDefinitionById(String processDefinitionId)
    {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        if (processDefinition != null && !processDefinition.isSuspended())
        {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDeployment(String deploymentId, boolean cascade)
    {
        repositoryService.deleteDeployment(deploymentId, cascade);
        return true;
    }

    @Override
    public ProcessDefinitionVo convertToProcessDefinitionVo(ProcessDefinition processDefinition)
    {
        ProcessDefinitionVo vo = new ProcessDefinitionVo();
        vo.setId(processDefinition.getId());
        vo.setName(processDefinition.getName());
        vo.setKey(processDefinition.getKey());
        vo.setCategory(processDefinition.getCategory());
        vo.setVersion(processDefinition.getVersion());
        vo.setDeploymentId(processDefinition.getDeploymentId());
        vo.setResourceName(processDefinition.getResourceName());
        vo.setDiagramResourceName(processDefinition.getDiagramResourceName());
        vo.setDescription(processDefinition.getDescription());
        vo.setSuspended(processDefinition.isSuspended());
        vo.setTenantId(processDefinition.getTenantId());

        Deployment deployment = repositoryService.createDeploymentQuery()
                .deploymentId(processDefinition.getDeploymentId())
                .singleResult();
        if (deployment != null)
        {
            vo.setDeploymentTime(deployment.getDeploymentTime());
        }

        return vo;
    }
}
