package com.gf;

import com.alibaba.fastjson.JSON;
import com.dingtalk.chatbot.message.MarkdownMessage;

import java.util.*;

/**
 * Title: MarkdownMessage扩展类<br>
 * Packet:com.gf<br>
 * Description: 钉钉官方提供的MarkdownMessage类没有@功能，此扩展类扩展了此功能<br>
 * Author:GuoFu<br>
 * Create Date: 2018-2-8.<br>
 * Modify User: <br>
 * Modify Date: <br>
 * Modify Description: <br>
 */
public class MarkdownMessageExtend extends MarkdownMessage {
    private boolean isAtAll;
    private String title;
    private List<String> items = new ArrayList();

    @Override
    public void add(String text) {
        this.items.add(text);
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setIsAtAll(boolean atAll) {
        this.isAtAll = atAll;
    }


    @Override
    public String toJsonString() {
        Map<String, Object> result = new HashMap();
        result.put("msgtype", "markdown");
        Map<String, Object> markdown = new HashMap();
        markdown.put("title", this.title);
        StringBuffer markdownText = new StringBuffer();
        Iterator iterator = this.items.iterator();

        while (iterator.hasNext()) {
            String item = (String) iterator.next();
            markdownText.append(item + "\n");
        }

        Map<String, Object> atItems = new HashMap();
        if (this.isAtAll) {
            atItems.put("isAtAll", this.isAtAll);
        }
        result.put("at", atItems);


        markdown.put("text", markdownText.toString());
        result.put("markdown", markdown);

        System.out.println(JSON.toJSONString(result));
        return JSON.toJSONString(result);
    }
}
