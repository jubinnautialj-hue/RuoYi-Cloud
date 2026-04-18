package com.ruoyi.application.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

public class CustomerVisit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long customerId;

    private Long userId;

    @Excel(name = "回访内容")
    private String visitContent;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "回访时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date visitDate;

    @Excel(name = "客户反馈")
    private String feedback;

    private Long createUser;

    private Long updateUser;

    private String delFlag;

    private String userName;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getVisitContent()
    {
        return visitContent;
    }

    public void setVisitContent(String visitContent)
    {
        this.visitContent = visitContent;
    }

    public Date getVisitDate()
    {
        return visitDate;
    }

    public void setVisitDate(Date visitDate)
    {
        this.visitDate = visitDate;
    }

    public String getFeedback()
    {
        return feedback;
    }

    public void setFeedback(String feedback)
    {
        this.feedback = feedback;
    }

    public Long getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Long createUser)
    {
        this.createUser = createUser;
    }

    public Long getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }
}
