package com.ruoyi.flowable.service.impl;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.flowable.domain.ProcessModel;
import com.ruoyi.flowable.service.IProcessModelService;

@Service
public class ProcessModelServiceImpl implements IProcessModelService
{
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<ProcessModel> selectProcessModelList(ProcessModel processModel)
    {
        ModelQuery query = repositoryService.createModelQuery();
        if (StringUtils.isNotBlank(processModel.getKey()))
        {
            query.modelKey(processModel.getKey());
        }
        if (StringUtils.isNotBlank(processModel.getName()))
        {
            query.modelNameLike("%" + processModel.getName() + "%");
        }
        if (StringUtils.isNotBlank(processModel.getCategory()))
        {
            query.modelCategory(processModel.getCategory());
        }
        query.orderByLastUpdateTime().desc();
        List<Model> models = query.list();
        List<ProcessModel> processModels = new ArrayList<>();
        for (Model model : models)
        {
            processModels.add(convertToProcessModel(model));
        }
        return processModels;
    }

    @Override
    public ProcessModel selectProcessModelById(String id)
    {
        Model model = repositoryService.createModelQuery().modelId(id).singleResult();
        if (model != null)
        {
            return convertToProcessModel(model);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessModel(ProcessModel processModel)
    {
        ObjectNode metaInfo = objectMapper.createObjectNode();
        metaInfo.put("name", processModel.getName());
        metaInfo.put("description", processModel.getDescription());

        Model model = repositoryService.newModel();
        model.setName(processModel.getName());
        model.setKey(processModel.getKey());
        model.setCategory(processModel.getCategory());
        model.setMetaInfo(metaInfo.toString());
        model.setVersion(1);
        repositoryService.saveModel(model);

        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("resourceId", model.getId());
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.set("stencilset", stencilSetNode);
        ObjectNode propertiesNode = objectMapper.createObjectNode();
        propertiesNode.put("process_id", processModel.getKey());
        propertiesNode.put("name", processModel.getName());
        propertiesNode.put("documentation", processModel.getDescription());
        editorNode.set("properties", propertiesNode);

        repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
        return model.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProcessModel(ProcessModel processModel)
    {
        Model model = repositoryService.createModelQuery().modelId(processModel.getId()).singleResult();
        if (model != null)
        {
            model.setName(processModel.getName());
            model.setKey(processModel.getKey());
            model.setCategory(processModel.getCategory());
            ObjectNode metaInfo = objectMapper.createObjectNode();
            metaInfo.put("name", processModel.getName());
            metaInfo.put("description", processModel.getDescription());
            model.setMetaInfo(metaInfo.toString());
            repositoryService.saveModel(model);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProcessModelByIds(String[] ids)
    {
        for (String id : ids)
        {
            repositoryService.deleteModel(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveModelEditor(String modelId, String jsonXml, String svgXml)
    {
        try
        {
            Model model = repositoryService.createModelQuery().modelId(modelId).singleResult();
            if (model != null)
            {
                repositoryService.addModelEditorSource(modelId, jsonXml.getBytes(StandardCharsets.UTF_8));
                if (StringUtils.isNotBlank(svgXml))
                {
                    repositoryService.addModelEditorSourceExtra(modelId, svgXml.getBytes(StandardCharsets.UTF_8));
                }
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public InputStream getModelEditorSource(String modelId)
    {
        return repositoryService.getModelEditorSource(modelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deployProcessModel(String modelId)
    {
        Model model = repositoryService.createModelQuery().modelId(modelId).singleResult();
        if (model == null)
        {
            return null;
        }
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        String bpmnXml = new String(bpmnBytes, StandardCharsets.UTF_8);

        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName())
                .addString(model.getKey() + ".bpmn20.xml", bpmnXml)
                .deploy();

        model.setDeploymentId(deployment.getId());
        repositoryService.saveModel(model);

        return deployment.getId();
    }

    @Override
    public ProcessModel convertToProcessModel(Model model)
    {
        ProcessModel processModel = new ProcessModel();
        processModel.setId(model.getId());
        processModel.setName(model.getName());
        processModel.setKey(model.getKey());
        processModel.setCategory(model.getCategory());
        processModel.setVersion(model.getVersion());
        processModel.setCreateTime(model.getCreateTime());
        processModel.setLastUpdateTime(model.getLastUpdateTime());
        processModel.setMetaInfo(model.getMetaInfo());
        processModel.setDeploymentId(model.getDeploymentId());
        processModel.setEditorSourceValueId(model.getEditorSourceValueId());
        processModel.setEditorSourceExtraValueId(model.getEditorSourceExtraValueId());
        processModel.setTenantId(model.getTenantId());
        return processModel;
    }
}
