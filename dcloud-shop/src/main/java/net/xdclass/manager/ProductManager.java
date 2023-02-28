package net.xdclass.manager;

import net.xdclass.model.ProductDO;

import java.util.List;

public interface ProductManager {

    /**
     * 查看商品列表
     * @return
     */
    List<ProductDO> list();


    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    ProductDO findDetailById(long productId);
}
