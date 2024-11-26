package com.tiktok.task.service;

import java.util.List;

import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.framework.web.exception.CustomException;
import com.tiktok.task.domain.TaskData;
import com.tiktok.task.domain.TkUsers;
import com.tiktok.task.domain.TkWallettransactions;

/**
 * 用户信息Service接口
 * 
 * @author ruoyi
 * @date 2024-09-20
 */
public interface ITkUsersService 
{
    /**
     * 查询用户信息
     * 
     * @param uid 用户信息主键
     * @return 用户信息
     */
    public TkUsers selectTkUsersByUid(Long uid);

    /**
     * 查询用户信息列表
     * 
     * @param tkUsers 用户信息
     * @return 用户信息集合
     */
    public List<TkUsers> selectTkUsersList(TkUsers tkUsers);

    /**
     * 新增用户信息
     * 
     * @param tkUsers 用户信息
     * @return 结果
     */
    public int insertTkUsers(TkUsers tkUsers);

    /**
     * 修改用户信息
     * 
     * @param tkUsers 用户信息
     * @return 结果
     */
    public int updateTkUsers(TkUsers tkUsers);

    /**
     * 批量删除用户信息
     * 
     * @param uids 需要删除的用户信息主键集合
     * @return 结果
     */
    public int deleteTkUsersByUids(Long[] uids);

    /**
     * 删除用户信息信息
     * 
     * @param uid 用户信息主键
     * @return 结果
     */
    public int deleteTkUsersByUid(Long uid);

    List<TkWallettransactions> getWalletRecords(String category);

    AjaxResult updateUser(TkUsers tkUsers) throws CustomException;

    AjaxResult withdraw(String amount,String paymentPassword,String type);

    AjaxResult getUserUpgradeVIPMessage();

    AjaxResult GetFundComposition();

    AjaxResult addSpecialTask(TaskData taskData);

    int SignIn(TkUsers tkUsers);

    int LanguageSetting(String language);

    AjaxResult getLanguage();
}
