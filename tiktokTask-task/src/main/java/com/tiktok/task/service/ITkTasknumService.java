package com.tiktok.task.service;

import java.util.List;
import com.tiktok.task.domain.TkTasknum;

/**
 * 任务数量Service接口
 * 
 * @author ruoyi
 * @date 2024-09-25
 */
public interface ITkTasknumService 
{
    /**
     * 查询任务数量
     * 
     * @param id 任务数量主键
     * @return 任务数量
     */
    public TkTasknum selectTkTasknumById(Long id);

    /**
     * 查询任务数量列表
     * 
     * @param tkTasknum 任务数量
     * @return 任务数量集合
     */
    public List<TkTasknum> selectTkTasknumList(TkTasknum tkTasknum);

    /**
     * 新增任务数量
     * 
     * @param tkTasknum 任务数量
     * @return 结果
     */
    public int insertTkTasknum(TkTasknum tkTasknum);

    /**
     * 修改任务数量
     * 
     * @param tkTasknum 任务数量
     * @return 结果
     */
    public int updateTkTasknum(TkTasknum tkTasknum);

    /**
     * 批量删除任务数量
     * 
     * @param ids 需要删除的任务数量主键集合
     * @return 结果
     */
    public int deleteTkTasknumByIds(Long[] ids);

    /**
     * 删除任务数量信息
     * 
     * @param id 任务数量主键
     * @return 结果
     */
    public int deleteTkTasknumById(Long id);
}
