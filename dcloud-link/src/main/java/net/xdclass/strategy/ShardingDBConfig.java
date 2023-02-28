package net.xdclass.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description
 * @Author：刘森飚
 **/

public class ShardingDBConfig {

    /**
     * 存储数据库位置编号
     */
    private static final List<String> dbPrefixList = new ArrayList<>();

    //配置启用那些库的前缀
    static {
        dbPrefixList.add("0");
        dbPrefixList.add("1");
        dbPrefixList.add("a");
    }


    /**
     * 获取随机的前缀
     * @return
     */
    public static String getRandomDBPrefix(String code){

        //hashCode是固定的
        int hashCode = code.hashCode();
        int index = Math.abs(hashCode) % dbPrefixList.size();
        return dbPrefixList.get(index);
    }
}