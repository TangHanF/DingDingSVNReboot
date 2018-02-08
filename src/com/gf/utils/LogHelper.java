package com.gf.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Title:日志工具类
 * Packet:com.gh.com.gh.utils
 * Description:日志已做加密处理
 * Author:郭富
 * Create Date: 2015/4/27.
 * Modify User:
 * Modify Date:
 * Modify Description:
 */


public class LogHelper implements Serializable {
    private static String currentDay;
    Class c = null;

    static String logFilePath = "";

    public LogHelper(Class c) {
        this.c = c;
        currentDay = TimeUtils.getFormatTime("yyyy-MM-dd");
        logFilePath = BufferClass.LOGPATH + File.separator + currentDay + ".log";
        FileUtils.mkdir(BufferClass.LOGPATH);
    }

    public static void i(String msg) {
        if ("true".equals(BufferClass.OPENLOG)) {
            currentDay = TimeUtils.getFormatTime("yyyy-MM-dd");
            logFilePath = BufferClass.LOGPATH + File.separator + currentDay + ".log";
            FileUtils.mkdir(BufferClass.LOGPATH);

            if (!"".equals(logFilePath)) {
                try {
                    if (FileUtils.existFile(logFilePath) == false) {
                        FileUtils.createNewFile(logFilePath, "");
                    }
                    System.out.println(msg);
                    //未加密
                    FileUtils.appendToFile_A(logFilePath, "【" + TimeUtils.getFormatTime("yyyy-MM-dd HH:mm:ss") + "】" + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(msg);
            }
        } else {
            System.out.println(msg);
        }

    }


    public static void e(Exception ex) {
        try {
            if (FileUtils.existFile(logFilePath) == false) {
                FileUtils.createNewFile(logFilePath, "");
            }
            FileUtils.appendToFile_A(logFilePath, ("【" + TimeUtils.getFormatTime("yyyy-MM-dd HH:mm:ss") + "】" + getStackMsg(ex)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getStackMsg(Exception e) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }

    private static String getStackMsg(Throwable e) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }


}
