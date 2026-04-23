package com.ruoyi.flowable.domain;

import java.util.Date;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

public class ProcessInstanceVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String id;

    @Excel(name = "流程实例名称")
    private String name;

    @Excel(name = "流程实例标识")
    private String businessKey;

    @Excel(name = "流程定义ID")
    private String processDefinitionId;

    @Excel(name = "流程定义名称")
    private String processDefinitionName;

    @Excel(name = "流程定义标识")
    private String processDefinitionKey;

    private Integer processDefinitionVersion;

    @Excel(name = "流程发起人")
    private String startUserId;

    @Excel(name = "流程发起人名称")
    private String startUserName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Excel(name = "执行时长")
    private Long duration;

    @Excel(name = "流程状态")
    private String status;

    private String activityId;

    private String superProcessInstanceId;

    private String parentId;

    private String rootProcessInstanceId;

    private String tenantId;

    private String nameItems;

    private String localizedName;

    private String localizedDescription;

    private boolean suspended;

    private boolean ended;

    private String deleteReason;

    private Map<String, Object> processVariables;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBusinessKey()
    {
        return businessKey;
    }

    public void setBusinessKey(String businessKey)
    {
        this.businessKey = businessKey;
    }

    public String getProcessDefinitionId()
    {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId)
    {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionName()
    {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName)
    {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessDefinitionKey()
    {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey)
    {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Integer getProcessDefinitionVersion()
    {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion)
    {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getStartUserId()
    {
        return startUserId;
    }

    public void setStartUserId(String startUserId)
    {
        this.startUserId = startUserId;
    }

    public String getStartUserName()
    {
        return startUserName;
    }

    public void setStartUserName(String startUserName)
    {
        this.startUserName = startUserName;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Long getDuration()
    {
        return duration;
    }

    public void setDuration(Long duration)
    {
        this.duration = duration;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getActivityId()
    {
        return activityId;
    }

    public void setActivityId(String activityId)
    {
        this.activityId = activityId;
    }

    public String getSuperProcessInstanceId()
    {
        return superProcessInstanceId;
    }

    public void setSuperProcessInstanceId(String superProcessInstanceId)
    {
        this.superProcessInstanceId = superProcessInstanceId;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getRootProcessInstanceId()
    {
        return rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(String rootProcessInstanceId)
    {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    public String getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }

    public String getNameItems()
    {
        return nameItems;
    }

    public void setNameItems(String nameItems)
    {
        this.nameItems = nameItems;
    }

    public String getLocalizedName()
    {
        return localizedName;
    }

    public void setLocalizedName(String localizedName)
    {
        this.localizedName = localizedName;
    }

    public String getLocalizedDescription()
    {
        return localizedDescription;
    }

    public void setLocalizedDescription(String localizedDescription)
    {
        this.localizedDescription = localizedDescription;
    }

    public boolean isSuspended()
    {
        return suspended;
    }

    public void setSuspended(boolean suspended)
    {
        this.suspended = suspended;
    }

    public boolean isEnded()
    {
        return ended;
    }

    public void setEnded(boolean ended)
    {
        this.ended = ended;
    }

    public String getDeleteReason()
    {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason)
    {
        this.deleteReason = deleteReason;
    }

    public Map<String, Object> getProcessVariables()
    {
        return processVariables;
    }

    public void setProcessVariables(Map<String, Object> processVariables)
    {
        this.processVariables = processVariables;
    }
}
