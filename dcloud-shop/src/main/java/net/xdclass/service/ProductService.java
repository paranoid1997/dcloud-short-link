package net.xdclass.service;


import net.xdclass.vo.ProductVO;

import java.util.List;

/**
 * @author 刘森飚
 * @since 2023-01-30
 */
public interface ProductService{

    /**
     * 查看商品列表
     * @return
     */
    List<ProductVO> list();


    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    ProductVO findDetailById(long productId);

}
