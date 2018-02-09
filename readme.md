# 钉钉SVN提交消息推送机器人

#### 效果图如下：

![消息推送效果图](https://github.com/TangHanF/DingDingSVNReboot/raw/master/img/pic1.jpg)


------------

#### 配置文件config.properties说明
- `open_push`:是否开启消息推送，取值`true` `false`

- `msg_type`:消息类型，目前支持text、markdown类型，取值：`text` `markdown`

- `at_all`:是否推送消息后@所有人，取值`true` `false`

- `open_log`:是否开启日志记录，取值`true` `false`

- `log_path`:日志保存路径

- `users_list`:SVN用户名和姓名映射，格式：

    ** svn用户名1-姓名1,svn用户名2-姓名2,...**

	用户名和姓名之间可以用“-”或“=”进行分割，多个映射项之间用“,”分割

- `default_push_token`:默认推送的token，当相关token没有配置时使用该默认值，目前仅支持一个

- `msg_push_people`:接收推送消息的用户表，格式：

	**文件夹1-token1|token2|...,文件夹2-token1|token2|,...**

	如果提交的内容不再上面定义的文件夹内，那么默认将消息推送到`default_push_token`


------------

#### jar包调用方式
`java -jar DingDingReboot.jar 配置文件路径 提交人 SVN库目录 提交时间 日志 变动的文件列表 版本号`

#### SVN钩子post-commit内容
```shell
#!/bin/sh

export LANG=zh_CN.UTF-8
REPOS="$1"
REV="$2"

# 作者
AUTHOR=$(svnlook author -r $REV $REPOS)

# 备注信息
MESSAGE=$(svnlook log $REPOS -r $REV)

# 本次提交的内容，返回格式： A 新建文本文档.txt    其中，A：新增 D：删除 U：修改 UU：内容和属性改变
CHANGEDLIST=$(svnlook changed -r $REV $REPOS)

# 修改日期
DATE=$(svnlook date -r $REV $REPOS)

java -jar /var/svnroot/DingDingReboot.jar /var/svnroot/config.properties "$AUTHOR" "$REPOS" "$DATE" "$MESSAGE" "$CHANGEDLIST" "$REV"
```
