package com.tiktok.task.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tiktok.common.annotation.Excel;
import com.tiktok.common.core.domain.BaseEntity;

/**
 * 地址对象 sp_addresses
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
public class SpAddresses extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地址ID */
    private Long addressId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long uid;

    /** 收件人全名 */
    @Excel(name = "收件人全名")
    private String fullName;

    /** 街道地址行1 */
    @Excel(name = "街道地址行1")
    private String streetAddress1;

    /** 街道地址行2 */
    @Excel(name = "街道地址行2")
    private String streetAddress2;

    /** 城市 */
    @Excel(name = "城市")
    private String city;

    /** 州/省 */
    @Excel(name = "州/省")
    private String state;

    /** 邮政编码 */
    @Excel(name = "邮政编码")
    private String postalCode;

    /** 国家 */
    @Excel(name = "国家")
    private String country;

    /** 联系电话号码 */
    @Excel(name = "联系电话号码")
    private String phoneNumber;

    /** 电子邮件地址 */
    @Excel(name = "电子邮件地址")
    private String email;

    /** 是否为默认地址 */
    @Excel(name = "是否为默认地址")
    private String defaultAddress;

    // 构造函数、getter 和 setter 方法省略

    public String toUSAddressFormat() {
        StringBuilder addressBuilder = new StringBuilder();
        addressBuilder.append(fullName).append(",\n");
        if (streetAddress1 != null && !streetAddress1.isEmpty()) {
            addressBuilder.append(streetAddress1).append(",\n");
        }
        if (streetAddress2 != null && !streetAddress2.isEmpty()) {
            addressBuilder.append(streetAddress2).append(",\n");
        }
        addressBuilder.append(city).append(", ").append(state).append(" ").append(postalCode).append("\n");
        addressBuilder.append(country);

        return addressBuilder.toString();
    }

    public void setAddressId(Long addressId) 
    {
        this.addressId = addressId;
    }

    public Long getAddressId() 
    {
        return addressId;
    }
    public void setUid(Long uid) 
    {
        this.uid = uid;
    }

    public Long getUid() 
    {
        return uid;
    }
    public void setFullName(String fullName) 
    {
        this.fullName = fullName;
    }

    public String getFullName() 
    {
        return fullName;
    }
    public void setStreetAddress1(String streetAddress1) 
    {
        this.streetAddress1 = streetAddress1;
    }

    public String getStreetAddress1() 
    {
        return streetAddress1;
    }
    public void setStreetAddress2(String streetAddress2) 
    {
        this.streetAddress2 = streetAddress2;
    }

    public String getStreetAddress2() 
    {
        return streetAddress2;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setPostalCode(String postalCode) 
    {
        this.postalCode = postalCode;
    }

    public String getPostalCode() 
    {
        return postalCode;
    }
    public void setCountry(String country) 
    {
        this.country = country;
    }

    public String getCountry() 
    {
        return country;
    }
    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setDefaultAddress(String defaultAddress) 
    {
        this.defaultAddress = defaultAddress;
    }

    public String getDefaultAddress() 
    {
        return defaultAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("addressId", getAddressId())
            .append("uid", getUid())
            .append("fullName", getFullName())
            .append("streetAddress1", getStreetAddress1())
            .append("streetAddress2", getStreetAddress2())
            .append("city", getCity())
            .append("state", getState())
            .append("postalCode", getPostalCode())
            .append("country", getCountry())
            .append("phoneNumber", getPhoneNumber())
            .append("email", getEmail())
            .append("defaultAddress", getDefaultAddress())
            .toString();
    }
}
