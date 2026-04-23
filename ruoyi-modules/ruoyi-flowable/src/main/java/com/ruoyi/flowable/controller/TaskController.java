package com.ruoyi.flowable.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.flowable.domain.TaskVo;
import com.ruoyi.flowable.service.ITaskService;

@RestController
@RequestMapping("/task")
public class TaskController extends BaseController
{
    @Autowired
    private ITaskService taskService;

    @RequiresPermissions("flowable:task:todolist")
    @GetMapping("/todoList")
    public TableDataInfo todoList(TaskVo taskVo)
    {
        startPage();
        String userId = SecurityUtils.getUserId().toString();
        List<TaskVo> list = taskService.selectTodoTaskListByUserId(userId, taskVo);
        return getDataTable(list);
    }

    @RequiresPermissions("flowable:task:donelist")
    @GetMapping("/doneList")
    public TableDataInfo doneList(TaskVo taskVo)
    {
        startPage();
        String userId = SecurityUtils.getUserId().toString();
        List<TaskVo> list = taskService.selectDoneTaskListByUserId(userId, taskVo);
        return getDataTable(list);
    }

    @RequiresPermissions("flowable:task:myProcess")
    @GetMapping("/myProcess")
    public TableDataInfo myProcess(TaskVo taskVo)
    {
        startPage();
        String userId = SecurityUtils.getUserId().toString();
        List<TaskVo> list = taskService.selectMyProcessList(userId, taskVo);
        return getDataTable(list);
    }

    @RequiresPermissions("flowable:task:query")
    @GetMapping(value = { "/", "/{taskId}" })
    public AjaxResult getInfo(@PathVariable(value = "taskId", required = false) String taskId)
    {
        AjaxResult ajax = AjaxResult.success();
        if (taskId != null)
        {
            TaskVo taskVo = taskService.selectTaskById(taskId);
            ajax.put(AjaxResult.DATA_TAG, taskVo);
        }
        return ajax;
    }

    @RequiresPermissions("flowable:task:claim")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PostMapping("/claim/{taskId}")
    public AjaxResult claim(@PathVariable String taskId)
    {
        String userId = SecurityUtils.getUserId().toString();
        return toAjax(taskService.claimTask(taskId, userId));
    }

    @RequiresPermissions("flowable:task:unclaim")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PostMapping("/unclaim/{taskId}")
    public AjaxResult unclaim(@PathVariable String taskId)
    {
        return toAjax(taskService.unclaimTask(taskId));
    }

    @RequiresPermissions("flowable:task:complete")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PostMapping("/complete/{taskId}")
    public AjaxResult complete(@PathVariable String taskId, @RequestBody(required = false) Map<String, Object> variables)
    {
        return toAjax(taskService.completeTask(taskId, variables));
    }

    @RequiresPermissions("flowable:task:delegate")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PostMapping("/delegate/{taskId}")
    public AjaxResult delegate(@PathVariable String taskId, @RequestParam("userId") String userId)
    {
        return toAjax(taskService.delegateTask(taskId, userId));
    }

    @RequiresPermissions("flowable:task:resolve")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PostMapping("/resolve/{taskId}")
    public AjaxResult resolve(@PathVariable String taskId, @RequestBody(required = false) Map<String, Object> variables)
    {
        return toAjax(taskService.resolveTask(taskId, variables));
    }

    @RequiresPermissions("flowable:task:assign")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PostMapping("/assign/{taskId}")
    public AjaxResult assign(@PathVariable String taskId, @RequestParam("userId") String userId)
    {
        return toAjax(taskService.setAssignee(taskId, userId));
    }

    @RequiresPermissions("flowable:task:comment")
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @PostMapping("/comment/{taskId}")
    public AjaxResult addComment(@PathVariable String taskId,
                                   @RequestParam(value = "processInstanceId", required = false) String processInstanceId,
                                   @RequestParam("message") String message)
    {
        return toAjax(taskService.addComment(taskId, processInstanceId, message));
    }
}
