package net.xdclass.manager;

import net.xdclass.model.AccountDO;

import java.util.List;

/**
 * @author 刘森飚
 * @since 2023-02-06
 */
public interface AccountManager {

    /**
     * 新增账号
     * @param accountDO
     * @return
     */
    int insert(AccountDO accountDO);


    /**
     * 通过手机号查询
     * @param phone
     * @return
     */
    List<AccountDO> findByPhone(String phone);


    /**
     * 查看个人信息
     * @param accountNo
     * @return
     */
    AccountDO detail(long accountNo);
}
