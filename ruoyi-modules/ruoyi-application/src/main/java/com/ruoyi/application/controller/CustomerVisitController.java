package com.ruoyi.application.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.application.domain.CustomerVisit;
import com.ruoyi.application.service.ICustomerVisitService;

@RestController
@RequestMapping("/customer/visit")
public class CustomerVisitController extends BaseController
{
    @Autowired
    private ICustomerVisitService customerVisitService;

    @RequiresPermissions("app:visit:list")
    @GetMapping("/list")
    public TableDataInfo list(CustomerVisit customerVisit)
    {
        startPage();
        List<CustomerVisit> list = customerVisitService.selectCustomerVisitList(customerVisit);
        return getDataTable(list);
    }

    @RequiresPermissions("app:visit:list")
    @GetMapping("/list/{customerId}")
    public AjaxResult listByCustomerId(@PathVariable Long customerId)
    {
        List<CustomerVisit> list = customerVisitService.selectCustomerVisitByCustomerId(customerId);
        return AjaxResult.success(list);
    }

    @Log(title = "客户回访记录", businessType = BusinessType.EXPORT)
    @RequiresPermissions("app:visit:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerVisit customerVisit)
    {
        List<CustomerVisit> list = customerVisitService.selectCustomerVisitList(customerVisit);
        ExcelUtil<CustomerVisit> util = new ExcelUtil<CustomerVisit>(CustomerVisit.class);
        util.exportExcel(response, list, "客户回访记录数据");
    }

    @RequiresPermissions("app:visit:query")
    @GetMapping(value = { "/", "/{id}" })
    public AjaxResult getInfo(@PathVariable(value = "id", required = false) Long id)
    {
        AjaxResult ajax = AjaxResult.success();
        if (id != null)
        {
            CustomerVisit customerVisit = customerVisitService.selectCustomerVisitById(id);
            ajax.put(AjaxResult.DATA_TAG, customerVisit);
        }
        return ajax;
    }

    @RequiresPermissions("app:visit:add")
    @Log(title = "客户回访记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerVisit customerVisit)
    {
        return toAjax(customerVisitService.insertCustomerVisit(customerVisit));
    }

    @RequiresPermissions("app:visit:edit")
    @Log(title = "客户回访记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerVisit customerVisit)
    {
        return toAjax(customerVisitService.updateCustomerVisit(customerVisit));
    }

    @RequiresPermissions("app:visit:del")
    @Log(title = "客户回访记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(customerVisitService.deleteCustomerVisitByIds(ids));
    }
}
