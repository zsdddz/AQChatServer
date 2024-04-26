package com.howcode.aqchat.starter;

import com.howcode.aqchat.codec.MessageDecoder;
import com.howcode.aqchat.codec.MessageEncoder;
import com.howcode.aqchat.common.config.AQChatConfig;
import com.howcode.aqchat.handler.AQChatCommandHandler;
import com.howcode.aqchat.handler.HearBeatHandler;
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
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringBootConfiguration;


/**
 * @Author: ZhangWeinan
 * @Description: Netty启动类
 * @date 2024-04-19 18:58
 */
@SpringBootConfiguration
public class AQChatNettyStarter implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NioServerSocketChannel.class);

    @Resource
    private AQChatConfig aqChatConfig;
    @Resource
    private MessageDecoder messageDecoder;
    @Resource
    private MessageEncoder messageEncoder;
    @Resource
    private AQChatCommandHandler aqChatCommandHandler;
    @Resource
    private HearBeatHandler hearBeatHandler;

    private void startImApplication() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(aqChatConfig.getBossThreadSize());
        NioEventLoopGroup workGroup = new NioEventLoopGroup(aqChatConfig.getWorkThreadSize());
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
                        ch.pipeline().addLast(new IdleStateHandler(0,0,10));
                        ch.pipeline().addLast(hearBeatHandler);
                        ch.pipeline().addLast(messageDecoder);
                        ch.pipeline().addLast(messageEncoder);
                        ch.pipeline().addLast(aqChatCommandHandler);
                    }
                });
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            LOGGER.info("JVM关闭钩子触发，开始关闭Netty服务...");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            LOGGER.info("Netty服务关闭完成");
        }));

        ChannelFuture channelFuture = bootstrap.bind(aqChatConfig.getWebSocketPort()).sync();
        LOGGER.info("Netty服务启动成功，监听端口：{}", aqChatConfig.getWebSocketPort());
        //阻塞线程，直到channel关闭
        channelFuture.channel().closeFuture();
    }

    @Override
    public void afterPropertiesSet() {
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
