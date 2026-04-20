package com.ruoyi.system.api.dto;

import com.ruoyi.common.core.web.domain.BaseDTO;

public class UserQueryDTO extends BaseDTO
{
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long deptId;

    private String userName;

    private String nickName;

    private String email;

    private String phonenumber;

    private String status;

    private String delFlag;

    private Long[] userIds;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public Long[] getUserIds()
    {
        return userIds;
    }

    public void setUserIds(Long[] userIds)
    {
        this.userIds = userIds;
    }
}
