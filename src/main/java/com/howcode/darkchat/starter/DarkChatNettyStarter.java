package com.howcode.darkchat.starter;

import com.howcode.darkchat.config.DarkChatConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringBootConfiguration;

import javax.annotation.Resource;

/**
 * @Author: ZhangWeinan
 * @Description: Netty启动类
 * @date 2024-04-19 18:58
 */
@SpringBootConfiguration
public class DarkChatNettyStarter implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NioServerSocketChannel.class);

    @Resource
    private DarkChatConfig darkChatConfig;

    private void startImApplication() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(darkChatConfig.getBossThreadSize());
        NioEventLoopGroup workGroup = new NioEventLoopGroup(darkChatConfig.getWorkThreadSize());
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        ch.pipeline().addLast(new HttpObjectAggregator(65535));
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
                    }
                });
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            LOGGER.info("JVM关闭钩子触发，开始关闭Netty服务...");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            LOGGER.info("Netty服务关闭完成");
        }));

        ChannelFuture channelFuture = bootstrap.bind(darkChatConfig.getWebSocketPort()).sync();
        LOGGER.info("Netty服务启动成功，监听端口：{}", darkChatConfig.getWebSocketPort());
        //阻塞线程，直到channel关闭
        channelFuture.channel().closeFuture();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(() -> {
            try {
                startImApplication();
            } catch (InterruptedException e) {
                LOGGER.error("Netty服务启动失败", e);
            }
        });
        thread.setName("Netty-Server-Thread");
        thread.start();
    }
}
