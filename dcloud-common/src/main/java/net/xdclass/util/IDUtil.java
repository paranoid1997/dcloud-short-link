package net.xdclass.util;

import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;

/**
 * @Description
 * @Author 刘森飚
 **/

public class IDUtil {


    private static SnowflakeShardingKeyGenerator shardingKeyGenerator = new SnowflakeShardingKeyGenerator();

    /**
     * 雪花算法生成器
     * 用来生成唯一的账号
     * @return
     */
    public static   Comparable<?> geneSnowFlakeID(){

        return shardingKeyGenerator.generateKey();
    }

}
