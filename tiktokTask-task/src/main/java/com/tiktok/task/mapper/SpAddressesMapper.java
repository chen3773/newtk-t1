package com.tiktok.task.mapper;

import java.util.List;
import com.tiktok.task.domain.SpAddresses;

/**
 * 地址Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
public interface SpAddressesMapper 
{
    /**
     * 查询地址
     * 
     * @param addressId 地址主键
     * @return 地址
     */
    public SpAddresses selectSpAddressesByAddressId(Long addressId);

    /**
     * 查询地址列表
     * 
     * @param spAddresses 地址
     * @return 地址集合
     */
    public List<SpAddresses> selectSpAddressesList(SpAddresses spAddresses);

    /**
     * 新增地址
     * 
     * @param spAddresses 地址
     * @return 结果
     */
    public int insertSpAddresses(SpAddresses spAddresses);

    /**
     * 修改地址
     * 
     * @param spAddresses 地址
     * @return 结果
     */
    public int updateSpAddresses(SpAddresses spAddresses);

    /**
     * 删除地址
     * 
     * @param addressId 地址主键
     * @return 结果
     */
    public int deleteSpAddressesByAddressId(Long addressId);

    /**
     * 批量删除地址
     * 
     * @param addressIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpAddressesByAddressIds(Long[] addressIds);
}
