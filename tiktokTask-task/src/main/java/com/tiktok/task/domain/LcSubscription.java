package com.tiktok.task.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tiktok.common.annotation.Excel;
import com.tiktok.common.core.domain.BaseEntity;

/**
 * 认购对象 lc_subscription
 * 
 * @author ruoyi
 * @date 2024-10-22
 */
public class LcSubscription extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 认购份数 */
    @Excel(name = "认购份数")
    private Long subscriptionCount;

    /** 购入价格 */
    @Excel(name = "购入价格")
    private String purchasePrice;
    /** 分红开始时间 */
    @JsonFormat(pattern = "MM/dd/yyyy")
    @Excel(name = "分红开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dividendStartTime;

    /** 购入时间 */
    @JsonFormat(pattern = "MM/dd/yyyy")
    @Excel(name = "购入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseTime;

    /** 累计分红 */
    @Excel(name = "累计分红")
    private String cumulativeDividend;

    /** 过期时间 */
    @JsonFormat(pattern = "MM/dd/yyyy")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expirationTime;
    /** 分红间隔天数 */
    @Excel(name = "分红间隔天数")
    private Long dividendIntervalDays;

    /** 分红天数 */
    @Excel(name = "分红天数")
    private Long dividendDays;
    /** 剩余天数 */
    @Excel(name = "剩余天数")
    private String remainingDays;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNumber;
    private String productTitle;
    private String productType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setSubscriptionCount(Long subscriptionCount) 
    {
        this.subscriptionCount = subscriptionCount;
    }

    public Long getSubscriptionCount() 
    {
        return subscriptionCount;
    }
    public void setPurchasePrice(String purchasePrice) 
    {
        this.purchasePrice = purchasePrice;
    }

    public String getPurchasePrice() 
    {
        return purchasePrice;
    }
    public void setPurchaseTime(Date purchaseTime) 
    {
        this.purchaseTime = purchaseTime;
    }

    public Date getPurchaseTime() 
    {
        return purchaseTime;
    }
    public void setCumulativeDividend(String cumulativeDividend) 
    {
        this.cumulativeDividend = cumulativeDividend;
    }

    public String getCumulativeDividend() 
    {
        return cumulativeDividend;
    }
    public void setExpirationTime(Date expirationTime) 
    {
        this.expirationTime = expirationTime;
    }

    public Date getExpirationTime() 
    {
        return expirationTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("productId", getProductId())
            .append("subscriptionCount", getSubscriptionCount())
            .append("purchasePrice", getPurchasePrice())
            .append("purchaseTime", getPurchaseTime())
            .append("cumulativeDividend", getCumulativeDividend())
            .append("expirationTime", getExpirationTime())
            .toString();
    }

    public Long getDividendIntervalDays() {
        return dividendIntervalDays;
    }

    public void setDividendIntervalDays(Long dividendIntervalDays) {
        this.dividendIntervalDays = dividendIntervalDays;
    }

    public Long getDividendDays() {
        return dividendDays;
    }

    public void setDividendDays(Long dividendDays) {
        this.dividendDays = dividendDays;
    }

    public String getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(String remainingDays) {
        this.remainingDays = remainingDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getDividendStartTime() {
        return dividendStartTime;
    }

    public void setDividendStartTime(Date dividendStartTime) {
        this.dividendStartTime = dividendStartTime;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
