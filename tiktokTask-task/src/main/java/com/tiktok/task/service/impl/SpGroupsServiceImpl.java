package com.tiktok.task.service.impl;

import java.util.List;

import com.tiktok.common.core.redis.RedisCache;
import com.tiktok.common.utils.DateUtils;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.task.domain.SpProduct;
import com.tiktok.task.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.mapper.SpGroupsMapper;
import com.tiktok.task.domain.SpGroups;
import com.tiktok.task.service.ISpGroupsService;

import static com.tiktok.common.utils.ParseTitle.iterateObjectFields;

/**
 * 商品分类Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
@Service
public class SpGroupsServiceImpl implements ISpGroupsService 
{
    @Autowired
    private SpGroupsMapper spGroupsMapper;

    @Autowired
    private RedisCache redisCache;
    
    
    /**
     * 查询商品分类
     * 
     * @param id 商品分类主键
     * @return 商品分类
     */
    @Override
    public SpGroups selectSpGroupsById(Long id)
    {
        return spGroupsMapper.selectSpGroupsById(id);
    }

    /**
     * 查询商品分类列表
     * 
     * @param spGroups 商品分类
     * @return 商品分类
     */
    @Override
    public List<SpGroups> selectSpGroupsList(SpGroups spGroups)
    {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        List<SpGroups> spGrouplist = spGroupsMapper.selectSpGroupsList(spGroups);
        return LanguageUtil.processListWithLanguageSettingList(uid, spGrouplist, redisCache);
    }

    /**
     * 新增商品分类
     * 
     * @param spGroups 商品分类
     * @return 结果
     */
    @Override
    public int insertSpGroups(SpGroups spGroups)
    {
        spGroups.setCreateTime(DateUtils.getNowDate());
        return spGroupsMapper.insertSpGroups(spGroups);
    }

    /**
     * 修改商品分类
     * 
     * @param spGroups 商品分类
     * @return 结果
     */
    @Override
    public int updateSpGroups(SpGroups spGroups)
    {
        return spGroupsMapper.updateSpGroups(spGroups);
    }

    /**
     * 批量删除商品分类
     * 
     * @param ids 需要删除的商品分类主键
     * @return 结果
     */
    @Override
    public int deleteSpGroupsByIds(Long[] ids)
    {
        return spGroupsMapper.deleteSpGroupsByIds(ids);
    }

    /**
     * 删除商品分类信息
     * 
     * @param id 商品分类主键
     * @return 结果
     */
    @Override
    public int deleteSpGroupsById(Long id)
    {
        return spGroupsMapper.deleteSpGroupsById(id);
    }
}
