package com.tiktok.task.service.impl;

import java.util.List;

import com.tiktok.common.core.redis.RedisCache;
import com.tiktok.common.utils.DateUtils;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.task.domain.TkTasks;
import com.tiktok.task.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.mapper.TkAssigningTasksMapper;
import com.tiktok.task.domain.TkAssigningTasks;
import com.tiktok.task.service.ITkAssigningTasksService;

/**
 * 用户指定对应等级任务Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
@Service
public class TkAssigningTasksServiceImpl implements ITkAssigningTasksService 
{
    @Autowired
    private TkAssigningTasksMapper tkAssigningTasksMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 查询用户指定对应等级任务
     * 
     * @param id 用户指定对应等级任务主键
     * @return 用户指定对应等级任务
     */
    @Override
    public TkAssigningTasks selectTkAssigningTasksById(Long id)
    {
        return tkAssigningTasksMapper.selectTkAssigningTasksById(id);
    }

    /**
     * 查询用户指定对应等级任务列表
     * 
     * @param tkAssigningTasks 用户指定对应等级任务
     * @return 用户指定对应等级任务
     */
    @Override
    public List<TkAssigningTasks> selectTkAssigningTasksList(TkAssigningTasks tkAssigningTasks)
    {
        return tkAssigningTasksMapper.selectTkAssigningTasksList(tkAssigningTasks);
    }

    /**
     * 新增用户指定对应等级任务
     * 
     * @param tkAssigningTasks 用户指定对应等级任务
     * @return 结果
     */
    @Override
    public int insertTkAssigningTasks(TkAssigningTasks tkAssigningTasks)
    {
        tkAssigningTasks.setCreateTime(DateUtils.getNowDate());
        return tkAssigningTasksMapper.insertTkAssigningTasks(tkAssigningTasks);
    }

    /**
     * 修改用户指定对应等级任务
     * 
     * @param tkAssigningTasks 用户指定对应等级任务
     * @return 结果
     */
    @Override
    public int updateTkAssigningTasks(TkAssigningTasks tkAssigningTasks)
    {
        tkAssigningTasks.setUpdateTime(DateUtils.getNowDate());
        return tkAssigningTasksMapper.updateTkAssigningTasks(tkAssigningTasks);
    }

    /**
     * 批量删除用户指定对应等级任务
     * 
     * @param ids 需要删除的用户指定对应等级任务主键
     * @return 结果
     */
    @Override
    public int deleteTkAssigningTasksByIds(Long[] ids)
    {
        return tkAssigningTasksMapper.deleteTkAssigningTasksByIds(ids);
    }

    /**
     * 删除用户指定对应等级任务信息
     * 
     * @param id 用户指定对应等级任务主键
     * @return 结果
     */
    @Override
    public int deleteTkAssigningTasksById(Long id)
    {
        return tkAssigningTasksMapper.deleteTkAssigningTasksById(id);
    }

    @Override
    public  List<TkTasks> getUserTask(String taskLevel) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        List<TkTasks> userTask = tkAssigningTasksMapper.getUserTask(taskLevel, uid);
        return LanguageUtil.processListWithLanguageSettingList(uid, userTask, redisCache);
    }
}
