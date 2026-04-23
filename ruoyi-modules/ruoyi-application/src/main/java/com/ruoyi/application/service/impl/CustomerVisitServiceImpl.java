package com.ruoyi.application.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.application.domain.CustomerVisit;
import com.ruoyi.application.mapper.CustomerVisitMapper;
import com.ruoyi.application.service.ICustomerVisitService;

@Service
public class CustomerVisitServiceImpl implements ICustomerVisitService
{
    @Autowired
    private CustomerVisitMapper customerVisitMapper;

    @Override
    public CustomerVisit selectCustomerVisitById(Long id)
    {
        return customerVisitMapper.selectCustomerVisitById(id);
    }

    @Override
    public List<CustomerVisit> selectCustomerVisitList(CustomerVisit customerVisit)
    {
        return customerVisitMapper.selectCustomerVisitList(customerVisit);
    }

    @Override
    public List<CustomerVisit> selectCustomerVisitByCustomerId(Long customerId)
    {
        return customerVisitMapper.selectCustomerVisitByCustomerId(customerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCustomerVisit(CustomerVisit customerVisit)
    {
        customerVisit.setCreateBy(SecurityUtils.getUsername());
        customerVisit.setCreateUser(SecurityUtils.getUserId());
        customerVisit.setUserId(SecurityUtils.getUserId());
        if (customerVisit.getVisitDate() == null)
        {
            customerVisit.setVisitDate(new Date());
        }
        return customerVisitMapper.insertCustomerVisit(customerVisit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomerVisit(CustomerVisit customerVisit)
    {
        customerVisit.setUpdateBy(SecurityUtils.getUsername());
        customerVisit.setUpdateUser(SecurityUtils.getUserId());
        return customerVisitMapper.updateCustomerVisit(customerVisit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCustomerVisitByIds(Long[] ids)
    {
        return customerVisitMapper.deleteCustomerVisitByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCustomerVisitById(Long id)
    {
        return customerVisitMapper.deleteCustomerVisitById(id);
    }
}
