package com.tiktok.task.mapper;

import java.util.List;
import com.tiktok.task.domain.SpProduct;

/**
 * 产品Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
public interface SpProductMapper 
{
    /**
     * 查询产品
     * 
     * @param id 产品主键
     * @return 产品
     */
    public SpProduct selectSpProductById(Long id);

    /**
     * 查询产品列表
     * 
     * @param spProduct 产品
     * @return 产品集合
     */
    public List<SpProduct> selectSpProductList(SpProduct spProduct);

    /**
     * 新增产品
     * 
     * @param spProduct 产品
     * @return 结果
     */
    public int insertSpProduct(SpProduct spProduct);

    /**
     * 修改产品
     * 
     * @param spProduct 产品
     * @return 结果
     */
    public int updateSpProduct(SpProduct spProduct);

    /**
     * 删除产品
     * 
     * @param id 产品主键
     * @return 结果
     */
    public int deleteSpProductById(Long id);

    /**
     * 批量删除产品
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpProductByIds(Long[] ids);
}
