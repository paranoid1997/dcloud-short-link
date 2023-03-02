package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.AccountManager;
import net.xdclass.mapper.AccountMapper;
import net.xdclass.model.AccountDO;
import net.xdclass.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author 刘森飚
 **/

@Component
@Slf4j
public class AccountManagerImpl implements AccountManager {

    @Autowired
    private AccountMapper accountMapper;


    /**
     * 新增账号
     * @param accountDO
     * @return
     */
    @Override
    public int insert(AccountDO accountDO) {

        return accountMapper.insert(accountDO);
    }



    /**
     * 通过手机号查询
     * @param phone
     * @return
     */
    @Override
    public List<AccountDO> findByPhone(String phone) {

        List<AccountDO> accountDOList = accountMapper.selectList
                (new QueryWrapper<AccountDO>().eq("phone", phone));
        return accountDOList;
    }


    /**
     * 查看个人信息
     * @param accountNo
     * @return
     */
    @Override
    public AccountDO detail(long accountNo) {
        AccountDO accountDO = accountMapper.selectOne(new QueryWrapper<AccountDO>()
                .eq("account_no", accountNo));
        return accountDO;

    }
}
