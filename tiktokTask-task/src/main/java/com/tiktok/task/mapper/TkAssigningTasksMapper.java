package com.tiktok.task.mapper;

import java.util.List;
import com.tiktok.task.domain.TkAssigningTasks;
import com.tiktok.task.domain.TkTasks;
import org.apache.ibatis.annotations.Param;

/**
 * 用户指定对应等级任务Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
public interface TkAssigningTasksMapper 
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
     * 删除用户指定对应等级任务
     * 
     * @param id 用户指定对应等级任务主键
     * @return 结果
     */
    public int deleteTkAssigningTasksById(Long id);

    /**
     * 批量删除用户指定对应等级任务
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTkAssigningTasksByIds(Long[] ids);

    public List<TkTasks> getUserTask(@Param("level") String taskLevel,@Param("uid") Long uid);
}
