package com.tiktok.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.tiktok.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.mapper.SpAddressesMapper;
import com.tiktok.task.domain.SpAddresses;
import com.tiktok.task.service.ISpAddressesService;
import org.springframework.util.Assert;

/**
 * 地址Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
@Service
public class SpAddressesServiceImpl implements ISpAddressesService 
{
    @Autowired
    private SpAddressesMapper spAddressesMapper;

    /**
     * 查询地址
     * 
     * @param addressId 地址主键
     * @return 地址
     */
    @Override
    public SpAddresses selectSpAddressesByAddressId(Long addressId)
    {
        return spAddressesMapper.selectSpAddressesByAddressId(addressId);
    }

    /**
     * 查询地址列表
     * 
     * @param spAddresses 地址
     * @return 地址
     */
    @Override
    public List<SpAddresses> selectSpAddressesList(SpAddresses spAddresses)
    {
        return spAddressesMapper.selectSpAddressesList(spAddresses);
    }

    /**
     * 新增地址
     * 
     * @param spAddresses 地址
     * @return 结果
     */
    @Override
    public int insertSpAddresses(SpAddresses spAddresses)
    {
        return spAddressesMapper.insertSpAddresses(spAddresses);
    }

    /**
     * 修改地址
     * 
     * @param spAddresses 地址
     * @return 结果
     */
    @Override
    public int updateSpAddresses(SpAddresses spAddresses)
    {
        return spAddressesMapper.updateSpAddresses(spAddresses);
    }

    /**
     * 批量删除地址
     * 
     * @param addressIds 需要删除的地址主键
     * @return 结果
     */
    @Override
    public int deleteSpAddressesByAddressIds(Long[] addressIds)
    {
        return spAddressesMapper.deleteSpAddressesByAddressIds(addressIds);
    }

    /**
     * 删除地址信息
     * 
     * @param addressId 地址主键
     * @return 结果
     */
    @Override
    public int deleteSpAddressesByAddressId(Long addressId)
    {
        return spAddressesMapper.deleteSpAddressesByAddressId(addressId);
    }

    @Override
    public int editAddresses(SpAddresses spAddresses) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        spAddresses.setUid(uid);
        if(spAddresses.getDefaultAddress()!=null){
            SpAddresses Addresses = new SpAddresses();
            Addresses.setUid(uid);
            List<SpAddresses> spAddresses1 = spAddressesMapper.selectSpAddressesList(Addresses);
            for (int i = 0; i < spAddresses1.size(); i++) {
                spAddresses1.get(i).setDefaultAddress("0");
                spAddressesMapper.updateSpAddresses(spAddresses1.get(i));
            }
        }
        return spAddressesMapper.updateSpAddresses(spAddresses);
    }

    @Override
    public int addAddresses(SpAddresses spAddresses) {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        SpAddresses Addresses = new SpAddresses();
        Addresses.setUid(uid);
        List<SpAddresses> spAddresses1 = spAddressesMapper.selectSpAddressesList(Addresses);
        Assert.isTrue(spAddresses1.size()<=5,"{ Chinese: \"地址達到上限\",   \"English\": \"Max 5\" }");
        return spAddressesMapper.insertSpAddresses(spAddresses);
    }

    @Override
    public List<SpAddresses> Addresses(SpAddresses spAddresses) {
        List<SpAddresses> spAddresses1 = spAddressesMapper.selectSpAddressesList(spAddresses);


        List<SpAddresses> sortedAddresses = spAddresses1.stream()
                .sorted((a, b) -> {
                    // Compare defaultAddress values
                    return b.getDefaultAddress().compareTo(a.getDefaultAddress());
                })
                .collect(Collectors.toList());
        return sortedAddresses;
    }
}
