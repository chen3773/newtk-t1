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
import com.tiktok.task.domain.TkAssigningTasks;
import com.tiktok.task.service.ITkAssigningTasksService;
import com.tiktok.common.utils.poi.ExcelUtil;
import com.tiktok.common.core.page.TableDataInfo;

/**
 * 用户指定对应等级任务Controller
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
@RestController
@RequestMapping("/task/userTasks")
public class TkAssigningTasksController extends BaseController
{
    @Autowired
    private ITkAssigningTasksService tkAssigningTasksService;

    /**
     * 查询用户指定对应等级任务列表
     */
    @PreAuthorize("@ss.hasPermi('task:userTasks:list')")
    @GetMapping("/list")
    public TableDataInfo list(TkAssigningTasks tkAssigningTasks)
    {
        startPage();
        List<TkAssigningTasks> list = tkAssigningTasksService.selectTkAssigningTasksList(tkAssigningTasks);
        return getDataTable(list);
    }

    /**
     * 导出用户指定对应等级任务列表
     */
    @PreAuthorize("@ss.hasPermi('task:userTasks:export')")
    @Log(title = "用户指定对应等级任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TkAssigningTasks tkAssigningTasks)
    {
        List<TkAssigningTasks> list = tkAssigningTasksService.selectTkAssigningTasksList(tkAssigningTasks);
        ExcelUtil<TkAssigningTasks> util = new ExcelUtil<TkAssigningTasks>(TkAssigningTasks.class);
        util.exportExcel(response, list, "用户指定对应等级任务数据");
    }

    /**
     * 获取用户指定对应等级任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:userTasks:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tkAssigningTasksService.selectTkAssigningTasksById(id));
    }

    /**
     * 新增用户指定对应等级任务
     */
    @PreAuthorize("@ss.hasPermi('task:userTasks:add')")
    @Log(title = "用户指定对应等级任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TkAssigningTasks tkAssigningTasks)
    {
        return toAjax(tkAssigningTasksService.insertTkAssigningTasks(tkAssigningTasks));
    }

    /**
     * 修改用户指定对应等级任务
     */
    @PreAuthorize("@ss.hasPermi('task:userTasks:edit')")
    @Log(title = "用户指定对应等级任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TkAssigningTasks tkAssigningTasks)
    {
        return toAjax(tkAssigningTasksService.updateTkAssigningTasks(tkAssigningTasks));
    }

    /**
     * 删除用户指定对应等级任务
     */
    @PreAuthorize("@ss.hasPermi('task:userTasks:remove')")
    @Log(title = "用户指定对应等级任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tkAssigningTasksService.deleteTkAssigningTasksByIds(ids));
    }

    /**
     * 用户获取指定的任务  指定等级
     */
    /**
     * 新增用户指定对应等级任务
     */
    @GetMapping("/getUserTask")
    public AjaxResult getUserTask(String taskLevel)
    {
        return AjaxResult.success(tkAssigningTasksService.getUserTask(taskLevel));
    }
}
