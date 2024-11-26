package com.tiktok.task.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.tiktok.task.util.AssertionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
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
import com.tiktok.task.domain.TkTasks;
import com.tiktok.task.service.ITkTasksService;
import com.tiktok.common.utils.poi.ExcelUtil;
import com.tiktok.common.core.page.TableDataInfo;

/**
 * 任务列Controller
 * 
 * @author ruoyi
 * @date 2024-09-25
 */
@RestController
@RequestMapping("/task/tasks")
public class TkTasksController extends BaseController
{
    @Autowired
    private ITkTasksService tkTasksService;

    /**
     * 查询任务列列表
     */
    @PreAuthorize("@ss.hasPermi('task:tasks:list')")
    @GetMapping("/list")
    public TableDataInfo list(TkTasks tkTasks)
    {
        startPage();
        List<TkTasks> list = tkTasksService.selectTkTasksList(tkTasks);
        return getDataTable(list);
    }

    /**
     * 查询任务列列表---金额排序
     */
    @PreAuthorize("@ss.hasPermi('task:tasks:list')")
    @GetMapping("/Sortinglist")
    public TableDataInfo Sortinglist(TkTasks tkTasks)
    {
        startPage();
        List<TkTasks> list = tkTasksService.selectTkTasksList(tkTasks);
        List<TkTasks> sortedList = list.stream()
                .sorted(Comparator.comparing(TkTasks::getRewardAmount))
                .collect(Collectors.toList());

        return getDataTable(sortedList);
    }

    /**
     * 导出任务列列表
     */
    @PreAuthorize("@ss.hasPermi('task:tasks:export')")
    @Log(title = "任务列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TkTasks tkTasks)
    {
        List<TkTasks> list = tkTasksService.selectTkTasksList(tkTasks);
        ExcelUtil<TkTasks> util = new ExcelUtil<TkTasks>(TkTasks.class);
        util.exportExcel(response, list, "任务列数据");
    }

    /**
     * 获取任务列详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:tasks:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tkTasksService.selectTkTasksById(id));
    }

    /**
     * 新增任务列
     */
    @PreAuthorize("@ss.hasPermi('task:tasks:add')")
    @Log(title = "任务列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TkTasks tkTasks)
    {
        Integer surplusQuantity = tkTasks.getSurplusquantity();
        Long completedQuantity = tkTasks.getCompletedQuantity();
        Long totalQuantity = tkTasks.getTotalQuantity();

        Random random = new Random();

        long randomValue = (long) (random.nextDouble() * totalQuantity) + 1;

        if (surplusQuantity == 0) {
            tkTasks.setSurplusquantity((int) randomValue);
        }
        randomValue = (long) (random.nextDouble() * totalQuantity) + 1;
        if(completedQuantity == null){
            tkTasks.setCompletedQuantity(0L);
        }
        Assert.isTrue(tkTasks.getRewardAmount() != null && tkTasks.getRewardAmount().compareTo(BigDecimal.ONE) >= 0,
                "奖励金额不能为空或者小于1");
        Assert.isTrue(tkTasks.getCompletedQuantity()<=tkTasks.getTotalQuantity()&&
                tkTasks.getSurplusquantity()<=tkTasks.getTotalQuantity(),"任务数量错误");
        return toAjax(tkTasksService.insertTkTasks(tkTasks));
    }

    /**
     * 修改任务列
     */
    @PreAuthorize("@ss.hasPermi('task:tasks:edit')")
    @Log(title = "任务列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TkTasks tkTasks)
    {
        return toAjax(tkTasksService.updateTkTasks(tkTasks));
    }



    /**
     * 修改任务列
     * 批量修改
     */
    @PreAuthorize("@ss.hasPermi('task:tasks:edit')")
    @Log(title = "任务列", businessType = BusinessType.UPDATE)
    @PostMapping("/BatchChanges")
    public AjaxResult BatchChanges(@RequestBody Map<String, Object> requestData) {
        List<Long> idList = (List<Long>) requestData.get("idList");
        String rewardAmount = requestData.get("rewardAmount") != null ? requestData.get("rewardAmount").toString() : null;
        String title = (String) requestData.get("title");

        if (rewardAmount == null && title == null) {
            return toAjax(0);
        }

        return toAjax(tkTasksService.batchUpdateTasks(idList, rewardAmount, title));
    }




    /**
     * 删除任务列
     */
    @PreAuthorize("@ss.hasPermi('task:tasks:remove')")
    @Log(title = "任务列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tkTasksService.deleteTkTasksByIds(ids));
    }



    /**
     * 用户获取任务详情
     */

    @GetMapping("/getTaskId")
    public AjaxResult getTaskId(Long id)
    {
        return success(tkTasksService.selectTkTasksById(id));
    }

    /**
     * 获取对应等级任务
     */
    @GetMapping("/getTask")
    public AjaxResult getTask(Long taskLevel)
    {
        TkTasks tkTasks = new TkTasks();
        tkTasks.setTaskLevel(taskLevel);
        List<TkTasks> task = tkTasksService.getTask(tkTasks);
        return AjaxResult.success(task);
    }

    /**
     * 用户接取任务
     */
    @PostMapping("/receiveTask")
    public AjaxResult receiveTask(Long taskId){
        AssertionUtils.isTrue(taskId!=null,"Missing parameter");
       return tkTasksService.receiveTask(taskId);
    }

    /**
     * 获取我的任务
     */
    @GetMapping("/getTaskNum")
    public AjaxResult getTaskNum()
    {
        return tkTasksService.getTaskNum();
    }

    /**
     * 条件筛选我的任务
     */
    @GetMapping("/getUserTask")
    public AjaxResult getUserTask(String status)
    {
        return tkTasksService.getUserTask(status);
    }

    /**
     * 用户提交任务
     */

    @PostMapping("/submitTask")
    public AjaxResult submitTask(@RequestBody TkTasks tkTasks)
    {
        return tkTasksService.submitTask(tkTasks);
    }

    /**
     *
     */
    /**
     * 用户获取任务详情
     */

    @GetMapping("/getUserTaskById")
    public AjaxResult getUserTaskById(Long taskId)
    {
        return tkTasksService.getUserTaskById(taskId);
    }



}
