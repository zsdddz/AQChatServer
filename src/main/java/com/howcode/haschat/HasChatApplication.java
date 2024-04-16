package com.howcode.haschat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-16 21:17
 */
@SpringBootApplication
public class HasChatApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(HasChatApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
