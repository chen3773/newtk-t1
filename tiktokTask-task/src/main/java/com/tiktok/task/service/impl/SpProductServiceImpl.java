package com.tiktok.task.service.impl;

import java.util.List;

import com.tiktok.common.core.redis.RedisCache;
import com.tiktok.common.utils.DateUtils;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.task.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.mapper.SpProductMapper;
import com.tiktok.task.domain.SpProduct;
import com.tiktok.task.service.ISpProductService;

/**
 * 产品Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
@Service
public class SpProductServiceImpl implements ISpProductService 
{
    @Autowired
    private SpProductMapper spProductMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 查询产品
     * 
     * @param id 产品主键
     * @return 产品
     */
    @Override
    public SpProduct selectSpProductById(Long id)
    {
        SpProduct spProduct = spProductMapper.selectSpProductById(id);
       return spProduct;
    }

    /**
     * 查询产品列表
     * 
     * @param spProduct 产品
     * @return 产品
     */
    @Override
    public List<SpProduct> selectSpProductList(SpProduct spProduct)
    {
        List<SpProduct> spProducts = spProductMapper.selectSpProductList(spProduct);
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        return LanguageUtil.processListWithLanguageSettingList(uid,spProducts,redisCache);
    }

    /**
     * 新增产品
     * 
     * @param spProduct 产品
     * @return 结果
     */
    @Override
    public int insertSpProduct(SpProduct spProduct)
    {
        spProduct.setCreateTime(DateUtils.getNowDate());
        return spProductMapper.insertSpProduct(spProduct);
    }

    /**
     * 修改产品
     * 
     * @param spProduct 产品
     * @return 结果
     */
    @Override
    public int updateSpProduct(SpProduct spProduct)
    {
        spProduct.setUpdateTime(DateUtils.getNowDate());
        return spProductMapper.updateSpProduct(spProduct);
    }

    /**
     * 批量删除产品
     * 
     * @param ids 需要删除的产品主键
     * @return 结果
     */
    @Override
    public int deleteSpProductByIds(Long[] ids)
    {
        return spProductMapper.deleteSpProductByIds(ids);
    }

    /**
     * 删除产品信息
     * 
     * @param id 产品主键
     * @return 结果
     */
    @Override
    public int deleteSpProductById(Long id)
    {
        return spProductMapper.deleteSpProductById(id);
    }
}
