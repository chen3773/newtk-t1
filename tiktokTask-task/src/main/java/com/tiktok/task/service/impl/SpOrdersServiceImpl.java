package com.tiktok.task.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.core.page.TableDataInfo;
import com.tiktok.common.core.redis.RedisCache;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.task.domain.*;
import com.tiktok.task.domain.ov.OrderProductOV;
import com.tiktok.task.mapper.*;
import com.tiktok.task.util.LanguageUtil;
import com.tiktok.task.util.OrderNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.service.ISpOrdersService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.tiktok.common.utils.ParseTitle.iterateObjectFields;

/**
 * 订单信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-27
 */
@Service
public class SpOrdersServiceImpl implements ISpOrdersService 
{
    @Autowired
    private SpOrdersMapper spOrdersMapper;
    @Autowired
    private TkUsersMapper tkUsersMapper;
    @Autowired
    private SpProductMapper spProductMapper;
    @Autowired
    private SpAddressesMapper spAddressesMapper;
    @Autowired
    private TkWallettransactionsMapper tkWallettransactionsMapper;
    @Autowired
    private RedisCache redisCache;
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
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        List<SpOrders> spOrders1 = spOrdersMapper.selectSpOrdersList(spOrders);
        return LanguageUtil.processListWithLanguageSettingList(uid,spOrders1,redisCache);
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

    @Override
    @Transactional
    public AjaxResult Buy(Map<String, Object> requestData) {
        Long productId = Long.valueOf(requestData.get("productId").toString());
        String paymentPassword = (String) requestData.get("paymentPassword");
        BigDecimal quantity = new BigDecimal(requestData.get("quantity").toString());
        Long addressId = Long.valueOf( requestData.get("addressId").toString());
        String errorMessage = "{ \"Chinese\": \"異常\",   \"English\": \"error\" }";

        Long uid = SecurityUtils.getLoginUser().getUser().getUid();//当前登录用户
        TkUsers tkUsers = tkUsersMapper.selectTkUsersByUid(uid);//获取出来的用户信息
        SpProduct spProduct = spProductMapper.selectSpProductById(productId);
        SpAddresses spAddresses = spAddressesMapper.selectSpAddressesByAddressId(addressId);

        Assert.isTrue(spAddresses!=null,errorMessage);
        Assert.isTrue(tkUsers!=null,errorMessage);
        Assert.isTrue(spProduct!=null,errorMessage);
        Assert.isTrue(tkUsers.getPaymentPassword()!=null,"{ \"Chinese\": \"設置支付密碼。\",   \"English\": \"Set a payment password.\" }");
        Assert.isTrue(tkUsers.getPaymentPassword().trim().equals(paymentPassword.trim()),"{ \"Chinese\": \"支付密碼錯誤\",   \"English\": \"Payment password error\" }");

        //判断库存是否足够
        BigDecimal productInventory = new BigDecimal(spProduct.getProductInventory());//库存数量
        Assert.isTrue(productInventory.compareTo(quantity) >= 0,errorMessage);

        //判断余额是否足够
        BigDecimal balance = new BigDecimal(tkUsers.getBalance());//余额
        BigDecimal price = quantity.multiply(spProduct.getProductPrice());
        Assert.isTrue(balance.compareTo(price) >= 0,"{ \"Chinese\": \"餘額不足\",   \"English\": \"Insufficient balance\" }");


        String jsonString = JSON.toJSONString(spAddresses);

        String orderNumber = OrderNumberGenerator.generateUSOrderNumber();//订单号
        //开始创建订单
        SpOrders spOrders = new SpOrders();
        spOrders.setUid(uid);
        spOrders.setProductId(productId);
        spOrders.setQuantity(quantity.longValue());
        spOrders.setOrderDate(new Date());
        spOrders.setTotalPrice(price);
        spOrders.setStatus("1");
        spOrders.setShippingAddress(jsonString);
        spOrders.setShippingStatus("0");
        spOrders.setPaymentMethod("0");
        spOrders.setPaymentStatus("0");
        spOrders.setCreatedAt(new Date());
        spOrders.setOrderNumber(orderNumber);
        int i = spOrdersMapper.insertSpOrders(spOrders);
        Assert.isTrue(i>0,errorMessage);

        //扣除余额
        String newBalance = new BigDecimal(tkUsers.getBalance()).subtract(price).toString();
        tkUsers.setBalance(newBalance);
        tkUsersMapper.updateTkUsers(tkUsers);

        //扣减库存
        spProduct.setProductInventory(new BigDecimal(spProduct.getProductInventory()).
                subtract(new BigDecimal(quantity.toString())).toString());

        spProductMapper.updateSpProduct(spProduct);

        //添加余额记录
        //添加记录
        TkWallettransactions tkWallettransaction = new TkWallettransactions();
        tkWallettransaction.setUserid(uid);
        tkWallettransaction.setFundBalance(new BigDecimal(newBalance));
        tkWallettransaction.setCategory("Buy");
        tkWallettransaction.setDescription("Buy products"+"#"+spProduct.getId());
        tkWallettransaction.setAmount(price.negate());
        tkWallettransaction.setTransactionStatus("已完成");
        tkWallettransaction.setUpdatedAt(new Date());
        tkWallettransaction.setTransactionType("withdraw");
        tkWallettransaction.setTransactionDate(new Date());
        tkWallettransaction.setOrderNumber(OrderNumberGenerator.generateOrderNumber());
        int i1 = tkWallettransactionsMapper.insertTkWallettransactions(tkWallettransaction);
        return AjaxResult.success();
    }

    @Override
    public List<OrderProductOV> orderList(String stateId,String orderId) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();//当前登录用户
        List<OrderProductOV> OrderProductOV = spOrdersMapper.getOrdersAndProductDetails(uid, stateId,orderId);

        for (int i = 0; i < OrderProductOV.size(); i++) {
            SpProduct product = OrderProductOV.get(i).getProduct();
            SpOrders order = OrderProductOV.get(i).getOrder();
            String language  = "English";
            if (uid != null ) {
                // 从Redis中获取用户的语言设置
             String l =  redisCache.getCacheObject("user:language:" + uid);
                if(l!=null){
                    language = l;
                }
            }
            product = (SpProduct) iterateObjectFields(product,language);
            order = (SpOrders) iterateObjectFields(order,language);
            OrderProductOV.get(i).setOrder(order);
            OrderProductOV.get(i).setProduct(product);
        }
        
        return OrderProductOV;
    }

    @Override
    public  List<Map<String, Object>>  OrderStatus() {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();//当前登录用户
        List<Map<String, Object>> objects = spOrdersMapper.countOrdersByStatus(uid);
        return objects;
    }
}
