package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.ProductManager;
import net.xdclass.model.ProductDO;
import net.xdclass.service.ProductService;
import net.xdclass.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 刘森飚
 * @since 2023-01-30
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductManager productManager;


    /**
     * 查看商品列表
     * @return
     */
    @Override
    public List<ProductVO> list() {

        List<ProductDO> list = productManager.list();
        List<ProductVO> collect = list.stream().map( obj ->
                beanProcess(obj) ).collect(Collectors.toList());
        return collect;
    }


    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    @Override
    public ProductVO findDetailById(long productId) {
        ProductDO productDO = productManager.findDetailById(productId);
        ProductVO productVO = beanProcess(productDO);
        return productVO;
    }


    private ProductVO beanProcess(ProductDO productDO) {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productDO, productVO);
        return productVO;
    }
}
