package com.tiktok.task.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.core.redis.RedisCache;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.task.domain.*;
import com.tiktok.task.domain.ov.UserTaskOV;
import com.tiktok.task.mapper.*;
import com.tiktok.task.service.ITkTaskAcceptancesService;
import com.tiktok.task.util.AssertionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.service.ITkTasksService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.tiktok.common.utils.PageUtils.startPage;
import static com.tiktok.task.util.parseTitle.iterateObjectFields;

/**
 * 任务列Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-25
 */
@Service
public class TkTasksServiceImpl implements ITkTasksService 
{
    @Autowired
    private TkTasksMapper tkTasksMapper;
    
    @Autowired
    private TkUsersMapper tkUsersMapper;
    @Autowired
    private TkTaskAcceptancesMapper tkTaskAcceptancesMapper;
    @Autowired
    private TkTasknumMapper tkTasknumMapper;
    @Autowired
    private TkSpecialTaskMapper tkSpecialTaskMapper;
    @Autowired
    private TkAutomaticAuditMapper tkAutomaticAuditMapper;
    @Autowired
    private ITkTaskAcceptancesService tkTaskAcceptancesService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询任务列
     * 
     * @param id 任务列主键
     * @return 任务列
     */
    @Override
    public TkTasks selectTkTasksById(Long id)
    {
        return tkTasksMapper.selectTkTasksById(id);
    }

    /**
     * 查询任务列列表
     * 
     * @param tkTasks 任务列
     * @return 任务列
     */
    @Override
    public List<TkTasks> selectTkTasksList(TkTasks tkTasks)
    {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        List<TkTasks> tkTasksList = tkTasksMapper.selectTkTasksList(tkTasks);
        if (uid != null) {
            // 从Redis中获取用户的语言设置
            String language =(String) redisCache.getCacheObject("user:language:" + uid);
        }
        for (int i = 0; i < tkTasksList.size(); i++) {
            tkTasksList.set(i,(TkTasks) iterateObjectFields(tkTasksList.get(i),"English"));
        }
        return tkTasksList;
    }

    /**
     * 新增任务列
     * 
     * @param tkTasks 任务列
     * @return 结果
     */
    @Override
    public int insertTkTasks(TkTasks tkTasks)
    {
        return tkTasksMapper.insertTkTasks(tkTasks);
    }

    /**
     * 修改任务列
     * 
     * @param tkTasks 任务列
     * @return 结果
     */
    @Override
    public int updateTkTasks(TkTasks tkTasks)
    {
        return tkTasksMapper.updateTkTasks(tkTasks);
    }

    /**
     * 批量删除任务列
     * 
     * @param ids 需要删除的任务列主键
     * @return 结果
     */
    @Override
    public int deleteTkTasksByIds(Long[] ids)
    {
        return tkTasksMapper.deleteTkTasksByIds(ids);
    }

    /**
     * 删除任务列信息
     * 
     * @param id 任务列主键
     * @return 结果
     */
    @Override
    public int deleteTkTasksById(Long id)
    {
        return tkTasksMapper.deleteTkTasksById(id);
    }

    /**
     * 客户端获取任务
     * @param tkTasks
     * @return
     */
    @Override
    public List<TkTasks> getTask(TkTasks tkTasks) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        long startTime = System.currentTimeMillis(); // 记录开始时间
        //用户领取过的过滤掉
        TkTaskAcceptances tkTaskAcceptances = new TkTaskAcceptances();
        tkTaskAcceptances.setUid(SecurityUtils.getLoginUser().getUser().getUid());
        List<TkTaskAcceptances> tkTaskAcceptances1 = tkTaskAcceptancesMapper.selectTkTaskAcceptancesList(tkTaskAcceptances);//领取任务表
        List<TkTasks> tkTasks1 = tkTasksMapper.selectRandomTkTasks(tkTasks);//返回的任务
        // 获取 tkTaskAcceptances1 中所有的 taskid
        List<Long> taskIdsToRemove = tkTaskAcceptances1.stream()
                .map(TkTaskAcceptances::getTaskId) // 假设 getTaskId() 是获取 taskid 的方法
                .collect(Collectors.toList());

        // 从 tkTasks1 中移除所有匹配的任务
        List<TkTasks> filteredTasks = tkTasks1.stream()
                .filter(task -> !taskIdsToRemove.contains(task.getId())) // 假设 getId() 是获取任务 id 的方法
                .collect(Collectors.toList());

        List<TkTasks> finalFilteredTasks = filteredTasks.stream()
                .filter(task -> task.getSurplusquantity() > 0) // 假设 getSurplusquantity() 是获取剩余数量的方法
                .collect(Collectors.toList());
        String language = "Chinese";
        if (uid != null) {
            // 从Redis中获取用户的语言设置
            language = (String) redisCache.getCacheObject("user:language:" + uid);
        }

        for (int i = 0; i < finalFilteredTasks.size(); i++) {
            finalFilteredTasks.set(i,(TkTasks) iterateObjectFields(finalFilteredTasks.get(i),language));
        }

        return finalFilteredTasks;

    }

    /**
     * 客户端接收任务
     */
    @Override
    @Transactional
    public AjaxResult receiveTask(Long taskId)  {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        //判断用户是否有未完成的任务
        TkTaskAcceptances tkTaskAcceptancesdDm = new TkTaskAcceptances();
        tkTaskAcceptancesdDm.setUid(uid);
        tkTaskAcceptancesdDm.setStatus("1");
        List<TkTaskAcceptances> tkTaskAcceptances3= tkTaskAcceptancesMapper.selectTkTaskAcceptancesList(tkTaskAcceptancesdDm);
        tkTaskAcceptancesdDm.setStatus("4");
        List<TkTaskAcceptances> tkTaskAcceptances5= tkTaskAcceptancesMapper.selectTkTaskAcceptancesList(tkTaskAcceptancesdDm);
        tkTaskAcceptancesdDm.setStatus("0");
        List<TkTaskAcceptances> tkTaskAcceptances4= tkTaskAcceptancesMapper.selectTkTaskAcceptancesList(tkTaskAcceptancesdDm);

        AssertionUtils.isTrue(tkTaskAcceptances3.size()==0,"You have a task to review, please wait patiently for approval.");
        AssertionUtils.isTrue(tkTaskAcceptances5.size()==0,"You have a task to review, please wait patiently for approval.");
        AssertionUtils.isTrue(tkTaskAcceptances4.size()==0,"There's still work to be done");
        //只允许接一次
        TkTaskAcceptances tkTaskAcceptances1 = new TkTaskAcceptances();
        tkTaskAcceptances1.setTaskId(taskId);
        tkTaskAcceptances1.setUid(uid);
        List<TkTaskAcceptances> tkTaskAcceptances2 = tkTaskAcceptancesMapper.selectTkTaskAcceptancesList(tkTaskAcceptances1);
        AssertionUtils.isTrue(tkTaskAcceptances2.size()==0,"Cannot be claimed again");

        TkTasks tkTasks = new TkTasks();
        tkTasks.setId(taskId);
        TkTasks TaskList = tkTasksMapper.selectTkTasksList(tkTasks).get(0);

        AssertionUtils.isTrue(TaskList.getDeleted().equals("0"),"Network error");
        TkUsers tkUsers = tkUsersMapper.selectTkUsersByUid(uid);
        //等级不符合
        AssertionUtils.isTrue(TaskList.getTaskLevel()<=tkUsers.getSvipLevel(),"Level not met");

        //判断数量
        AssertionUtils.isTrue(TaskList.getSurplusquantity()>0,"Quota is full");

        //判断用户是否还有任务额度
        TkTasknum tkTasknum = new TkTasknum();
        tkTasknum.setUserId(uid);
        TkTasknum tkTasknum1 = tkTasknumMapper.selectTkTasknumList(tkTasknum).get(0);
        //判断数量
        if(TaskList.getTaskLevel()==0){
            AssertionUtils.isTrue(tkTasknum1.getExperienceTaskCount()>0,"Insufficient quota");
            tkTasknum1.setExperienceTaskCount(tkTasknum1.getExperienceTaskCount()-1);
        }else if(TaskList.getTaskLevel()>0 && TaskList.getTaskLevel()<=5){
            AssertionUtils.isTrue(tkTasknum1.getNormalTaskCount()>0,"Insufficient quota");
            tkTasknum1.setNormalTaskCount(tkTasknum1.getNormalTaskCount()-1);
        }else if(TaskList.getTaskLevel()==6){
            AssertionUtils.isTrue(tkTasknum1.getHiddenTaskCount()>0,"Insufficient quota");
            tkTasknum1.setHiddenTaskCount(tkTasknum1.getHiddenTaskCount()-1);
        }

        AssertionUtils.isTrue( tkTasknumMapper.updateTkTasknum(tkTasknum1)>0,"error");

        //接入任务
        TkTaskAcceptances tkTaskAcceptances = new TkTaskAcceptances();
        tkTaskAcceptances.setTaskId(taskId);
        tkTaskAcceptances.setStatus("0");
        tkTaskAcceptances.setUid(uid);
        tkTaskAcceptances.setTips("1");
        tkTaskAcceptances.setCreateTime(new Date());
        //减少一个任务数量
        tkTasks.setSurplusquantity(TaskList.getSurplusquantity()-1);
        int i = tkTasksMapper.updateTkTasks(tkTasks);
        //判断数量
        AssertionUtils.isTrue(i!=0,"error");
        //减少一个任务数
        int i1 = tkTaskAcceptancesMapper.insertTkTaskAcceptances(tkTaskAcceptances);
        AssertionUtils.isTrue(i1!=0,"error");
        return AjaxResult.success("success");
    }

    @Override
    public AjaxResult getTaskNum() {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        List<Map<String, Object>> taskNum = tkTasksMapper.getTaskNum(uid);
        Integer count = Integer.parseInt(taskNum.get(1).get("count").toString());
        Integer count2 = Integer.parseInt(taskNum.get(4).get("count").toString());
        Integer count3 = count + count2;
        taskNum.get(1).put("count",count3);
        taskNum.remove(taskNum.size()-1);
        return AjaxResult.success(taskNum);
    }

    @Override
    public AjaxResult getUserTask(String status) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        List<UserTaskOV> userTaskList =  tkTasksMapper.getUserTask(status, uid);

        List<UserTaskOV> userTaskList2 =  tkTasksMapper.getUserTask("4", uid);
        userTaskList.addAll(userTaskList2);


        return AjaxResult.success(userTaskList);
    }

    @Override
    @Transactional
    public AjaxResult submitTask(TkTasks tkTasks) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        Long id = tkTasks.getId();
        String image = tkTasks.getImage();

        Assert.isTrue(id!=null,"error");
        Assert.isTrue(image!=null,"error");



        TkTaskAcceptances tkTaskAcceptances1 = new TkTaskAcceptances();
        tkTaskAcceptances1.setUid(uid);
        tkTaskAcceptances1.setTaskId(id);
        TkTaskAcceptances tkTaskAcceptances = tkTaskAcceptancesMapper.selectTkTaskAcceptancesList(tkTaskAcceptances1).get(0);

        Assert.isTrue(tkTaskAcceptances.getStatus().equals("0"),"error");
        Assert.isTrue(Objects.equals(tkTaskAcceptances.getUid(), uid),"error");





        //判断是否为自动审核任务
        TkAutomaticAudit tkAutomaticAudit = tkAutomaticAuditMapper.selectTkAutomaticAuditById(1L);
        TkTasks tkTasks1 = tkTasksMapper.selectTkTasksById(tkTasks.getId());
        Long taskLevel = tkTasks1.getTaskLevel();


        tkTaskAcceptances.setStatus("1");
        tkTaskAcceptances.setSubmittedImage(image);
        tkTaskAcceptances.setSubmissionTime(new Date());

                //判断是否触发特殊任务
        long specialTaskId = 0;

        TkSpecialTask tkSpecialTask = new TkSpecialTask();
        tkSpecialTask.setUserId(uid);
        List<TkSpecialTask> tkSpecialTasks = tkSpecialTaskMapper.selectTkSpecialTaskList(tkSpecialTask);
        if(tkSpecialTasks.size()!=0){
            for (int i1 = 0; i1 < tkSpecialTasks.size(); i1++) {
                if(tkSpecialTasks.get(i1).getStatus().equals("0") && tkSpecialTasks.get(i1).getTriggerCount()>0){
                    long l = tkSpecialTasks.get(i1).getTriggerCount() - 1;
                    tkSpecialTasks.get(i1).setTriggerCount(l);
                    tkSpecialTaskMapper.updateTkSpecialTask(tkSpecialTasks.get(i1));
                    if(l==0){
                        long taskId = tkSpecialTasks.get(i1).getTaskId();
                        //触发隐藏任务
                        //接入任务
                        TkTaskAcceptances newtkTaskAcceptances = new TkTaskAcceptances();
                        newtkTaskAcceptances.setTaskId(taskId);
                        newtkTaskAcceptances.setStatus("0");
                        newtkTaskAcceptances.setUid(uid);
                        newtkTaskAcceptances.setCreateTime(new Date());
                        tkTaskAcceptancesMapper.insertTkTaskAcceptances(newtkTaskAcceptances);
                        tkSpecialTasks.get(i1).setStatus("1");
                        specialTaskId = taskId;
                    }
                    tkSpecialTaskMapper.updateTkSpecialTask(tkSpecialTasks.get(i1));
                }
            }
        }
        tkTaskAcceptances.setTips("0");
        int i = tkTaskAcceptancesMapper.updateTkTaskAcceptances(tkTaskAcceptances);
        Assert.isTrue(i!=0,"error");

        switch (taskLevel.intValue()) {
            case 0:
                if (tkAutomaticAudit.getLv0() == 0) {
                    tkTaskAcceptancesService.TaskAudit(tkTaskAcceptances.getId(),true);
                    tkTaskAcceptances.setTips("1");
                }
                break;
            case 1:
                if (tkAutomaticAudit.getLv1() == 0) {
                    tkTaskAcceptancesService.TaskAudit(tkTaskAcceptances.getId(),true);
                    tkTaskAcceptances.setTips("1");
                }
                break;
            case 2:
                if (tkAutomaticAudit.getLv2() == 0) {
                    tkTaskAcceptancesService.TaskAudit(tkTaskAcceptances.getId(),true);
                    tkTaskAcceptances.setTips("1");
                }
                break;
            case 3:
                if (tkAutomaticAudit.getLv3() == 0) {
                    // 执行自动审核任务
                    tkTaskAcceptancesService.TaskAudit(tkTaskAcceptances.getId(),true);
                    tkTaskAcceptances.setTips("1");
                }
                break;
            case 4:
                if (tkAutomaticAudit.getLv4() == 0) {
                    // 执行自动审核任务
                    tkTaskAcceptancesService.TaskAudit(tkTaskAcceptances.getId(),true);
                    tkTaskAcceptances.setTips("1");
                }
                break;
            case 5:
                if (tkAutomaticAudit.getLv5() == 0) {
                    // 执行自动审核任务
                    tkTaskAcceptancesService.TaskAudit(tkTaskAcceptances.getId(),true);
                    tkTaskAcceptances.setTips("1");
                }
                break;
        }



        AjaxResult ajax = AjaxResult.success();
        if(specialTaskId!=0){
            ajax.put("specialTaskId",specialTaskId);
        }

        return ajax;
    }

    @Override
    public AjaxResult getUserTaskById(Long taskId) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();

        List<UserTaskOV> userTaskById = tkTasksMapper.getUserTaskById(taskId, uid);
        for (int i = 0; i < userTaskById.size(); i++) {
            if(userTaskById.get(i).getTkTaskAcceptances().getStatus().equals("3")){
                userTaskById.get(i).getTkTasks().setLink("Task failure");
            }
            if(userTaskById.get(i).getTkTaskAcceptances().getStatus().equals("2")){
                userTaskById.get(i).getTkTasks().setLink("Mission completed");
            }
        }

        return AjaxResult.success(userTaskById);
    }

    @Override
    public int batchUpdateTasks(List<Long> idList, String rewardAmount, String title) {
        tkTasksMapper.batchUpdateTasks(idList, rewardAmount, title);
        return 1;

    }


}
