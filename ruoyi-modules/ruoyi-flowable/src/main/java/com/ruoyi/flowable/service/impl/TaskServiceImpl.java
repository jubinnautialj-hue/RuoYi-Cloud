package com.ruoyi.flowable.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.flowable.domain.TaskVo;
import com.ruoyi.flowable.service.ITaskService;

@Service
public class TaskServiceImpl implements ITaskService
{
    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    @Override
    public List<TaskVo> selectTodoTaskList(TaskVo taskVo)
    {
        TaskQuery query = taskService.createTaskQuery();
        if (StringUtils.isNotBlank(taskVo.getAssignee()))
        {
            query.taskAssignee(taskVo.getAssignee());
        }
        if (StringUtils.isNotBlank(taskVo.getName()))
        {
            query.taskNameLike("%" + taskVo.getName() + "%");
        }
        if (StringUtils.isNotBlank(taskVo.getProcessInstanceId()))
        {
            query.processInstanceId(taskVo.getProcessInstanceId());
        }
        query.orderByTaskCreateTime().desc();
        List<Task> tasks = query.list();
        List<TaskVo> taskVos = new ArrayList<>();
        for (Task task : tasks)
        {
            taskVos.add(convertToTaskVo(task));
        }
        return taskVos;
    }

    @Override
    public List<TaskVo> selectTodoTaskListByUserId(String userId, TaskVo taskVo)
    {
        TaskQuery query = taskService.createTaskQuery();
        query.taskCandidateOrAssigned(userId);
        if (StringUtils.isNotBlank(taskVo.getName()))
        {
            query.taskNameLike("%" + taskVo.getName() + "%");
        }
        if (StringUtils.isNotBlank(taskVo.getProcessDefinitionKey()))
        {
            query.processDefinitionKey(taskVo.getProcessDefinitionKey());
        }
        query.orderByTaskCreateTime().desc();
        List<Task> tasks = query.list();
        List<TaskVo> taskVos = new ArrayList<>();
        for (Task task : tasks)
        {
            taskVos.add(convertToTaskVo(task));
        }
        return taskVos;
    }

    @Override
    public List<TaskVo> selectDoneTaskList(TaskVo taskVo)
    {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .finished();
        if (StringUtils.isNotBlank(taskVo.getAssignee()))
        {
            query.taskAssignee(taskVo.getAssignee());
        }
        if (StringUtils.isNotBlank(taskVo.getName()))
        {
            query.taskNameLike("%" + taskVo.getName() + "%");
        }
        if (StringUtils.isNotBlank(taskVo.getProcessInstanceId()))
        {
            query.processInstanceId(taskVo.getProcessInstanceId());
        }
        query.orderByHistoricTaskInstanceEndTime().desc();
        List<HistoricTaskInstance> tasks = query.list();
        List<TaskVo> taskVos = new ArrayList<>();
        for (HistoricTaskInstance task : tasks)
        {
            taskVos.add(convertHistoricTaskToTaskVo(task));
        }
        return taskVos;
    }

    @Override
    public List<TaskVo> selectDoneTaskListByUserId(String userId, TaskVo taskVo)
    {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .taskAssignee(userId);
        if (StringUtils.isNotBlank(taskVo.getName()))
        {
            query.taskNameLike("%" + taskVo.getName() + "%");
        }
        if (StringUtils.isNotBlank(taskVo.getProcessDefinitionKey()))
        {
            query.processDefinitionKey(taskVo.getProcessDefinitionKey());
        }
        query.orderByHistoricTaskInstanceEndTime().desc();
        List<HistoricTaskInstance> tasks = query.list();
        List<TaskVo> taskVos = new ArrayList<>();
        for (HistoricTaskInstance task : tasks)
        {
            taskVos.add(convertHistoricTaskToTaskVo(task));
        }
        return taskVos;
    }

    @Override
    public List<TaskVo> selectMyProcessList(String userId, TaskVo taskVo)
    {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId);
        if (StringUtils.isNotBlank(taskVo.getProcessDefinitionKey()))
        {
            query.processDefinitionKey(taskVo.getProcessDefinitionKey());
        }
        if (taskVo.isEnded())
        {
            query.finished();
        }
        else
        {
            query.unfinished();
        }
        query.orderByProcessInstanceStartTime().desc();
        List<HistoricProcessInstance> instances = query.list();
        List<TaskVo> taskVos = new ArrayList<>();
        for (HistoricProcessInstance instance : instances)
        {
            TaskVo taskVoTemp = new TaskVo();
            taskVoTemp.setProcessInstanceId(instance.getId());
            taskVoTemp.setProcessDefinitionId(instance.getProcessDefinitionId());
            taskVoTemp.setProcessDefinitionName(instance.getProcessDefinitionName());
            taskVoTemp.setProcessDefinitionKey(instance.getProcessDefinitionKey());
            taskVoTemp.setStartUserId(instance.getStartUserId());
            taskVoTemp.setBusinessKey(instance.getBusinessKey());
            taskVoTemp.setCreateTime(instance.getStartTime());
            taskVos.add(taskVoTemp);
        }
        return taskVos;
    }

    @Override
    public TaskVo selectTaskById(String taskId)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null)
        {
            return convertToTaskVo(task);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean claimTask(String taskId, String userId)
    {
        taskService.claim(taskId, userId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unclaimTask(String taskId)
    {
        taskService.unclaim(taskId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(String taskId, Map<String, Object> variables)
    {
        if (variables != null && !variables.isEmpty())
        {
            taskService.complete(taskId, variables);
        }
        else
        {
            taskService.complete(taskId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delegateTask(String taskId, String userId)
    {
        taskService.delegateTask(taskId, userId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resolveTask(String taskId, Map<String, Object> variables)
    {
        if (variables != null && !variables.isEmpty())
        {
            taskService.resolveTask(taskId, variables);
        }
        else
        {
            taskService.resolveTask(taskId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setAssignee(String taskId, String userId)
    {
        taskService.setAssignee(taskId, userId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addComment(String taskId, String processInstanceId, String message)
    {
        taskService.addComment(taskId, processInstanceId, message);
        return true;
    }

    @Override
    public TaskVo convertToTaskVo(Task task)
    {
        TaskVo taskVo = new TaskVo();
        taskVo.setId(task.getId());
        taskVo.setName(task.getName());
        taskVo.setTaskDefinitionKey(task.getTaskDefinitionKey());
        taskVo.setProcessInstanceId(task.getProcessInstanceId());
        taskVo.setExecutionId(task.getExecutionId());
        taskVo.setProcessDefinitionId(task.getProcessDefinitionId());
        taskVo.setAssignee(task.getAssignee());
        taskVo.setOwner(task.getOwner());
        taskVo.setPriority(task.getPriority());
        taskVo.setCreateTime(task.getCreateTime());
        taskVo.setDueDate(task.getDueDate());
        taskVo.setDescription(task.getDescription());
        taskVo.setCategory(task.getCategory());
        taskVo.setFormKey(task.getFormKey());
        taskVo.setParentTaskId(task.getParentTaskId());
        taskVo.setTenantId(task.getTenantId());
        taskVo.setProcessVariables(task.getProcessVariables());
        taskVo.setTaskLocalVariables(task.getTaskLocalVariables());

        Map<String, Object> processVariables = task.getProcessVariables();
        if (processVariables != null)
        {
            taskVo.setBusinessKey((String) processVariables.get("businessKey"));
        }

        return taskVo;
    }

    private TaskVo convertHistoricTaskToTaskVo(HistoricTaskInstance task)
    {
        TaskVo taskVo = new TaskVo();
        taskVo.setId(task.getId());
        taskVo.setName(task.getName());
        taskVo.setTaskDefinitionKey(task.getTaskDefinitionKey());
        taskVo.setProcessInstanceId(task.getProcessInstanceId());
        taskVo.setExecutionId(task.getExecutionId());
        taskVo.setProcessDefinitionId(task.getProcessDefinitionId());
        taskVo.setAssignee(task.getAssignee());
        taskVo.setOwner(task.getOwner());
        taskVo.setPriority(task.getPriority());
        taskVo.setCreateTime(task.getStartTime());
        taskVo.setDueDate(task.getDueDate());
        taskVo.setDescription(task.getDescription());
        taskVo.setCategory(task.getCategory());
        taskVo.setFormKey(task.getFormKey());
        taskVo.setParentTaskId(task.getParentTaskId());
        taskVo.setTenantId(task.getTenantId());
        return taskVo;
    }
}
