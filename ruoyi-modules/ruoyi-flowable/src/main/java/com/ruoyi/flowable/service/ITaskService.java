package com.ruoyi.flowable.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.flowable.domain.TaskVo;

public interface ITaskService
{
    public List<TaskVo> selectTodoTaskList(TaskVo taskVo);

    public List<TaskVo> selectTodoTaskListByUserId(String userId, TaskVo taskVo);

    public List<TaskVo> selectDoneTaskList(TaskVo taskVo);

    public List<TaskVo> selectDoneTaskListByUserId(String userId, TaskVo taskVo);

    public List<TaskVo> selectMyProcessList(String userId, TaskVo taskVo);

    public TaskVo selectTaskById(String taskId);

    public boolean claimTask(String taskId, String userId);

    public boolean unclaimTask(String taskId);

    public boolean completeTask(String taskId, Map<String, Object> variables);

    public boolean delegateTask(String taskId, String userId);

    public boolean resolveTask(String taskId, Map<String, Object> variables);

    public boolean setAssignee(String taskId, String userId);

    public boolean addComment(String taskId, String processInstanceId, String message);

    public TaskVo convertToTaskVo(org.flowable.task.api.Task task);
}
