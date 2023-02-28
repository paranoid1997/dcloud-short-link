package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import groovy.util.logging.Slf4j;
import net.xdclass.manager.ProductManager;
import net.xdclass.mapper.ProductMapper;
import net.xdclass.model.ProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author 刘森飚
 **/
@Component
@Slf4j
public class ProductManagerImpl implements ProductManager {

    @Autowired
    private ProductMapper productMapper;


    /**
     * 查看商品列表
     * @return
     */
    @Override
    public List<ProductDO> list() {
        return productMapper.selectList(null);
    }


    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    @Override
    public ProductDO findDetailById(long productId) {
        return productMapper.selectOne(new QueryWrapper<ProductDO>()
                .eq("id",productId));
    }
}
