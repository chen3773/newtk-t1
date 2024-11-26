package com.tiktok.task.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import com.tiktok.common.core.domain.AjaxResult;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.task.domain.*;
import com.tiktok.task.domain.ov.SubscriptionProductOV;
import com.tiktok.task.mapper.*;
import com.tiktok.task.util.OrderNumberGenerator;
import com.tiktok.task.util.PercentageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.service.ILcSubscriptionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 认购Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-22
 */
@Service
public class LcSubscriptionServiceImpl implements ILcSubscriptionService 
{
    @Autowired
    private LcSubscriptionMapper lcSubscriptionMapper;
    @Autowired
    private TkUsersMapper tkUsersMapper;
    @Autowired
    private LcProductMapper lcProductMapper;
    @Autowired
    private TkWallettransactionsMapper tkWallettransactionsMapper;
    @Autowired
    private LcRecordsMapper lcRecordsMapper;

    /**
     * 查询认购
     * 
     * @param id 认购主键
     * @return 认购
     */
    @Override
    public LcSubscription selectLcSubscriptionById(Long id)
    {
        return lcSubscriptionMapper.selectLcSubscriptionById(id);
    }

    /**
     * 查询认购列表
     * 
     * @param lcSubscription 认购
     * @return 认购
     */
    @Override
    public List<LcSubscription> selectLcSubscriptionList(LcSubscription lcSubscription)
    {
        return lcSubscriptionMapper.selectLcSubscriptionList(lcSubscription);
    }

    /**
     * 新增认购
     * 
     * @param lcSubscription 认购
     * @return 结果
     */
    @Override
    public int insertLcSubscription(LcSubscription lcSubscription)
    {
        return lcSubscriptionMapper.insertLcSubscription(lcSubscription);
    }

    /**
     * 修改认购
     * 
     * @param lcSubscription 认购
     * @return 结果
     */
    @Override
    public int updateLcSubscription(LcSubscription lcSubscription)
    {
        return lcSubscriptionMapper.updateLcSubscription(lcSubscription);
    }

    /**
     * 批量删除认购
     * 
     * @param ids 需要删除的认购主键
     * @return 结果
     */
    @Override
    public int deleteLcSubscriptionByIds(Long[] ids)
    {
        return lcSubscriptionMapper.deleteLcSubscriptionByIds(ids);
    }

    /**
     * 删除认购信息
     * 
     * @param id 认购主键
     * @return 结果
     */
    @Override
    public int deleteLcSubscriptionById(Long id)
    {
        return lcSubscriptionMapper.deleteLcSubscriptionById(id);
    }

    /**
     * 用户认购产品
     * @return
     */
    @Override
    @Transactional
    public AjaxResult Buy(Map<String, Object> requestData) {
        String productId = requestData.get("productId").toString();//产品id
        String paymentPassword = requestData.get("paymentPassword").toString();//产品id
        BigDecimal quantity  = new BigDecimal(requestData.get("quantity").toString());//认购数量
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();//当前登录用户

        TkUsers tkUsers = tkUsersMapper.selectTkUsersByUid(uid);//获取出来的用户信息
        BigDecimal frozenIvestmentAmount =new BigDecimal(tkUsers.getFrozenIvestmentAmount());//投资账号冻结余额
        BigDecimal investmentAmount = new BigDecimal(tkUsers.getInvestmentAmount());//投资账号总余额
        LcProduct lcProduct = lcProductMapper.selectLcProductByProductId(Long.valueOf(productId));//产品信息
        BigDecimal TotalAmount = new BigDecimal(lcProduct.getTotalAmount().toString());//单价
        BigDecimal totalShares = new BigDecimal(lcProduct.getTotalShares());//份额
        BigDecimal soldShares = new BigDecimal(lcProduct.getSoldShares());//已售出份额
        String cycle = lcProduct.getCycle();//产品周期  天
        Date currentDate = new Date();//当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(cycle));
        Date expirationDate = calendar.getTime();//过期时间



        Assert.isTrue(tkUsers.getPaymentPassword()!=null,"Set a payment password.");
        Assert.isTrue(tkUsers.getPaymentPassword().trim().equals(paymentPassword.trim()),"Payment password error");

        //判断产品是否在有效期
        Date endTime = lcProduct.getEndTime();
        Date date = new Date();//当前时间
        Assert.isTrue(date.before(endTime), "Already ended.");
        BigDecimal subtract = totalShares.subtract(soldShares);//剩余数量

        //判断份额是否足够
        Assert.isTrue(quantity.compareTo(subtract) <= 0, "Shortage of quantity.");

        //更新数据库已售出份额
        lcProduct.setSoldShares(lcProduct.getSoldShares()+quantity.intValue());
        lcProductMapper.updateLcProduct(lcProduct);

        //判断余额是否足够  计算出用户所需余额
        BigDecimal totalPrice = TotalAmount.multiply(quantity);//当前交易总价
        BigDecimal availableBalance = investmentAmount.subtract(frozenIvestmentAmount);//可用余额
        Assert.isTrue(availableBalance.compareTo(totalPrice) >=0,"Insufficient money.");

        //购入产品
        LcSubscription lcSubscription = new LcSubscription();
        lcSubscription.setUserId(uid);
        lcSubscription.setProductId(Long.valueOf(productId));
        lcSubscription.setSubscriptionCount(quantity.longValue());
        lcSubscription.setPurchasePrice(TotalAmount.toString());
        lcSubscription.setPurchaseTime(currentDate);
        lcSubscription.setCumulativeDividend("0");
        lcSubscription.setRemainingDays(cycle);
        lcSubscription.setExpirationTime(expirationDate);
        lcSubscription.setStatus("0");
        lcSubscription.setDividendStartTime(lcProduct.getDividendStartTime());
        lcSubscription.setOrderNumber(OrderNumberGenerator.generateOrderNumber());

        lcSubscription.setDividendIntervalDays(Long.valueOf(lcProduct.getDividendDays()));
        lcSubscription.setDividendDays(Long.valueOf(lcProduct.getDividendDays()));
        Assert.isTrue( lcSubscriptionMapper.insertLcSubscription(lcSubscription)!=0,"Network error");

        //操作用户进行扣款
        String InvestmentAmount = String.valueOf(investmentAmount.subtract(totalPrice));//交易后余额
        tkUsers.setInvestmentAmount(InvestmentAmount);
        Assert.isTrue( tkUsersMapper.updateTkUsers(tkUsers)!=0,"Network error");

        //添加记录
        TkWallettransactions tkWallettransaction = new TkWallettransactions();
        tkWallettransaction.setUserid(uid);
        tkWallettransaction.setFundBalance(new BigDecimal(InvestmentAmount));
        tkWallettransaction.setCategory("Buy");
        tkWallettransaction.setDescription("Buy products"+"#"+lcProduct.getProductId());
        tkWallettransaction.setAmount(totalPrice.negate());
        tkWallettransaction.setTransactionStatus("已完成");
        tkWallettransaction.setUpdatedAt(new Date());
        tkWallettransaction.setTransactionType("withdraw");
        tkWallettransaction.setTransactionDate(new Date());
        tkWallettransaction.setOrderNumber(OrderNumberGenerator.generateOrderNumber());
        tkWallettransactionsMapper.insertTkWallettransactions(tkWallettransaction);
        return AjaxResult.success("Successful");
    }

    @Override
    public AjaxResult MyProduct(String status) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        List<SubscriptionProductOV> subscriptionProductOVS = lcSubscriptionMapper.selectSubscriptionProductByUserIdAndStatus(uid, status,null);
        for (int i = 0; i < subscriptionProductOVS.size(); i++) {
            LcProduct lcProduct = subscriptionProductOVS.get(i).getLcProduct();
            LcSubscription lcSubscription = subscriptionProductOVS.get(i).getLcSubscription();
            Long totalShares = lcProduct.getTotalShares();//总数
            Long soldShares = lcProduct.getSoldShares();//已出售份额
            BigDecimal totalAmount = lcProduct.getTotalAmount();//单价
            double v = PercentageCalculator.calculatePercentage(totalShares.intValue(), soldShares.intValue());
            lcProduct.setCompleted(v + "%");
            lcProduct.setGrossAmount((new BigDecimal(totalShares).multiply(totalAmount)).toString());


            BigDecimal  cycle = new BigDecimal(lcProduct.getCycle());//周期
            BigDecimal dividendDays = new BigDecimal(lcProduct.getDividendDays());//天数
            BigDecimal returnRate = new BigDecimal(lcProduct.getReturnRate());//收益
            //计算产品大概可以产生多少收益
            // 计算 cycle / dividendDays 并取整
            BigDecimal divisionResult = cycle.divide(dividendDays, 0, RoundingMode.DOWN);

            // 结果乘以 returnRate 并保留两位小数
            BigDecimal finalResult = divisionResult.multiply(returnRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal count = new BigDecimal(lcSubscription.getSubscriptionCount());
            BigDecimal multiply = count.multiply(finalResult);
            subscriptionProductOVS.get(i).getLcProduct().setEstimatedIncome(multiply.toString());

        }
        Collections.reverse(subscriptionProductOVS);
        return AjaxResult.success(subscriptionProductOVS);
    }

    @Override
    public AjaxResult orderDtails(String orderNumber) {
        //获取出订单和对应的产品信息
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        //获取出来一个订单信息
        List<SubscriptionProductOV> subscriptionProductOVS = lcSubscriptionMapper.selectSubscriptionProductByUserIdAndStatus(uid, null,orderNumber);
        for (int i = 0; i < subscriptionProductOVS.size(); i++) {
            Long totalShares = subscriptionProductOVS.get(i).getLcProduct().getTotalShares();//总数
            Long soldShares = subscriptionProductOVS.get(i).getLcProduct().getSoldShares();//已出售份额
            BigDecimal totalAmount = subscriptionProductOVS.get(i).getLcProduct().getTotalAmount();//单价
            double v = PercentageCalculator.calculatePercentage(totalShares.intValue(), soldShares.intValue());
            subscriptionProductOVS.get(i).getLcProduct().setCompleted(v + "%");
            subscriptionProductOVS.get(i).getLcProduct().setGrossAmount((new BigDecimal(totalShares).multiply(totalAmount)).toString());
        }
        //获取出来分红记录
        LcRecords lcRecordsBody = new LcRecords();
        lcRecordsBody.setOrderNumber(orderNumber);
        List<LcRecords> lcRecords = lcRecordsMapper.selectLcRecordsList(lcRecordsBody);
        Collections.reverse(lcRecords); // 对lcRecords列表进行倒序排序
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        if(subscriptionProductOVS.size()!=0){
            SubscriptionProductOV subscriptionProductOV = subscriptionProductOVS.get(0);
            LcProduct lcProduct = subscriptionProductOV.getLcProduct();
            LcSubscription lcSubscription = subscriptionProductOV.getLcSubscription();

            BigDecimal  cycle = new BigDecimal(lcProduct.getCycle());//周期
            BigDecimal dividendDays = new BigDecimal(lcProduct.getDividendDays());//天数
            BigDecimal returnRate = new BigDecimal(lcProduct.getReturnRate());//收益
            

            //计算产品大概可以产生多少收益
            // 计算 cycle / dividendDays 并取整
            BigDecimal divisionResult = cycle.divide(dividendDays, 0, RoundingMode.DOWN);

            // 结果乘以 returnRate 并保留两位小数
            BigDecimal finalResult = divisionResult.multiply(returnRate).setScale(2, RoundingMode.HALF_UP);

            BigDecimal count = new BigDecimal(lcSubscription.getSubscriptionCount());
            BigDecimal multiply = count.multiply(finalResult);
            subscriptionProductOV.getLcProduct().setEstimatedIncome(multiply.toString());


            objectObjectHashMap.put("product",subscriptionProductOV);
        }
        objectObjectHashMap.put("lcRecords",lcRecords);
        return AjaxResult.success(objectObjectHashMap);
    }

    @Override
    public AjaxResult getProjectCountAndAmount() {
        //获取出订单和对应的产品信息
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        //获取出来分红记录
        LcRecords lcRecordsBody = new LcRecords();
        lcRecordsBody.setUserId(uid);
        lcRecordsBody.setTitle("Revenue");
        List<LcRecords> lcRecords = lcRecordsMapper.selectLcRecordsList(lcRecordsBody);
        // 使用Java 8 Stream API将amount转换为数字并求和
        double totalAmount = lcRecords.stream()
                .mapToDouble(record -> {
                    // 使用Optional处理可能为null的情况
                    return Optional.ofNullable(record.getAmount())
                            .map(Double::parseDouble)
                            .orElse(0.0);
                })
                .sum();
        //获取项目数量
        LcSubscription lcSubscription = new LcSubscription();
        lcSubscription.setUserId(uid);
        List<LcSubscription> lcSubscriptions = lcSubscriptionMapper.selectLcSubscriptionList(lcSubscription);
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("myProject",lcSubscriptions.size());
        objectObjectHashMap.put("projectIncome",totalAmount);


        //获取出用户一共质押了多少钱
        //获取出来未结束的订单
        lcSubscription.setStatus("0");
        List<LcSubscription> lcSubscriptions2 = lcSubscriptionMapper.selectLcSubscriptionList(lcSubscription);
        BigDecimal bigDecimal = new BigDecimal(0);
        for (int i = 0; i < lcSubscriptions2.size(); i++) {
            BigDecimal purchasePrice = new BigDecimal(lcSubscriptions2.get(i).getPurchasePrice());//单价
            BigDecimal subscriptionCount = new BigDecimal(lcSubscriptions2.get(i).getSubscriptionCount());//数量
            bigDecimal = bigDecimal.add(purchasePrice.multiply(subscriptionCount));

        }
        objectObjectHashMap.put("frozenAmount",bigDecimal);
        return AjaxResult.success(objectObjectHashMap);
    }
}
