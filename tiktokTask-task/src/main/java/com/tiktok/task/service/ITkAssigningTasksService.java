package com.tiktok.task.service;

import java.util.List;
import com.tiktok.task.domain.TkAssigningTasks;
import com.tiktok.task.domain.TkTasks;

/**
 * 用户指定对应等级任务Service接口
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
public interface ITkAssigningTasksService 
{
    /**
     * 查询用户指定对应等级任务
     * 
     * @param id 用户指定对应等级任务主键
     * @return 用户指定对应等级任务
     */
    public TkAssigningTasks selectTkAssigningTasksById(Long id);

    /**
     * 查询用户指定对应等级任务列表
     * 
     * @param tkAssigningTasks 用户指定对应等级任务
     * @return 用户指定对应等级任务集合
     */
    public List<TkAssigningTasks> selectTkAssigningTasksList(TkAssigningTasks tkAssigningTasks);

    /**
     * 新增用户指定对应等级任务
     * 
     * @param tkAssigningTasks 用户指定对应等级任务
     * @return 结果
     */
    public int insertTkAssigningTasks(TkAssigningTasks tkAssigningTasks);

    /**
     * 修改用户指定对应等级任务
     * 
     * @param tkAssigningTasks 用户指定对应等级任务
     * @return 结果
     */
    public int updateTkAssigningTasks(TkAssigningTasks tkAssigningTasks);

    /**
     * 批量删除用户指定对应等级任务
     * 
     * @param ids 需要删除的用户指定对应等级任务主键集合
     * @return 结果
     */
    public int deleteTkAssigningTasksByIds(Long[] ids);

    /**
     * 删除用户指定对应等级任务信息
     * 
     * @param id 用户指定对应等级任务主键
     * @return 结果
     */
    public int deleteTkAssigningTasksById(Long id);

    List<TkTasks> getUserTask(String taskLevel);
}
