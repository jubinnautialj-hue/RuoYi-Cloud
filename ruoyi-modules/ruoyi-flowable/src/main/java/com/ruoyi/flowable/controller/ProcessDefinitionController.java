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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.flowable.domain.ProcessDefinitionVo;
import com.ruoyi.flowable.service.IProcessDefinitionService;

@RestController
@RequestMapping("/definition")
public class ProcessDefinitionController extends BaseController
{
    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @Autowired
    private RepositoryService repositoryService;

    @PreAuthorize("@ss.hasPermi('flowable:definition:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProcessDefinitionVo processDefinition)
    {
        startPage();
        List<ProcessDefinitionVo> list = processDefinitionService.selectProcessDefinitionList(processDefinition);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('flowable:definition:query')")
    @GetMapping(value = { "/", "/{processDefinitionId}" })
    public AjaxResult getInfo(@PathVariable(value = "processDefinitionId", required = false) String processDefinitionId)
    {
        AjaxResult ajax = AjaxResult.success();
        if (processDefinitionId != null)
        {
            ProcessDefinitionVo processDefinition = processDefinitionService.selectProcessDefinitionById(processDefinitionId);
            ajax.put(AjaxResult.DATA_TAG, processDefinition);
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('flowable:definition:deploy')")
    @Log(title = "流程定义", businessType = BusinessType.INSERT)
    @PostMapping("/deploy")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deploy(@RequestParam("file") MultipartFile file,
                              @RequestParam(value = "category", required = false) String category) throws IOException
    {
        String fileName = file.getOriginalFilename();
        if (fileName == null)
        {
            return AjaxResult.error("上传文件为空");
        }
        Deployment deployment = repositoryService.createDeployment()
                .name(fileName)
                .addInputStream(fileName, file.getInputStream())
                .deploy();
        return AjaxResult.success("部署成功，部署ID：" + deployment.getId());
    }

    @PreAuthorize("@ss.hasPermi('flowable:definition:remove')")
    @Log(title = "流程定义", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deploymentIds}")
    public AjaxResult remove(@PathVariable String[] deploymentIds)
    {
        for (String deploymentId : deploymentIds)
        {
            processDefinitionService.deleteDeployment(deploymentId, true);
        }
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('flowable:definition:edit')")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    @PutMapping("/activate/{processDefinitionId}")
    public AjaxResult activate(@PathVariable String processDefinitionId)
    {
        return toAjax(processDefinitionService.activateProcessDefinitionById(processDefinitionId));
    }

    @PreAuthorize("@ss.hasPermi('flowable:definition:edit')")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    @PutMapping("/suspend/{processDefinitionId}")
    public AjaxResult suspend(@PathVariable String processDefinitionId)
    {
        return toAjax(processDefinitionService.suspendProcessDefinitionById(processDefinitionId));
    }

    @GetMapping("/resource/{deploymentId}/{resourceName}")
    public void getProcessDefinitionResource(@PathVariable String deploymentId,
                                               @PathVariable String resourceName,
                                               HttpServletResponse response) throws IOException
    {
        InputStream inputStream = processDefinitionService.getProcessDefinitionResource(deploymentId, resourceName);
        if (inputStream != null)
        {
            response.setContentType("application/xml;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + resourceName);
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }

    @GetMapping("/diagram/{processDefinitionId}")
    public void getProcessDiagram(@PathVariable String processDefinitionId,
                                    HttpServletResponse response) throws IOException
    {
        InputStream inputStream = processDefinitionService.getProcessDiagram(processDefinitionId);
        if (inputStream != null)
        {
            response.setContentType("image/png");
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }
}
