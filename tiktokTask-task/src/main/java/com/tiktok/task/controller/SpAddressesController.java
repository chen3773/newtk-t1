package com.tiktok.task.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.tiktok.common.utils.SecurityUtils;
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
import com.tiktok.task.domain.SpAddresses;
import com.tiktok.task.service.ISpAddressesService;
import com.tiktok.common.utils.poi.ExcelUtil;
import com.tiktok.common.core.page.TableDataInfo;

/**
 * 地址Controller
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
@RestController
@RequestMapping("/task/userAddresses")
public class SpAddressesController extends BaseController
{
    @Autowired
    private ISpAddressesService spAddressesService;

    /**
     * 查询地址列表
     */
    @PreAuthorize("@ss.hasPermi('task:userAddresses:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpAddresses spAddresses)
    {
        startPage();
        List<SpAddresses> list = spAddressesService.selectSpAddressesList(spAddresses);
        return getDataTable(list);
    }

    /**
     * 查询地址列表
     */
    @GetMapping("/Addresses")
    public TableDataInfo Addresses(SpAddresses spAddresses)
    {
        startPage();
        List<SpAddresses> list = spAddressesService.Addresses(spAddresses);
        return getDataTable(list);
    }

    /**
     * 导出地址列表
     */
    @PreAuthorize("@ss.hasPermi('task:userAddresses:export')")
    @Log(title = "地址", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpAddresses spAddresses)
    {
        List<SpAddresses> list = spAddressesService.selectSpAddressesList(spAddresses);
        ExcelUtil<SpAddresses> util = new ExcelUtil<SpAddresses>(SpAddresses.class);
        util.exportExcel(response, list, "地址数据");
    }

    /**
     * 获取地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:userAddresses:query')")
    @GetMapping(value = "/{addressId}")
    public AjaxResult getInfo(@PathVariable("addressId") Long addressId)
    {
        return success(spAddressesService.selectSpAddressesByAddressId(addressId));
    }

    /**
     * 新增地址
     */
    @PreAuthorize("@ss.hasPermi('task:userAddresses:add')")
    @Log(title = "地址", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpAddresses spAddresses)
    {
        return toAjax(spAddressesService.insertSpAddresses(spAddresses));
    }
    /**
     * 新增地址
     */
    @Log(title = "地址", businessType = BusinessType.INSERT)
    @PostMapping("/addAddresses")
    public AjaxResult addAddresses(@RequestBody SpAddresses spAddresses)
    {
        spAddresses.setUid(SecurityUtils.getLoginUser().getUser().getUid());
        return toAjax(spAddressesService.addAddresses(spAddresses));
    }



    /**
     * 修改地址
     */
    @PreAuthorize("@ss.hasPermi('task:userAddresses:edit')")
    @Log(title = "地址", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpAddresses spAddresses)
    {
        return toAjax(spAddressesService.updateSpAddresses(spAddresses));
    }

    /**
     * 修改地址
     */
    @PostMapping("editAddresses")
    public AjaxResult editAddresses(@RequestBody SpAddresses spAddresses)
    {
        return toAjax(spAddressesService.editAddresses(spAddresses));
    }


    /**
     * 删除地址
     */
    @PreAuthorize("@ss.hasPermi('task:userAddresses:remove')")
    @Log(title = "地址", businessType = BusinessType.DELETE)
	@DeleteMapping("/{addressIds}")
    public AjaxResult remove(@PathVariable Long[] addressIds)
    {
        return toAjax(spAddressesService.deleteSpAddressesByAddressIds(addressIds));
    }


    /**
     * 删除地址
     */
    @PostMapping("/removeAddress")
    public AjaxResult removeAddress(Long[] addressIds)
    {
        return toAjax(spAddressesService.deleteSpAddressesByAddressIds(addressIds));
    }
}
