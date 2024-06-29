
<p align="center">
	<img alt="logo" height="50" src="https://docs.aqchat.run/aqchat.svg">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">AQChat</h1>
<h4 align="center">一个已接入AI✨的极速、便捷的匿名在线即时AI聊天室。</h4>
<p align="center">
	<a href="https://gitee.com/howcode/aq-chat-server"><img src="https://gitee.com/howcode/aq-chat-server/badge/star.svg"></a>
    <a href="https://gitee.com/howcode/aq-chat-server"><img src="https://gitee.com/howcode/aq-chat-server/badge/fork.svg"></a>
    <a href="https://gitee.com/howcode/aq-chat-server"><img src="https://img.shields.io/badge/License-Apache--2.0-brightgreen.svg"></a>
</p>

[官网文档](https://docs.aqchat.run/)

```
无偿开源！你们的Star是我的动力！
```

### 介绍
AQChat 一个已接入AI✨的极速、便捷的匿名在线即时AI聊天室。
<br/>
<br/>
- 对标游戏后端开发，采用Netty作为通讯框架，支持高并发，高性能的即时通讯
- 全程无需HTTP协议，支持文本、图片、文件、音频、视频的发送和接收
- 消息提醒
- 消息撤回重新编辑
- 艾特成员/AI助手
- ······


### 其他仓库

>本项目为服务端代码

WEB端: <a href="https://github.com/zsdddz/AQChat">AQChat</a><br/>
移动端: <a href="https://gitee.com/ghosthhf/aqchat-mobile">AQChat-Mobile</a>

### 项目预览
项目演示地址：https://www.aqchat.run 
>pc端访问默认访问WEB端地址,移动端访问跳转移动端地址

## 免责声明

- 通用性：本项目是作为一个开源项目提供的，开发者在法律允许的范围内不对软件的功能性、安全性或适用性提供任何形式的明示或暗示的保证
- 无担保：用户明确理解并同意，使用本软件的风险完全由用户自己承担，软件以"现状"和"现有"基础提供。开发者不提供任何形式的担保，无论是明示还是暗示的，包括但不限于适销性、特定用途的适用性和非侵权的担保
- 风险承担：在任何情况下，开发者或其供应商都不对任何直接的、间接的、偶然的、特殊的、惩罚性的或后果性的损害承担责任，包括但不限于使用本软件产生的利润损失、业务中断、个人信息泄露或其他商业损害或损失
- 合法性：所有在本项目上进行二次开发的用户，都需承诺将本软件用于合法目的，并自行负责遵守当地的法律和法规
- 修改和版本：开发者有权在任何时间修改软件的功能或特性，以及本免责声明的任何部分，并且这些修改可能会以软件更新的形式体现

最终解释权：本免责声明的最终解释权归开发者所有


## 项目架构说明
| 模块               | 说明                        |
|------------------|---------------------------|
| aqchat-common    | AQChat项目公共模块，包含一些工具类和常量   |
| aqchat-framework | AQChat项目框架模块，包含对中间件的封装和配置 |
| aqchat-im        | AQChat项目IM核心模块，负责通讯协议以及和前端的连接 |
| aqchat-service   | AQChat项目业务逻辑层，包含业务逻辑的实现   |
| aqchat-ai        | AQChat项目AI功能模块，负责接入AI大模型  |


### 项目环境
| 环境           | 版本           |
|--------------|--------------|
| JDK          | 17           |
| Netty        | 4.1.89.Final |
| SpringBoot   | 3.2.5        |
| Mybatis-Plus | 3.5.5        |
| Protobuf     | 3            |
| MySQL        | 8.0.34       |
| Redis        | 6.0.8        |
| RocketMQ     | 4.4.0        |
| OSS       | 阿里云OSS       |
| AI大模型        | 通义千问MAX      |

### 项目截图
<img src="https://typora-record.oss-cn-shenzhen.aliyuncs.com/img/20240613_203832.gif">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/1715433594150.png" width="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/1715433609771.png" width="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/1715433659944.png" width="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/1715433812218.png">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/20240511212736.jpg" height="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/20240511212748.jpg" height="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/20240511212754.jpg" height="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/20240511212801.jpg" height="500">

# 部署文档

- 项目启动类位于`aq-chat-im`模块下 `com.howcode.aqchat.AQChatApplication`类
  - 启动前保证配置文件（aq-chat-im/src/main/resources/application.yml）中必填的配置补充完整
- 查看顶部官网文档

## 部署可能会用到的平台文档
<a href="https://help.aliyun.com/zh/oss/developer-reference/use-temporary-access-credentials-provided-by-sts-to-access-oss">使用STS临时访问凭证访问OSS</a><br/>
<a href="https://bailian.console.aliyun.com/">大模型服务平台百炼</a>

# 更新日志

### 2024.06.25

- 🩹 修复未感知用户断开

### 2024.06.18

- 🩹 修复房间内退出登录多次通知问题

### 2024.06.17

-  🩹 过滤xss攻击

### 2024.06.16

- ✨ 支持无AI启动

### 2024.06.10

- ✨ AI大模型接入

### 2024.05.26

- ✨ 消息撤回功能

### 2024.05.24

- ✅ 失败消息重发功能
- ✨ 房间成员离线通知

### 2024.05.23

- ✅ 房间成员列表
- ✨ 刷新页面仅同步加入房间后的消息

### 2024.05.18

- ✨ 成员退出房间通知

### 2024.05.11

- ✅ 刷新页面恢复用户登录
- ✅ 刷新页面同步聊天信息
- ✨ 退出登录

### 更前
- ✅ 发送消息
- ✨ 成员加入房间通知
- ✅ 房间号输入加入房间
- ✅ 创建房间