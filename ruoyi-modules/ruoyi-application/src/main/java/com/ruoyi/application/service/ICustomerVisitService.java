package com.ruoyi.application.service;

import java.util.List;
import com.ruoyi.application.domain.CustomerVisit;

public interface ICustomerVisitService
{
    public CustomerVisit selectCustomerVisitById(Long id);

    public List<CustomerVisit> selectCustomerVisitList(CustomerVisit customerVisit);

    public List<CustomerVisit> selectCustomerVisitByCustomerId(Long customerId);

    public int insertCustomerVisit(CustomerVisit customerVisit);

    public int updateCustomerVisit(CustomerVisit customerVisit);

    public int deleteCustomerVisitByIds(Long[] ids);

    public int deleteCustomerVisitById(Long id);
}
