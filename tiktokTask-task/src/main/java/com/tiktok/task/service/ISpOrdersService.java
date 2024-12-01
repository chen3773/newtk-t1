package com.tiktok.task.service;

import java.util.List;
import java.util.Map;

import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.core.page.TableDataInfo;
import com.tiktok.task.domain.SpOrders;
import com.tiktok.task.domain.ov.OrderProductOV;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 订单信息Service接口
 * 
 * @author ruoyi
 * @date 2024-11-27
 */
public interface ISpOrdersService 
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
     * 批量删除订单信息
     * 
     * @param orderIds 需要删除的订单信息主键集合
     * @return 结果
     */
    public int deleteSpOrdersByOrderIds(Long[] orderIds);

    /**
     * 删除订单信息信息
     * 
     * @param orderId 订单信息主键
     * @return 结果
     */
    public int deleteSpOrdersByOrderId(Long orderId);

    public AjaxResult Buy(Map<String, Object> requestData);

    List<OrderProductOV>  orderList(String stateId,String orderId);

    List<Map<String, Object>> OrderStatus();
}
