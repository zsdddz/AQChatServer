# AQChatServer

### 介绍
AQChat 一个极速、便捷的匿名在线即时聊天室。
<br/>
<br/>
对标游戏后端开发，采用Netty作为通讯框架，支持高并发，高性能的即时通讯。全程无需HTTP协议，支持文本、图片、文件、音频、视频的发送和接收。
<br/>
<br/>
本项目为服务端代码，客户端代码请查看：<a href="https://gitee.com/howcode/aq-chat">AQChat</a>


项目地址：https://aqchat.run


### 项目架构说明
| 模块               | 说明                            |
|------------------|-------------------------------|
| aqchat-common    | AQChat项目公共模块，包含一些工具类和常量       |
| aqchat-framework | AQChat项目框架模块，包含对中间件的封装和配置     |
| aqchat-im        | AQChat项目IM核心模块，负责通讯协议以及和前端的连接 |
| aqchat-service   | AQChat项目业务逻辑层，包含业务逻辑的实现       |


### 项目环境
| 环境       | 版本       |
|----------|----------|
| JDK      | 17     |
| Netty    | 4.1.89.Final   |
| SpringBoot | 3.2.5    |
| Mybatis-Plus|3.5.5|
| Protobuf | 3        |
| MySQL    | 8.0.34   |
| Redis    | 6.0.8    |
| RocketMQ | 4.4.0    |
| 阿里云OSS  |          |

### 项目截图

### 项目部署
1. 克隆项目到本地
```shell
git clone https://gitee.com/howcode/aq-chat-server.git
```
修改配置文件`application.yml`中的数据库、Redis、RocketMQ、阿里云OSS等配置

2. 打包项目
进入`aq-chat-im`模块，执行以下命令
```shell
mvn clean package -Dmaven.test.skip=true
```
