package com.howcode.aqchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-16 21:17
 */
@SpringBootApplication
public class AQChatApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AQChatApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
        System.out.print("""
                 ________  ________  ________  ___  ___  ________  _________
                |\\   __  \\|\\   __  \\|\\   ____\\|\\  \\|\\  \\|\\   __  \\|\\___   ___\\
                \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\|\\  \\|___ \\  \\_|
                 \\ \\   __  \\ \\  \\\\\\  \\ \\  \\    \\ \\   __  \\ \\   __  \\   \\ \\  \\
                  \\ \\  \\ \\  \\ \\  \\\\\\  \\ \\  \\____\\ \\  \\ \\  \\ \\  \\ \\  \\   \\ \\  \\
                   \\ \\__\\ \\__\\ \\_____  \\ \\_______\\ \\__\\ \\__\\ \\__\\ \\__\\   \\ \\__\\
                    \\|__|\\|__|\\|___| \\__\\|_______|\\|__|\\|__|\\|__|\\|__|    \\|__|
                                    \\|__|
                                                        AQChat v1.0.0 启动成功
                """);
    }
}
