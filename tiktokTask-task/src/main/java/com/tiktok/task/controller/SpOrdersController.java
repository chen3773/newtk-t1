package com.tiktok.task.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tiktok.common.annotation.Log;
import com.tiktok.common.core.controller.BaseController;
import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.enums.BusinessType;
import com.tiktok.task.domain.SpOrders;
import com.tiktok.task.service.ISpOrdersService;
import com.tiktok.common.utils.poi.ExcelUtil;
import com.tiktok.common.core.page.TableDataInfo;

/**
 * 订单信息Controller
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
@RestController
@RequestMapping("/task/orders")
public class SpOrdersController extends BaseController
{
    @Autowired
    private ISpOrdersService spOrdersService;

    /**
     * 查询订单信息列表
     */
    @PreAuthorize("@ss.hasPermi('task:orders:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpOrders spOrders)
    {
        startPage();
        List<SpOrders> list = spOrdersService.selectSpOrdersList(spOrders);
        return getDataTable(list);
    }

    /**
     * 导出订单信息列表
     */
    @PreAuthorize("@ss.hasPermi('task:orders:export')")
    @Log(title = "订单信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpOrders spOrders)
    {
        List<SpOrders> list = spOrdersService.selectSpOrdersList(spOrders);
        ExcelUtil<SpOrders> util = new ExcelUtil<SpOrders>(SpOrders.class);
        util.exportExcel(response, list, "订单信息数据");
    }

    /**
     * 获取订单信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:orders:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(spOrdersService.selectSpOrdersByOrderId(orderId));
    }

    /**
     * 新增订单信息
     */
    @PreAuthorize("@ss.hasPermi('task:orders:add')")
    @Log(title = "订单信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpOrders spOrders)
    {
        return toAjax(spOrdersService.insertSpOrders(spOrders));
    }

    /**
     * 修改订单信息
     */
    @PreAuthorize("@ss.hasPermi('task:orders:edit')")
    @Log(title = "订单信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpOrders spOrders)
    {
        return toAjax(spOrdersService.updateSpOrders(spOrders));
    }

    /**
     * 删除订单信息
     */
    @PreAuthorize("@ss.hasPermi('task:orders:remove')")
    @Log(title = "订单信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(spOrdersService.deleteSpOrdersByOrderIds(orderIds));
    }
}
