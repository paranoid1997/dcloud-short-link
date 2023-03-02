package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.controller.request.AccountLoginRequest;
import net.xdclass.controller.request.AccountRegisterRequest;
import net.xdclass.enums.AuthTypeEnum;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.EventMessageType;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.AccountManager;
import net.xdclass.model.AccountDO;
import net.xdclass.mapper.AccountMapper;
import net.xdclass.model.EventMessage;
import net.xdclass.model.LoginUser;
import net.xdclass.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.IDUtil;
import net.xdclass.util.JWTUtil;
import net.xdclass.util.JsonData;
import net.xdclass.vo.AccountVO;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 刘森飚
 * @since 2023-01-09
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;


    /**
     * 免费流量包商品id
     */
    private static final Long FREE_TRAFFIC_PRODUCT_ID = 1L;

    /**
     * ⼿机验证码验证
     * 密码加密（TODO）
     * 账号唯⼀性检查(TODO)
     * 插⼊数据库
     * 新注册用户福利发放(TODO)
     * @param registerRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public JsonData register(AccountRegisterRequest registerRequest) {
        boolean checkCode = false;
        //判断验证码
        if (StringUtils.isNotBlank(registerRequest.getPhone())) {
            checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, registerRequest.getPhone(), registerRequest.getCode());
        }
        //验证码错误
        if (!checkCode) {
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }
        AccountDO accountDO = new AccountDO();
        BeanUtils.copyProperties(registerRequest,accountDO);
        //认证级别
        accountDO.setAuth(AuthTypeEnum.DEFAULT.name());
        //生成唯一的账号
        accountDO.setAccountNo(Long.valueOf(IDUtil.geneSnowFlakeID().toString()));
        //密码加密 密钥(盐)
        accountDO.setSecret("$1$" + CommonUtil.getStringNumRandom(8));
        String cryptPwd = Md5Crypt.md5Crypt(registerRequest.getPwd().getBytes(), accountDO.getSecret());
        accountDO.setPwd(cryptPwd);
        int rows = accountManager.insert(accountDO);
        log.info("rows:{},注册成功:{}",rows,accountDO);
        //用户注册成功，发放福利
        userRegisterInitTask(accountDO);
        return JsonData.buildSuccess();
    }


    /**
     * 用户登录逻辑
     * @param request
     * @return
     */
    @Override
    public JsonData login(AccountLoginRequest request) {
        List<AccountDO> accountDOList = accountManager.findByPhone(request.getPhone());
        if (accountDOList != null && accountDOList.size() == 1) {
            AccountDO accountDO = accountDOList.get(0);
            //前端传来的密码和数据库的盐进行加密,生成密文
            String md5Crypt = Md5Crypt.md5Crypt(request.getPwd().getBytes(), accountDO.getSecret());
            if (md5Crypt.equalsIgnoreCase(accountDO.getPwd())) {
                LoginUser loginUser = LoginUser.builder().build();
                BeanUtils.copyProperties(accountDO,loginUser);
                //生成TOKEN
                String token = JWTUtil.geneJsonWebTokne(loginUser);
                return JsonData.buildSuccess(token);
            }else {
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
        }else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
    }



    /**
     * 查询个人信息
     * @return
     */
    @Override
    public JsonData detail() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AccountDO accountDO = accountManager.detail(loginUser.getAccountNo());
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(accountDO,accountVO);
        return JsonData.buildSuccess(accountVO);
    }



    /**
     * 用户初始化福利，流量包
     * @param accountDO
     */
    private void userRegisterInitTask(AccountDO accountDO) {
        EventMessage eventMessage = EventMessage.builder()
                .messageId(IDUtil.geneSnowFlakeID().toString())
                .accountNo(accountDO.getAccountNo())
                .eventMessageType(EventMessageType.TRAFFIC_FREE_INIT.name())
                .bizId(FREE_TRAFFIC_PRODUCT_ID.toString())
                .build();

        //发送发放流量包消息
        rabbitTemplate.convertAndSend(rabbitMQConfig.getTrafficEventExchange(),
                rabbitMQConfig.getTrafficFreeInitRoutingKey(),eventMessage);
    }
}