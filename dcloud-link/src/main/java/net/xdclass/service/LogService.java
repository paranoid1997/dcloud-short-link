package net.xdclass.service;

import net.xdclass.util.JsonData;

import javax.servlet.http.HttpServletRequest;


/**
 * @author 刘森飚
 * @since 2023-02-13
 */

public interface LogService {

    /**
     * 记录日志
     * @param request
     * @param shortLinkCode
     * @param accountNo
     * @return
     */
    void recordShortLinkLog(HttpServletRequest request, String shortLinkCode, Long accountNo);

}
