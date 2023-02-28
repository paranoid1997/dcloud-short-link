package net.xdclass.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;

/**
 * @author 刘森飚
 * @since 2023-01-30
 */


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 订单类型
     */
    private Long productId;

    /**
     * 商品标题
     */
    private String productTitle;

    /**
     * 商品单价
     */
    private BigDecimal productAmount;

    /**
     * 商品快照
     */
    private String productSnapshot;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 订单唯一标识
     */
    private String outTradeNo;

    /**
     * NEW 未支付订单,PAY已经支付订单,CANCEL超时取消订单
     */
    private String state;

    /**
     * 订单生成时间
     */
    private Date createTime;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单实际支付价格
     */
    private BigDecimal payAmount;

    /**
     * 支付类型，微信-银行-支付宝
     */
    private String payType;

    /**
     * 账号昵称
     */
    private String nickname;

    /**
     * 用户id
     */
    private Long accountNo;

    /**
     * 0表示未删除，1表示已经删除
     */
    private Integer del;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 发票类型：0->不开发票；1->电子发票；2->纸质发票
     */
    private String billType;

    /**
     * 发票抬头
     */
    private String billHeader;

    /**
     * 发票内容
     */
    private String billContent;

    /**
     * 发票收票人电话
     */
    private String billReceiverPhone;

    /**
     * 发票收票人邮箱
     */
    private String billReceiverEmail;


}
