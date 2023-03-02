package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.mapper.ProductOrderMapper;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.vo.ProductOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author 刘森飚
 **/

@Component
public class ProductOrderManagerImpl implements ProductOrderManager {

    @Autowired
    private ProductOrderMapper productOrderMapper;


    /***
     * 新增
     * @param productOrderDO
     * @return
     */
    @Override
    public int add(ProductOrderDO productOrderDO) {
        return productOrderMapper.insert(productOrderDO);
    }


    /**
     * 通过订单号和账号查询
     * @param outTradeNo
     * @param accountNo
     * @return
     */
    @Override
    public ProductOrderDO findByOutTradeNoAndAccountNo(String outTradeNo, Long accountNo) {
        ProductOrderDO productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>()
                .eq("out_trade_no", outTradeNo)
                .eq("account_no", accountNo)
                .eq("del",0));
        return productOrderDO;
    }



    /**
     * 更新订单状态
     * @param outTradeNo
     * @param accountNo
     * @param newState
     * @param oldState
     * @return
     */
    @Override
    public int updateOrderPayState(String outTradeNo, Long accountNo, String newState, String oldState) {

        int rows = productOrderMapper.update(null, new UpdateWrapper<ProductOrderDO>()
                .eq("out_trade_no", outTradeNo)
                .eq("account_no", accountNo)
                .eq("state", oldState)
                .set("state", newState));

        return rows;
    }



    /**
     * 分页查看订单列表
     * @param page
     * @param size
     * @param accountNo
     * @param state
     * @return
     */
    @Override
    public Map<String, Object> page(int page, int size, Long accountNo, String state) {
        Page<ProductOrderDO> pageInfo = new Page<>(page, size);
        IPage<ProductOrderDO> orderDOIPage;
        if (StringUtils.isBlank(state)) {
            orderDOIPage = productOrderMapper.selectPage(pageInfo,
                    new QueryWrapper<ProductOrderDO>()
                            .eq("account_no", accountNo)
                            .eq("del",0)
                            .orderByDesc("gmt_create"));
        } else {
            orderDOIPage = productOrderMapper.selectPage(pageInfo, new QueryWrapper<ProductOrderDO>()
                    .eq("account_no", accountNo)
                    .eq("state", state)
                    .eq("del",0)
                    .orderByDesc("gmt_create"));
        }
        List<ProductOrderDO> orderDOIPageRecords = orderDOIPage.getRecords();
        List<ProductOrderVO> productOrderVOList = orderDOIPageRecords.stream().map(obj -> {
            ProductOrderVO productOrderVO = new ProductOrderVO();
            BeanUtils.copyProperties(obj, productOrderVO);
            return productOrderVO;
        }).collect(Collectors.toList());

        Map<String,Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",orderDOIPage.getTotal());
        pageMap.put("total_page",orderDOIPage.getPages());
        pageMap.put("current_data",productOrderVOList);
        return pageMap;
    }


    /**
     * 删除(逻辑删除)
     * @param productOrderId
     * @param accountNo
     * @return
     */
    @Override
    public int del(Long productOrderId, Long accountNo) {
        int rows = productOrderMapper.update(null, new UpdateWrapper<ProductOrderDO>()
                .eq("id", productOrderId)
                .eq("account_no", accountNo)
                .set("del", 1));
        return rows;
    }
}