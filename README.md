# AQChatServer

### 介绍
AQChat 一个极速、便捷的匿名在线即时聊天室。
<br/>
<br/>
对标游戏后端开发，采用Netty作为通讯框架，支持高并发，高性能的即时通讯。全程无需HTTP协议，支持文本、图片、文件、音频、视频的发送和接收。
<br/>
<br/>
本项目为服务端代码，客户端代码请查看：<a href="https://gitee.com/howcode/aq-chat">AQChat</a>


项目演示地址：https://demo.aqchat.run


### 项目架构说明
| 模块               | 说明                            |
|------------------|-------------------------------|
| aqchat-common    | AQChat项目公共模块，包含一些工具类和常量       |
| aqchat-framework | AQChat项目框架模块，包含对中间件的封装和配置     |
| aqchat-im        | AQChat项目IM核心模块，负责通讯协议以及和前端的连接 |
| aqchat-service   | AQChat项目业务逻辑层，包含业务逻辑的实现       |


### 项目环境
JDK17 <br/>
Netty 4.1.89.Final <br/>
SpringBoot 3.2.5 <br/>
Mybatis-Plus 3.5.3.2<br/>
protobuf 3 <br/>
MySQL 8.0.34 <br/>
Redis 6.0.8 <br/>
RocketMQ 4.4.0 <br/>
阿里云OSS

