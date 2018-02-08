package com.gf.utils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Title: <br>
 * Packet:com.gf.utils<br>
 * Description: <br>
 * Author:GuoFu<br>
 * Create Date: 2018-2-6.<br>
 * Modify User: <br>
 * Modify Date: <br>
 * Modify Description: <br>
 */
public class CommonUtils {


    /**
     * 处理、获取本次SVN提交改变的内容.由文件夹--token 最后反转为 token--文件夹
     *
     * @param fileList         接收到的SVN传入的数据
     * @param pushMap          配置文件中配置的推送权限设置，进过getPushAuth方法转化后的
     * @param defaultPushToken
     * @return
     */
    public static Map<String, List<String>> dealChangedFileListMap(String fileList, Map<String, List<String>> pushMap, String defaultPushToken) {
        //A 新建文本文档.txt    其中，A：新增 D：删除 U：修改 UU：内容和属性改变 _U:创建文件夹
        String[] fileArr = fileList.split("\n");
        Map<String, List> tmp = new HashMap<>();
        for (String file : fileArr) {
            String type = getOperType(file.substring(0, 1));
            String changedFileName = file.substring(1).trim();
            String dirName = "";
            if (changedFileName.contains("/")) {
                //说明是根目录
                dirName = changedFileName.substring(0, changedFileName.indexOf("/"));
            }
            if (pushMap.containsKey(dirName)) {
                List<String> tokenList = pushMap.get(dirName);
                tmp.put("[" + type + "]_" + changedFileName, tokenList);
            } else {
                //使用默认token，推送全部修改内容
                List<String> tokenList = new ArrayList<>();
                tokenList.add(defaultPushToken);
                tmp.put("[" + type + "]_" + changedFileName, tokenList);
                LogHelper.i("路径：【" + dirName + "】-未在配置文件定义,发送给默认token:" + defaultPushToken);
            }
        }

        Set set = tmp.entrySet();
        Iterator iterator = set.iterator();
        Map<String, List<String>> tmpMap = new HashMap();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String fileName = (String) entry.getKey();
            List<String> tokenList = (List<String>) entry.getValue();
            for (String token : tokenList) {
                List<String> list = new ArrayList<>();
                if (tmpMap.containsKey(token)) {
                    list = tmpMap.get(token);
                }
                list.add(fileName);
                tmpMap.put(token, list);
            }
        }
        return tmpMap;
    }

    /**
     * 获取操作类型
     *
     * @param flag 类型标志
     * @return 返回： 新增、删除、修改、内容和属性修改、创建、未知操作
     */
    public static String getOperType(String flag) {
        switch (flag) {
            case "A":
                return "新增";
            case "D":
                return "删除";
            case "U":
                return "修改";
            case "UU":
                return "内容和属性修改";
            case "_U":
                return "创建";
            default:
                return "未知操作";
        }
    }

    /**
     * 处理配置文件中 msg_push_people 的设置,转为Map对象（key为目录，val为对应目录有权限接收推送消息的token列表）
     *
     * @param str msg_push_people 配置的内容，类似于：PrivateDoc-ef9b7cc968d5929faaef4d5e51aa28b8b1938aa3400bd5bf2f7478c0b1f4882d,CommonDoc-ef9b7cc968d5929faaef4d5e51aa28b8b1938aa3400bd5bf2f7478c0b1f4882d|9414638ead509bc4cf411a9188c9b754fb712d5bc9bb64f358e60fc9db570ba1
     * @return 返回Map对象。Map对象（key为目录，val为对应目录有权限接收推送消息的token列表）
     */
    public static Map<String, List<String>> getPushAuth(String str, String[] defaultPushToken) {
        //分割出文件夹
        String[] pushArr = str.split(",");
        Map<String, List<String>> pushMap = new HashMap<>();
        for (String item : pushArr) {
            List<String> tokenList = new ArrayList<>();
            //分割出token
            String[] dirAndTokenArr = item.split("-");
            LogHelper.i("目录：" + dirAndTokenArr[0]);
            //分割出token
            String[] tokensArr = null;
            if (dirAndTokenArr.length > 0 && "".equals(dirAndTokenArr[0].trim())) {
                LogHelper.i("配置文件中没有对msg_push_people进行配置，使用默认token");
                tokensArr = defaultPushToken;
            } else {
                tokensArr = dirAndTokenArr[1].split("\\|");
            }
            for (String token : tokensArr) {
                tokenList.add(token);
                LogHelper.i("\t组ID：" + token);
            }
            pushMap.put(dirAndTokenArr[0], tokenList);
        }
        return pushMap;
    }


    public static String getOSType() {
        String osName = System.getProperty("os.name");
        //linu系统
        if (Pattern.matches("Linux.*", osName)) {
            return "LINUX";
        } else if (Pattern.matches("Windows.*", osName)) {
            return "WINDOWS";
        } else {
            return "";
        }
    }

    public static String getRepName(String repPath) {
        String osType=getOSType();
        switch (osType) {
            case "LINUX":
                return repPath.substring(repPath.lastIndexOf("/") + 1);
            case "WINDOWS":
                return repPath.substring(repPath.lastIndexOf("\\") + 1);
            default:
                return "";
        }
    }
}
