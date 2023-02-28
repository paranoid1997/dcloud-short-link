package net.xdclass.manager;

import net.xdclass.model.ShortLinkDO;

public interface ShortLinkManager {

    /**
     * 新增
     * @param shortLinkDO
     * @return
     */
    int addShortLink(ShortLinkDO shortLinkDO);

    /**
     * 根据短链码找短链
     * @param shortLinkCode
     * @return
     */
    ShortLinkDO findByShortLinkCode(String shortLinkCode);

    /**
     * 冗余双写C端-删除操作
     * @param shortLinkDO
     * @return
     */
    int del(ShortLinkDO shortLinkDO);

    /**
     * 冗余双写C端-更新操作
     * @param shortLinkDO
     * @return
     */
    int update(ShortLinkDO shortLinkDO);
}
