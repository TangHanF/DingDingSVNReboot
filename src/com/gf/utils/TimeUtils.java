package com.gf.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * Created by GuoF 2015年1月29日21:03:34
 */
public class TimeUtils {

    public static void main(String[] args) {
        System.out.println(getFormatTime("hhmmss"));
        System.out.println(parseStrTime("20150607124533"));
    }

    public static String getFormatTime(String style) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        return sdf.format(date);
    }

    /**
     * 获取当前时间，格式：yyyy/MM/dd hh:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return sdf.format(date);
    }

    public static String parseStrTime(String timeStr, String timeStyle) {
        String applyForTime = timeStr;
        Date d = null;
        try {
            d = new SimpleDateFormat(timeStyle).parse(applyForTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp t = new Timestamp(d.getTime());
        return t.toString();
    }

    /**
     * 默认处理yyyyMMddHHmmss，返回yyyy-MM-dd HH:mm:ss格式
     *
     * @param timeStr
     * @return
     * @throws ParseException
     */
    public static String parseStrTime(String timeStr) {
        String applyForTime = timeStr;
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyyMMddHHmmss").parse(applyForTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(d);

        return date;
    }


    /**
     *
     * @param timeStr
     * @param timeFormat timeStr当前格式
     * @param convertsTyle 转换为的格式
     * @return
     */
    public static String parseStrTime(String timeStr,String  timeFormat ,String convertsTyle) {
        String applyForTime = timeStr;
        Date d = null;
        try {
            d = new SimpleDateFormat(timeFormat).parse(applyForTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(convertsTyle);
        String date = simpleDateFormat.format(d);

        return date;
    }


    /**
     * 获取指定格式时间的TimeStamp类型。
     *
     * @param dateStr   String 要转换的指定格式时间
     * @param dateStyle String 对应的时间样式。注意：dateStyle要与dateStr的格式一致，例如：dateStr为2014-12-01 13:12:11 则，dateStyle为 yyyy-MM-dd hh:mm:ss
     * @return Timestamp 指定格式时间的TimeStamp类型
     */
    public static String getDiyTime_Timestamp(String dateStr, String dateStyle) {
        try {
            Date date = new SimpleDateFormat(dateStyle).parse(dateStr);
            Timestamp timestamp = new Timestamp(date.getTime());
            return timestamp.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDiyTime(String dateStr) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
            Timestamp timestamp = new Timestamp(date.getTime());
            return timestamp.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给指定时间加上指定时间
     *
     * @param date
     * @param num
     * @return
     */
    public static Date getAddDay(Date date, int num,String type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int typeInt=0;
        switch (type){
            case "y":typeInt=Calendar.YEAR;break;
            case "M":typeInt=Calendar.MONTH;break;
            case "d":typeInt=Calendar.DATE;break;
            case "h":typeInt=Calendar.HOUR;break;
            case "m":typeInt=Calendar.MINUTE;break;
            case "s":typeInt=Calendar.SECOND;break;
            case "S":typeInt=Calendar.MILLISECOND;break;
        }
        cal.add(typeInt, num);
        Date date1 = cal.getTime();

        return date1;
    }
}
