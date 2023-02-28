package net.xdclass.component;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.vo.PayInfoVO;
import org.springframework.stereotype.Service;

/**
 * @author 刘森飚
 * @since 2023-02-05
 * 支付宝支付
 */

@Service
@Slf4j
public class AliPayStrategy implements  PayStrategy{

    @Override
    public String unifiedOrder(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String refund(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String queryPayStatus(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String closeOrder(PayInfoVO payInfoVO) {
        return null;
    }
}
