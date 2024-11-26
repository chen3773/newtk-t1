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
import com.tiktok.task.domain.SpGroups;
import com.tiktok.task.service.ISpGroupsService;
import com.tiktok.common.utils.poi.ExcelUtil;
import com.tiktok.common.core.page.TableDataInfo;

/**
 * 商品分类Controller
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
@RestController
@RequestMapping("/task/groups")
public class SpGroupsController extends BaseController
{
    @Autowired
    private ISpGroupsService spGroupsService;

    /**
     * 查询商品分类列表
     */
    @PreAuthorize("@ss.hasPermi('task:groups:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpGroups spGroups)
    {
        startPage();
        List<SpGroups> list = spGroupsService.selectSpGroupsList(spGroups);
        return getDataTable(list);
    }

    @GetMapping("/GroupsList")
    public TableDataInfo GroupsList(SpGroups spGroups)
    {
        startPage();
        spGroups.setStatus("0");
        List<SpGroups> list = spGroupsService.selectSpGroupsList(spGroups);
        return getDataTable(list);
    }


    /**
     * 导出商品分类列表
     */
    @PreAuthorize("@ss.hasPermi('task:groups:export')")
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpGroups spGroups)
    {
        List<SpGroups> list = spGroupsService.selectSpGroupsList(spGroups);
        ExcelUtil<SpGroups> util = new ExcelUtil<SpGroups>(SpGroups.class);
        util.exportExcel(response, list, "商品分类数据");
    }

    /**
     * 获取商品分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:groups:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(spGroupsService.selectSpGroupsById(id));
    }

    /**
     * 新增商品分类
     */
    @PreAuthorize("@ss.hasPermi('task:groups:add')")
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpGroups spGroups)
    {
        return toAjax(spGroupsService.insertSpGroups(spGroups));
    }

    /**
     * 修改商品分类
     */
    @PreAuthorize("@ss.hasPermi('task:groups:edit')")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpGroups spGroups)
    {
        return toAjax(spGroupsService.updateSpGroups(spGroups));
    }

    /**
     * 删除商品分类
     */
    @PreAuthorize("@ss.hasPermi('task:groups:remove')")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(spGroupsService.deleteSpGroupsByIds(ids));
    }
}
