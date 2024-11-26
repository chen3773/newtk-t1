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
import com.tiktok.task.domain.SpProduct;
import com.tiktok.task.service.ISpProductService;
import com.tiktok.common.utils.poi.ExcelUtil;
import com.tiktok.common.core.page.TableDataInfo;

/**
 * 产品Controller
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
@RestController
@RequestMapping("/task/spproduct")
public class SpProductController extends BaseController
{
    @Autowired
    private ISpProductService spProductService;

    /**
     * 查询产品列表
     */
    @PreAuthorize("@ss.hasPermi('task:spproduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpProduct spProduct)
    {
        startPage();
        List<SpProduct> list = spProductService.selectSpProductList(spProduct);
        return getDataTable(list);
    }

    /**
     * 查询产品列表
     */
    @GetMapping("/ProductList")
    public TableDataInfo ProductList(SpProduct spProduct)
    {
        startPage();
        List<SpProduct> list = spProductService.selectSpProductList(spProduct);
        return getDataTable(list);
    }

    /**
     * 导出产品列表
     */
    @PreAuthorize("@ss.hasPermi('task:spproduct:export')")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpProduct spProduct)
    {
        List<SpProduct> list = spProductService.selectSpProductList(spProduct);
        ExcelUtil<SpProduct> util = new ExcelUtil<SpProduct>(SpProduct.class);
        util.exportExcel(response, list, "产品数据");
    }

    /**
     * 获取产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:spproduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(spProductService.selectSpProductById(id));
    }



    /**
     * 获取产品详细信息
     */
    @GetMapping("/getProductInfo")
    public AjaxResult getProductInfo(Long id)
    {
        return success(spProductService.selectSpProductById(id));
    }

    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('task:spproduct:add')")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpProduct spProduct)
    {
        return toAjax(spProductService.insertSpProduct(spProduct));
    }

    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('task:spproduct:edit')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpProduct spProduct)
    {
        return toAjax(spProductService.updateSpProduct(spProduct));
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('task:spproduct:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(spProductService.deleteSpProductByIds(ids));
    }
}
