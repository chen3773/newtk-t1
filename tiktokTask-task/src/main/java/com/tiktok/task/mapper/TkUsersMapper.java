package com.tiktok.task.mapper;

import java.util.List;
import com.tiktok.task.domain.TkUsers;
import com.tiktok.task.domain.ov.OrderProductOV;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-20
 */
public interface TkUsersMapper 
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
     * 删除用户信息
     * 
     * @param uid 用户信息主键
     * @return 结果
     */
    public int deleteTkUsersByUid(Long uid);

    /**
     * 批量删除用户信息
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTkUsersByUids(Long[] uids);

    List<TkUsers> selectSubordinates(Long userId);

    public List<TkUsers> selectTkUsersBlurList(TkUsers tkUsers);

}
