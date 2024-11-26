package com.tiktok.task.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.mapper.SpOrdersMapper;
import com.tiktok.task.domain.SpOrders;
import com.tiktok.task.service.ISpOrdersService;

/**
 * 订单信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
@Service
public class SpOrdersServiceImpl implements ISpOrdersService 
{
    @Autowired
    private SpOrdersMapper spOrdersMapper;

    /**
     * 查询订单信息
     * 
     * @param orderId 订单信息主键
     * @return 订单信息
     */
    @Override
    public SpOrders selectSpOrdersByOrderId(Long orderId)
    {
        return spOrdersMapper.selectSpOrdersByOrderId(orderId);
    }

    /**
     * 查询订单信息列表
     * 
     * @param spOrders 订单信息
     * @return 订单信息
     */
    @Override
    public List<SpOrders> selectSpOrdersList(SpOrders spOrders)
    {
        return spOrdersMapper.selectSpOrdersList(spOrders);
    }

    /**
     * 新增订单信息
     * 
     * @param spOrders 订单信息
     * @return 结果
     */
    @Override
    public int insertSpOrders(SpOrders spOrders)
    {
        return spOrdersMapper.insertSpOrders(spOrders);
    }

    /**
     * 修改订单信息
     * 
     * @param spOrders 订单信息
     * @return 结果
     */
    @Override
    public int updateSpOrders(SpOrders spOrders)
    {
        return spOrdersMapper.updateSpOrders(spOrders);
    }

    /**
     * 批量删除订单信息
     * 
     * @param orderIds 需要删除的订单信息主键
     * @return 结果
     */
    @Override
    public int deleteSpOrdersByOrderIds(Long[] orderIds)
    {
        return spOrdersMapper.deleteSpOrdersByOrderIds(orderIds);
    }

    /**
     * 删除订单信息信息
     * 
     * @param orderId 订单信息主键
     * @return 结果
     */
    @Override
    public int deleteSpOrdersByOrderId(Long orderId)
    {
        return spOrdersMapper.deleteSpOrdersByOrderId(orderId);
    }
}
