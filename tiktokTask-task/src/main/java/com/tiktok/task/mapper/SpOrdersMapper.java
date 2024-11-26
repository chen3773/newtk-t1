package com.tiktok.task.mapper;

import java.util.List;
import com.tiktok.task.domain.SpOrders;

/**
 * 订单信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
public interface SpOrdersMapper 
{
    /**
     * 查询订单信息
     * 
     * @param orderId 订单信息主键
     * @return 订单信息
     */
    public SpOrders selectSpOrdersByOrderId(Long orderId);

    /**
     * 查询订单信息列表
     * 
     * @param spOrders 订单信息
     * @return 订单信息集合
     */
    public List<SpOrders> selectSpOrdersList(SpOrders spOrders);

    /**
     * 新增订单信息
     * 
     * @param spOrders 订单信息
     * @return 结果
     */
    public int insertSpOrders(SpOrders spOrders);

    /**
     * 修改订单信息
     * 
     * @param spOrders 订单信息
     * @return 结果
     */
    public int updateSpOrders(SpOrders spOrders);

    /**
     * 删除订单信息
     * 
     * @param orderId 订单信息主键
     * @return 结果
     */
    public int deleteSpOrdersByOrderId(Long orderId);

    /**
     * 批量删除订单信息
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpOrdersByOrderIds(Long[] orderIds);
}
