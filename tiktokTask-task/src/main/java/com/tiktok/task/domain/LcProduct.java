package com.tiktok.task.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tiktok.common.annotation.Excel;
import com.tiktok.common.core.domain.BaseEntity;

/**
 * 众筹产品对象 lc_product
 * 
 * @author ruoyi
 * @date 2024-10-22
 */
public class LcProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 产品ID */
    private Long productId;

    /** 产品标题 */
    @Excel(name = "产品标题")
    private String productTitle;

    /** 商品大图 */
    @Excel(name = "商品大图")
    private String mainImageUrl;

    /** 详情图 */
    @Excel(name = "详情图")
    private String detailImageUrl;

    /** 商品详情描述 */
    @Excel(name = "商品详情描述")
    private String productDescription;

    /** 众筹总额 */
    @Excel(name = "众筹总额")
    private BigDecimal totalAmount;

    /** 最低等级限制 */
    @Excel(name = "最低等级限制")
    private String level;

    /** 份额 */
    @Excel(name = "份额")
    private Long totalShares;

    /** 已售出份额 */
    @Excel(name = "已售出份额")
    private Long soldShares;

    /** 开始时间 */
    @JsonFormat(pattern = "MM/dd/yyyy")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 分红开始时间 */
    @JsonFormat(pattern = "MM/dd/yyyy")
    @Excel(name = "分红开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dividendStartTime;

    /** 结束时间 */
    @JsonFormat(pattern = "MM/dd/yyyy")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 状态 */
    @Excel(name = "状态")
    private String state;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    private String deleted;

    /** 周期 */
    @Excel(name = "周期")
    private String cycle;

    /** 收益率 */
    @Excel(name = "收益率")
    private String returnRate;

    /** 分红天数 */
    @Excel(name = "分红天数")
    private String dividendDays;

    /** 类型 */
    @Excel(name = "类型")
    private String type;
    private String Completed;
    /**
     * 总额
     */
    private String grossAmount;
    private String participants;
    private String estimatedIncome;

    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setProductTitle(String productTitle) 
    {
        this.productTitle = productTitle;
    }

    public String getProductTitle() 
    {
        return productTitle;
    }
    public void setMainImageUrl(String mainImageUrl) 
    {
        this.mainImageUrl = mainImageUrl;
    }

    public String getMainImageUrl() 
    {
        return mainImageUrl;
    }
    public void setDetailImageUrl(String detailImageUrl) 
    {
        this.detailImageUrl = detailImageUrl;
    }

    public String getDetailImageUrl() 
    {
        return detailImageUrl;
    }
    public void setProductDescription(String productDescription) 
    {
        this.productDescription = productDescription;
    }

    public String getProductDescription() 
    {
        return productDescription;
    }
    public void setTotalAmount(BigDecimal totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() 
    {
        return totalAmount;
    }
    public void setLevel(String level) 
    {
        this.level = level;
    }

    public String getLevel() 
    {
        return level;
    }
    public void setTotalShares(Long totalShares) 
    {
        this.totalShares = totalShares;
    }

    public Long getTotalShares() 
    {
        return totalShares;
    }
    public void setSoldShares(Long soldShares) 
    {
        this.soldShares = soldShares;
    }

    public Long getSoldShares() 
    {
        return soldShares;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setDeleted(String deleted) 
    {
        this.deleted = deleted;
    }

    public String getDeleted() 
    {
        return deleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("productTitle", getProductTitle())
            .append("mainImageUrl", getMainImageUrl())
            .append("detailImageUrl", getDetailImageUrl())
            .append("productDescription", getProductDescription())
            .append("totalAmount", getTotalAmount())
            .append("level", getLevel())
            .append("totalShares", getTotalShares())
            .append("soldShares", getSoldShares())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("state", getState())
            .append("remark", getRemark())
            .append("deleted", getDeleted())
            .toString();
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(String returnRate) {
        this.returnRate = returnRate;
    }

    public String getDividendDays() {
        return dividendDays;
    }

    public void setDividendDays(String dividendDays) {
        this.dividendDays = dividendDays;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompleted() {
        return Completed;
    }

    public void setCompleted(String completed) {
        Completed = completed;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public Date getDividendStartTime() {
        return dividendStartTime;
    }

    public void setDividendStartTime(Date dividendStartTime) {
        this.dividendStartTime = dividendStartTime;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getEstimatedIncome() {
        return estimatedIncome;
    }

    public void setEstimatedIncome(String estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }
}
