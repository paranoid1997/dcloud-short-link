package net.xdclass.controller;

import net.xdclass.service.ProductService;
import net.xdclass.util.JsonData;
import net.xdclass.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author 刘森飚
 **/
@RestController
@RequestMapping("/api/product/v1")
public class ProductController {

    @Autowired
    private ProductService productService;


    /**
     * 查看商品列表接口
     * @return
     */
    @GetMapping("list")
    public JsonData list(){
        List<ProductVO> list = productService.list();
        return JsonData.buildSuccess(list);
    }


    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    @GetMapping("detail/{product_id}")
    public JsonData detail(@PathVariable("product_id") long productId){
        ProductVO productVO = productService.findDetailById(productId);
        return JsonData.buildSuccess(productVO);
    }
}
