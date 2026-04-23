package com.ruoyi.flowable.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.flowable.domain.ProcessModel;
import com.ruoyi.flowable.service.IProcessModelService;

@RestController
@RequestMapping("/model")
public class ProcessModelController extends BaseController
{
    @Autowired
    private IProcessModelService processModelService;

    @PreAuthorize("@ss.hasPermi('flowable:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProcessModel processModel)
    {
        startPage();
        List<ProcessModel> list = processModelService.selectProcessModelList(processModel);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('flowable:model:query')")
    @GetMapping(value = { "/", "/{modelId}" })
    public AjaxResult getInfo(@PathVariable(value = "modelId", required = false) String modelId)
    {
        AjaxResult ajax = AjaxResult.success();
        if (modelId != null)
        {
            ProcessModel processModel = processModelService.selectProcessModelById(modelId);
            ajax.put(AjaxResult.DATA_TAG, processModel);
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('flowable:model:add')")
    @Log(title = "流程模型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProcessModel processModel)
    {
        String modelId = processModelService.createProcessModel(processModel);
        return toAjax(modelId != null);
    }

    @PreAuthorize("@ss.hasPermi('flowable:model:edit')")
    @Log(title = "流程模型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProcessModel processModel)
    {
        return toAjax(processModelService.updateProcessModel(processModel));
    }

    @PreAuthorize("@ss.hasPermi('flowable:model:remove')")
    @Log(title = "流程模型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{modelIds}")
    public AjaxResult remove(@PathVariable String[] modelIds)
    {
        return toAjax(processModelService.deleteProcessModelByIds(modelIds));
    }

    @PreAuthorize("@ss.hasPermi('flowable:model:edit')")
    @PostMapping("/saveEditor")
    public AjaxResult saveEditor(@RequestParam("modelId") String modelId,
                                   @RequestParam("json_xml") String jsonXml,
                                   @RequestParam("svg_xml") String svgXml)
    {
        return toAjax(processModelService.saveModelEditor(modelId, jsonXml, svgXml));
    }

    @GetMapping("/editor/{modelId}")
    public void getModelEditorSource(@PathVariable String modelId, HttpServletResponse response) throws IOException
    {
        InputStream inputStream = processModelService.getModelEditorSource(modelId);
        if (inputStream != null)
        {
            response.setContentType("application/json;charset=UTF-8");
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }

    @PreAuthorize("@ss.hasPermi('flowable:model:deploy')")
    @Log(title = "流程模型", businessType = BusinessType.INSERT)
    @PostMapping("/deploy/{modelId}")
    public AjaxResult deploy(@PathVariable String modelId)
    {
        String deploymentId = processModelService.deployProcessModel(modelId);
        if (deploymentId != null)
        {
            return AjaxResult.success("部署成功，部署ID：" + deploymentId);
        }
        return AjaxResult.error("部署失败");
    }
}
