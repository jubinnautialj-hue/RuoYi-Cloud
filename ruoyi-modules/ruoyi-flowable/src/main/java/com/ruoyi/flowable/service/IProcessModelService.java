package com.ruoyi.flowable.service;

import java.io.InputStream;
import java.util.List;
import com.ruoyi.flowable.domain.ProcessModel;

public interface IProcessModelService
{
    public List<ProcessModel> selectProcessModelList(ProcessModel processModel);

    public ProcessModel selectProcessModelById(String id);

    public String createProcessModel(ProcessModel processModel);

    public boolean updateProcessModel(ProcessModel processModel);

    public boolean deleteProcessModelByIds(String[] ids);

    public boolean saveModelEditor(String modelId, String jsonXml, String svgXml);

    public InputStream getModelEditorSource(String modelId);

    public String deployProcessModel(String modelId);

    public ProcessModel convertToProcessModel(org.flowable.engine.repository.Model model);
}
