package com.tiktok.task.mapper;

import java.util.List;
import com.tiktok.task.domain.SpGroups;

/**
 * 商品分类Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-26
 */
public interface SpGroupsMapper 
{
    /**
     * 查询商品分类
     * 
     * @param id 商品分类主键
     * @return 商品分类
     */
    public SpGroups selectSpGroupsById(Long id);

    /**
     * 查询商品分类列表
     * 
     * @param spGroups 商品分类
     * @return 商品分类集合
     */
    public List<SpGroups> selectSpGroupsList(SpGroups spGroups);

    /**
     * 新增商品分类
     * 
     * @param spGroups 商品分类
     * @return 结果
     */
    public int insertSpGroups(SpGroups spGroups);

    /**
     * 修改商品分类
     * 
     * @param spGroups 商品分类
     * @return 结果
     */
    public int updateSpGroups(SpGroups spGroups);

    /**
     * 删除商品分类
     * 
     * @param id 商品分类主键
     * @return 结果
     */
    public int deleteSpGroupsById(Long id);

    /**
     * 批量删除商品分类
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpGroupsByIds(Long[] ids);
}
