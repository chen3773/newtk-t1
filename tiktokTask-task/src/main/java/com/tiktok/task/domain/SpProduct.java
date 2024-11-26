package com.tiktok.task.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tiktok.common.annotation.Excel;
import com.tiktok.common.core.domain.BaseEntity;

/**
 * 产品对象 sp_product
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
public class SpProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    @Excel(name = "")
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String productName;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal productPrice;

    /** 库存 */
    @Excel(name = "库存")
    private String productInventory;

    /** 大图 */
    @Excel(name = "大图")
    private String productImg;

    /** 详细描述 */
    @Excel(name = "详细描述")
    private String detailedDescription;

    /** 详情多图 */
    @Excel(name = "详情多图")
    private String productDetails;

    /** 0上架 1下架 */
    @Excel(name = "0上架 1下架")
    private String status;

    /** 分组id */
    @Excel(name = "分组id")
    private String groupId;

    /**  */
    @Excel(name = "")
    private String deleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setProductPrice(BigDecimal productPrice) 
    {
        this.productPrice = productPrice;
    }

    public BigDecimal getProductPrice() 
    {
        return productPrice;
    }
    public void setProductInventory(String productInventory) 
    {
        this.productInventory = productInventory;
    }

    public String getProductInventory() 
    {
        return productInventory;
    }
    public void setProductImg(String productImg) 
    {
        this.productImg = productImg;
    }

    public String getProductImg() 
    {
        return productImg;
    }
    public void setDetailedDescription(String detailedDescription) 
    {
        this.detailedDescription = detailedDescription;
    }

    public String getDetailedDescription() 
    {
        return detailedDescription;
    }
    public void setProductDetails(String productDetails) 
    {
        this.productDetails = productDetails;
    }

    public String getProductDetails() 
    {
        return productDetails;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setGroupId(String groupId) 
    {
        this.groupId = groupId;
    }

    public String getGroupId() 
    {
        return groupId;
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
            .append("id", getId())
            .append("productName", getProductName())
            .append("productPrice", getProductPrice())
            .append("productInventory", getProductInventory())
            .append("productImg", getProductImg())
            .append("detailedDescription", getDetailedDescription())
            .append("productDetails", getProductDetails())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("groupId", getGroupId())
            .append("deleted", getDeleted())
            .toString();
    }
}
