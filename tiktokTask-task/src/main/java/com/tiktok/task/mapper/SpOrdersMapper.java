package com.tiktok.task.mapper;

import java.util.List;
import java.util.Map;

import com.tiktok.task.domain.SpOrders;
import com.tiktok.task.domain.ov.OrderProductOV;
import org.apache.ibatis.annotations.Param;

/**
 * 订单信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-27
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
    public List<OrderProductOV> getOrdersAndProductDetails(@Param("uid") Long uid,
                                                           @Param("status") String status,
                                                           @Param("orderId") String orderId);

    public List<Map<String, Object>> countOrdersByStatus(@Param("uid") Long uid);

}
