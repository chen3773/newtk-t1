package com.tiktok.task.service.impl;

import java.util.List;

import com.tiktok.common.core.redis.RedisCache;
import com.tiktok.common.utils.SecurityUtils;
import com.tiktok.task.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tiktok.task.mapper.TkAgreementMapper;
import com.tiktok.task.domain.TkAgreement;
import com.tiktok.task.service.ITkAgreementService;

/**
 * 文本Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-07
 */
@Service
public class TkAgreementServiceImpl implements ITkAgreementService 
{
    @Autowired
    private TkAgreementMapper tkAgreementMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 查询文本
     * 
     * @param id 文本主键
     * @return 文本
     */
    @Override
    public TkAgreement selectTkAgreementById(Long id)
    {
        return tkAgreementMapper.selectTkAgreementById(id);
    }

    /**
     * 查询文本列表
     * 
     * @param tkAgreement 文本
     * @return 文本
     */
    @Override
    public List<TkAgreement> selectTkAgreementList(TkAgreement tkAgreement)
    {
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        List<TkAgreement> tkAgreements = tkAgreementMapper.selectTkAgreementList(tkAgreement);
        return LanguageUtil.processListWithLanguageSettingList(uid,tkAgreements,redisCache);
    }

    /**
     * 新增文本
     * 
     * @param tkAgreement 文本
     * @return 结果
     */
    @Override
    public int insertTkAgreement(TkAgreement tkAgreement)
    {
        return tkAgreementMapper.insertTkAgreement(tkAgreement);
    }

    /**
     * 修改文本
     * 
     * @param tkAgreement 文本
     * @return 结果
     */
    @Override
    public int updateTkAgreement(TkAgreement tkAgreement)
    {
        return tkAgreementMapper.updateTkAgreement(tkAgreement);
    }

    /**
     * 批量删除文本
     * 
     * @param ids 需要删除的文本主键
     * @return 结果
     */
    @Override
    public int deleteTkAgreementByIds(Long[] ids)
    {
        return tkAgreementMapper.deleteTkAgreementByIds(ids);
    }

    /**
     * 删除文本信息
     * 
     * @param id 文本主键
     * @return 结果
     */
    @Override
    public int deleteTkAgreementById(Long id)
    {
        return tkAgreementMapper.deleteTkAgreementById(id);
    }
}
