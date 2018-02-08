package com.gf;

import com.dingtalk.chatbot.DingtalkChatbotClient;
import com.dingtalk.chatbot.SendResult;
import com.dingtalk.chatbot.message.TextMessage;
import com.gf.utils.BufferClass;
import com.gf.utils.CommonUtils;
import com.gf.utils.LogHelper;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Title: 钉钉机器人SVN事件钩子入口<br>
 * Packet:com.gf<br>
 * Description: <br>
 * Author:GuoFu<br>
 * Create Date: 2018-2-4.<br>
 * Modify User: <br>
 * Modify Date: <br>
 * Modify Description: <br>
 */
public class SendMsgToReboot {

    static String openPush = "false";
    static String defaultPushToken = "";
    static String msgType = "text";
    static String atAll = "false";

    static String msg_push_people = "";
    static Map<String, String> userMap = null;
    static Map<String, List<String>> pushMap = null;
    /**
     * 钉钉机器人URL模板
     */
    private final static String TEXTURL_ = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    public static void main(String[] args) throws Exception {
        //args = new String[]{"D:\\IDEA_Project\\DingDingRobot\\dingdingreboot\\config.properties", "guof", "D:\\IDEA_Project\\DingDingRobot\\dingdingreboot", "2018-02-07 12:12:12 3333333", "调试模式测试", "A   CommonDoc/001 信贷业务群-郭富/ESB开发流程培训.pptx\n" +"A   CommonDoc/001 信贷业务群-郭富/ESB服务治理培训.pptx", "12"};

        initParam(args[0]);

        switch (msgType) {
            case "text":
                prepareTextMsgAndSend(args);
                break;
            case "markdown":
                prepareMarkDownMsgAndSend(args, CommonUtils.dealChangedFileListMap(args[5], pushMap, defaultPushToken));
                break;
            default:
                prepareTextMsgAndSend(args);
        }
    }


    /**
     * 初始化参数信息
     *
     * @param cfgFilePath 配置文件路径
     * @throws IOException
     */
    private static void initParam(String cfgFilePath) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(cfgFilePath));
            Properties properties = new Properties();
            //解决中文乱码问题
            properties.load(new InputStreamReader(inputStream, Charset.forName("utf-8")));
            openPush = properties.getProperty("open_push");
            defaultPushToken = properties.getProperty("default_push_token");
            msgType = properties.getProperty("msg_type");
            atAll = properties.getProperty("at_all");
            BufferClass.OPENLOG = properties.getProperty("open_log");
            BufferClass.LOGPATH = properties.getProperty("log_path");
            msg_push_people = properties.getProperty("msg_push_people");
            pushMap = CommonUtils.getPushAuth(msg_push_people, new String[]{defaultPushToken});

            LogHelper.i("\n\n>>>>>>>>>>>>>>>>>>>参数初始化完成<<<<<<<<<<<<<<<<<<<");
            LogHelper.i("获取到的推送群权限信息：" + msg_push_people);
            LogHelper.i("配置的默认token：" + defaultPushToken);

            String str = properties.getProperty("users_list");
            String[] userArr = str.split(",");
            userMap = new HashMap<>();
            for (String item : userArr) {
                String[] tmp = null;
                if (item.contains("-")) {
                    tmp = item.split("-");
                } else if (item.contains("=")) {
                    tmp = item.split("=");
                }
                userMap.put(tmp[0], tmp[1]);
            }
        } catch (FileNotFoundException fnfex) {
            LogHelper.e(fnfex);
            throw new FileNotFoundException("[config.properties]配置文件不存在");
        }
    }

    /**
     * 组织文本消息并发送
     *
     * @param args
     * @return
     */
    private static void prepareTextMsgAndSend(String[] args) {
        String msg;
        msg = ">>>>>>>>>>>>>>【通知】SVN有提交的内容>>>>>>>>>>>>>>\n\n";
        int argsLen = args.length;

        // <editor-fold desc="组织消息">
        switch (argsLen) {
            case 3:
                msg += "【提 交 者】" + (userMap.get(args[1]) != null ? userMap.get(args[1]) : args[1]) + "\n";
                msg += "【目    录】" + args[2] + "\n";
                break;
            case 4:
                msg += "【提 交 者】" + (userMap.get(args[1]) != null ? userMap.get(args[1]) : args[1]) + "\n";
                msg += "【目    录】" + args[2] + "\n";
                msg += "【时    间】" + args[3].substring(0, 19).trim() + "\n";
                break;
            case 5:
                msg += "【提 交 者】" + (userMap.get(args[1]) != null ? userMap.get(args[1]) : args[1]) + "\n";
                msg += "【目    录】" + args[2] + "\n";
                msg += "【时    间】" + args[3].substring(0, 19).trim() + "\n";
                msg += "【日    志】\n\t" + args[4] + "\n";
                break;
            case 6:
                msg += "【提 交 者】" + (userMap.get(args[1]) != null ? userMap.get(args[1]) : args[1]) + "\n";
                msg += "【目    录】" + args[2] + "\n";
                msg += "【时    间】" + args[3].substring(0, 19).trim() + "\n";
                msg += "【日    志】\n\t" + args[4] + "\n";
                msg += "【相关操作】\n%s" + "\n";
                break;
            case 7:
                msg += "【提 交 者】" + (userMap.get(args[1]) != null ? userMap.get(args[1]) : args[1]) + "\n";
                msg += "【目    录】" + args[2] + "\n";
                msg += "【时    间】" + args[3].substring(0, 19).trim() + "\n";
                msg += "【日    志】\n\t" + args[4] + "\n";
                msg += "【相关操作】\n%s" + "\n";
                msg += "【版 本 号】" + args[6] + "\n";
                break;
            default:
                msg = args.length != 0 ? args[0] : "SVN机器人消息...";
        }
        // </editor-fold>


        //发送消息

        Map<String, List<String>> map = CommonUtils.dealChangedFileListMap(args[5], pushMap, defaultPushToken);
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            String res = "";
            Object tmpMsg;
            Map.Entry entry = (Map.Entry) iterator.next();
            String token = (String) entry.getKey();
            List<String> fileList = (List<String>) entry.getValue();

            switch (msgType) {
                case "text":
                    for (int i = 0; i < fileList.size(); i++) {
                        String fileName = fileList.get(i);
                        res += (i + 1) + "、" + fileName + "\n";
                    }
                    tmpMsg = String.format(String.valueOf(msg), res);
                    break;
                case "markdown":
                    tmpMsg = msg;
                    break;
                default:
                    tmpMsg = String.format(String.valueOf(msg), res);
            }
            sendTextMsg(openPush, String.valueOf(tmpMsg), token, atAll);
        }
    }

    /**
     * 组织markdown消息并发送
     *
     * @param args
     * @param pushMap
     */
    private static void prepareMarkDownMsgAndSend(String[] args, Map<String, List<String>> pushMap) {
        ArrayList<String> orderList = new ArrayList<>();
        Map<String, List<String>> map = pushMap;
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            orderList.clear();
            Map.Entry entry = (Map.Entry) iterator.next();
            String token = (String) entry.getKey();
            List<String> fileList = (List<String>) entry.getValue();
            for (int i = 0; i < fileList.size(); i++) {
                String fileName = fileList.get(i);
                //orderList.add((i + 1) + "、" + fileName);
                orderList.add(fileName);
            }
            //创建文本消息对象并初始化
            MarkdownMessageExtend message = new MarkdownMessageExtend();
            message.setIsAtAll("true".equals(atAll) ? true : false);
            message.setTitle("SVN提交动态");

            message.add(MarkdownMessageExtend.getBoldText("【提交人】"));
            message.add(MarkdownMessageExtend.getReferenceText((userMap.get(args[1]) != null ? userMap.get(args[1]) : args[1])));
            message.add("\n\n");

            message.add(MarkdownMessageExtend.getBoldText("【版本库名】"));
            message.add(MarkdownMessageExtend.getReferenceText(CommonUtils.getRepName(args[2])));
            message.add("\n\n");

            message.add(MarkdownMessageExtend.getBoldText("【提交说明】"));
            message.add("\n\n");
            message.add(MarkdownMessageExtend.getReferenceText(args[4].trim().length() == 0 ? "无描述" : args[4].trim()));
            message.add("\n\n");

            message.add(MarkdownMessageExtend.getBoldText("【提交时间】"));
            message.add(MarkdownMessageExtend.getReferenceText(args[3].substring(0, 19).trim()));
            message.add("\n\n");

            message.add(MarkdownMessageExtend.getBoldText("【相关操作】"));
            message.add(MarkdownMessageExtend.getOrderListText(orderList));
            message.add("\n\n");

            message.add(MarkdownMessageExtend.getBoldText("【版本号】"));
            message.add(MarkdownMessageExtend.getReferenceText(args[6]));
            message.add("\n\n");

            sendMarkDownMsg(openPush, message, token);
        }

    }

    /**
     * 发送文本消息
     *
     * @param openPush
     * @param tmpMsg
     * @param token
     * @throws IOException
     */
    private static void sendTextMsg(String openPush, String tmpMsg, String token, String atAll) {
        try {//创建钉钉机器人消息封装对象
            DingtalkChatbotClient client = new DingtalkChatbotClient();
            //创建文本消息对象并初始化
            TextMessage message = new TextMessage(tmpMsg);
            if ("true".equals(atAll)) {
                //@所有人
                message.setIsAtAll(true);
            }
            if ("true".equals(openPush)) {
                //发送消息
                SendResult result = client.send(String.format(TEXTURL_, token), message);
                LogHelper.i("返回结果：" + result.toString());
            } else {
                LogHelper.i("未开启消息推送...");
            }
        } catch (IOException e) {
            LogHelper.e(e);
        }
    }

    /**
     * 发送MarkDown消息
     *
     * @param openPush 是否开启消息推送
     * @param message  MarkdownMessage对象实例
     * @param token
     * @throws IOException
     */
    private static void sendMarkDownMsg(String openPush, MarkdownMessageExtend message, String token) {
        try {//创建钉钉机器人消息封装对象
            DingtalkChatbotClient client = new DingtalkChatbotClient();
            //创建文本消息对象并初始化
            if ("true".equals(openPush)) {
                SendResult result = client.send(String.format(TEXTURL_, token), message);
                LogHelper.i(result.toString());
            } else {
                LogHelper.i("未开启消息推送...");
            }
        } catch (IOException e) {
            LogHelper.e(e);
        }
    }
}

