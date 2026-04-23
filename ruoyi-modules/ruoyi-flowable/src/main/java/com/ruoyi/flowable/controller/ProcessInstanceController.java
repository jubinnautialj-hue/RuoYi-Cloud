package com.ruoyi.flowable.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
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
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.flowable.domain.ProcessInstanceVo;
import com.ruoyi.flowable.service.IProcessInstanceService;

@RestController
@RequestMapping("/instance")
public class ProcessInstanceController extends BaseController
{
    @Autowired
    private IProcessInstanceService processInstanceService;

    @PreAuthorize("@ss.hasPermi('flowable:instance:runningList')")
    @GetMapping("/runningList")
    public TableDataInfo runningList(ProcessInstanceVo processInstance)
    {
        startPage();
        List<ProcessInstanceVo> list = processInstanceService.selectRunningProcessInstanceList(processInstance);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('flowable:instance:historyList')")
    @GetMapping("/historyList")
    public TableDataInfo historyList(ProcessInstanceVo processInstance)
    {
        startPage();
        List<ProcessInstanceVo> list = processInstanceService.selectHistoryProcessInstanceList(processInstance);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('flowable:instance:query')")
    @GetMapping(value = { "/", "/{processInstanceId}" })
    public AjaxResult getInfo(@PathVariable(value = "processInstanceId", required = false) String processInstanceId)
    {
        AjaxResult ajax = AjaxResult.success();
        if (processInstanceId != null)
        {
            ProcessInstanceVo processInstance = processInstanceService.selectProcessInstanceById(processInstanceId);
            ajax.put(AjaxResult.DATA_TAG, processInstance);
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('flowable:instance:start')")
    @Log(title = "流程实例", businessType = BusinessType.INSERT)
    @PostMapping("/startById")
    public AjaxResult startById(@RequestParam("processDefinitionId") String processDefinitionId,
                                  @RequestParam(value = "businessKey", required = false) String businessKey,
                                  @RequestBody(required = false) Map<String, Object> variables)
    {
        ProcessInstanceVo processInstance = processInstanceService.startProcessInstanceById(processDefinitionId, businessKey, variables);
        if (processInstance != null)
        {
            return AjaxResult.success("流程启动成功", processInstance);
        }
        return AjaxResult.error("流程启动失败");
    }

    @PreAuthorize("@ss.hasPermi('flowable:instance:start')")
    @Log(title = "流程实例", businessType = BusinessType.INSERT)
    @PostMapping("/startByKey")
    public AjaxResult startByKey(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                  @RequestParam(value = "businessKey", required = false) String businessKey,
                                  @RequestBody(required = false) Map<String, Object> variables)
    {
        ProcessInstanceVo processInstance = processInstanceService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        if (processInstance != null)
        {
            return AjaxResult.success("流程启动成功", processInstance);
        }
        return AjaxResult.error("流程启动失败");
    }

    @PreAuthorize("@ss.hasPermi('flowable:instance:edit')")
    @Log(title = "流程实例", businessType = BusinessType.UPDATE)
    @PutMapping("/suspend/{processInstanceId}")
    public AjaxResult suspend(@PathVariable String processInstanceId)
    {
        return toAjax(processInstanceService.suspendProcessInstanceById(processInstanceId));
    }

    @PreAuthorize("@ss.hasPermi('flowable:instance:edit')")
    @Log(title = "流程实例", businessType = BusinessType.UPDATE)
    @PutMapping("/activate/{processInstanceId}")
    public AjaxResult activate(@PathVariable String processInstanceId)
    {
        return toAjax(processInstanceService.activateProcessInstanceById(processInstanceId));
    }

    @PreAuthorize("@ss.hasPermi('flowable:instance:remove')")
    @Log(title = "流程实例", businessType = BusinessType.DELETE)
    @DeleteMapping("/{processInstanceId}")
    public AjaxResult remove(@PathVariable String processInstanceId,
                              @RequestParam(value = "deleteReason", defaultValue = "手动删除") String deleteReason)
    {
        return toAjax(processInstanceService.deleteProcessInstanceById(processInstanceId, deleteReason));
    }

    @GetMapping("/diagram/{processInstanceId}")
    public void getProcessInstanceDiagram(@PathVariable String processInstanceId,
                                           HttpServletResponse response) throws IOException
    {
        InputStream inputStream = processInstanceService.getProcessInstanceDiagram(processInstanceId);
        if (inputStream != null)
        {
            response.setContentType("image/png");
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }

    @GetMapping("/activityList/{processInstanceId}")
    public AjaxResult getProcessInstanceActivityList(@PathVariable String processInstanceId)
    {
        List<Map<String, Object>> activityList = processInstanceService.getProcessInstanceActivityList(processInstanceId);
        return AjaxResult.success(activityList);
    }
}
