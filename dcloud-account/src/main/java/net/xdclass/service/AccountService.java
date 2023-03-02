package net.xdclass.service;

import net.xdclass.controller.request.AccountLoginRequest;
import net.xdclass.controller.request.AccountRegisterRequest;
import net.xdclass.model.AccountDO;
import com.baomidou.mybatisplus.extension.service.IService;
import net.xdclass.util.JsonData;

/**
 * @author 刘森飚
 * @since 2023-01-09
 */
public interface AccountService {
    /**
     * 用户注册
     * @param registerRequest
     * @return
     */
    JsonData register(AccountRegisterRequest registerRequest);


    /**
     * 用户登录
     * @param request
     * @return
     */
    JsonData login(AccountLoginRequest request);


    /**
     * 查询个人信息
     * @return
     */
    JsonData detail();

}