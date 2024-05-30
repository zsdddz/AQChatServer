# AQChatServer

### 介绍
AQChat 一个极速、便捷的匿名在线即时聊天室。
<br/>
<br/>
对标游戏后端开发，采用Netty作为通讯框架，支持高并发，高性能的即时通讯。全程无需HTTP协议，支持文本、图片、文件、音频、视频的发送和接收。
<br/>
<br/>
本项目为服务端代码，WEB端代码请查看：<a href="https://gitee.com/howcode/aq-chat">AQChat</a>，移动端代码请查看<a href="https://gitee.com/ghosthhf/aqchat-mobile">AQChat-Mobile</a>


项目地址：https://aqchat.run 【pc端访问默认访问WEB端地址,移动端访问跳转移动端地址】


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
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/1715433579465.png" width="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/1715433594150.png" width="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/1715433609771.png" width="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/1715433659944.png" width="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/1715433812218.png">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/20240511212736.jpg" height="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/20240511212748.jpg" height="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/20240511212754.jpg" height="500">
<img src="https://aqchat.oss-cn-shenzhen.aliyuncs.com/demo/20240511212801.jpg" height="500">


### 项目部署
1. 克隆项目到本地
```shell
git clone https://gitee.com/howcode/aq-chat-server.git
```
修改配置文件`application.yml`中的数据库、Redis、RocketMQ、阿里云OSS等配置信息


2. 项目运行
启动类位于 `aq-chat-im`模块下 

com.howcode.aqchat.AQChatApplication类

启动成功默认连接路径为 `IP:9090`,可先使用在线websocket测试网页测试连接是否正常

3. 打包项目
进入`aq-chat-im`模块，执行以下命令
>注意:打包需要docker环境，打包完成后会生成对应的jar包以及docker镜像

```shell
mvn clean package -Dmaven.test.skip=true
```
运行后日志位于容器下`/tmp/logs`目录下

## 更新日志

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