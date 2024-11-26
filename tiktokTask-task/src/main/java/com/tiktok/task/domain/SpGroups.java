package com.tiktok.task.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tiktok.common.annotation.Excel;
import com.tiktok.common.core.domain.BaseEntity;

/**
 * 商品分类对象 sp_groups
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
public class SpGroups extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分组ID */
    private Long id;

    /** 分组名称 */
    @Excel(name = "分组名称")
    private String groupName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGroupName(String groupName) 
    {
        this.groupName = groupName;
    }

    public String getGroupName() 
    {
        return groupName;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupName", getGroupName())
            .append("createTime", getCreateTime())
            .append("status", getStatus())
            .toString();
    }
}
