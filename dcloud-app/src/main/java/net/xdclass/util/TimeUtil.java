package net.xdclass.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author 刘森飚
 * @since 2023-02-03
 */

public class TimeUtil {


    /**
     * 默认日期格式
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    private static final String DEFAULT_PATTERN_WITH_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期格式
     */
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER  = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

    /**
     * 默认日期时间格式
     */
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER  = DateTimeFormatter.ofPattern(DEFAULT_PATTERN_WITH_TIME);


    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();


    /**
     * LocalDateTime 转 字符串，指定日期格式
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String format(LocalDateTime localDateTime, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String timeStr = formatter.format(localDateTime.atZone(DEFAULT_ZONE_ID));
        return timeStr;
    }


    /**
     * Date 转 字符串, 指定日期格式
     * @param time
     * @param pattern
     * @return
     */
    public static String format(Date time, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String timeStr = formatter.format(time.toInstant().atZone(DEFAULT_ZONE_ID));
        return timeStr;
    }

    /**
     *  Date 转 字符串，默认日期格式
     * @param time
     * @return
     */
    public static String format(Date time){

        String timeStr = DEFAULT_DATE_FORMATTER.format(time.toInstant().atZone(DEFAULT_ZONE_ID));
        return timeStr;
    }

    /**
     *  Date 转 字符串，默认日期格式
     * @param timestamp
     * @return
     */
    public static String format(Long timestamp){

        String timeStr = DEFAULT_DATE_FORMATTER.format(new Date(timestamp).toInstant().atZone(DEFAULT_ZONE_ID));
        return timeStr;
    }



    /**
     * timestamp 转 字符串，指定日期格式
     *
     * @param timestamp
     * @return
     */
    public static String formatWithTime(long timestamp) {
        String timeStr = DEFAULT_DATE_TIME_FORMATTER.format(new Date(timestamp).toInstant().atZone(DEFAULT_ZONE_ID));
        return timeStr;
    }


    /**
     * 字符串 转 Date
     *
     * @param time
     * @return
     */
    public static Date strToDate(String time) {
        LocalDateTime localDateTime = LocalDateTime.parse(time, DEFAULT_DATE_FORMATTER);
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());

    }
}