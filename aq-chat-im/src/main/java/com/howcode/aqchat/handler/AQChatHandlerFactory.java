package com.howcode.aqchat.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.howcode.aqchat.common.constant.AQMessageHandlerConstant;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: ZhangWeinan
 * @Description: 指令处理器工厂
 * @date 2024-04-20 16:15
 */
@Component
public class AQChatHandlerFactory implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AQChatHandlerFactory.class);

    private final Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> commandHandlerMap = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;

    private void init() {
        // 获取包名称
        String packageName = AQChatHandlerFactory.class.getPackage().getName();
        // 获取所有的 ICmdHandler 子类
        Set<Class<?>> clazzSet = listSubClazz(packageName + AQMessageHandlerConstant.HANDLER_IMPLEMENTATION_PACKAGE_NAME);
        for (Class<?> cmdHandlerClazz : clazzSet) {
            if (null == cmdHandlerClazz || 0 != (cmdHandlerClazz.getModifiers() & Modifier.ABSTRACT)) {
                // 如果是抽象类,
                continue;
            }

            // 获取方法数组
            Method[] methodArray = cmdHandlerClazz.getDeclaredMethods();
            // 命令类
            Class<?> cmdClazz = null;

            for (Method currMethod : methodArray) {
                if (!currMethod.getName().equals(AQMessageHandlerConstant.HANDLER_METHOD_NAME)) {
                    // 如果不是 handle 方法,
                    continue;
                }

                // 获取函数参数类型
                Class<?>[] paramTypeArray = currMethod.getParameterTypes();

                if (paramTypeArray.length < 2 || paramTypeArray[1] == GeneratedMessageV3.class || // 这里最好加上这个判断
                        !GeneratedMessageV3.class.isAssignableFrom(paramTypeArray[1])) {
                    continue;
                }

                cmdClazz = paramTypeArray[1];
                break;
            }

            if (null == cmdClazz) {
                continue;
            }

            try {
                // 从spring容器中获取实例
                ICmdHandler<?> newHandler = (ICmdHandler<?>) applicationContext.getBean(cmdHandlerClazz);
                LOGGER.info("factory adds a correspondence::command:{} =>handler:{}", cmdClazz.getName(), cmdHandlerClazz.getName());
                commandHandlerMap.put(cmdClazz, newHandler);
            } catch (Exception ex) {
                // 记录错误日志
                LOGGER.error(ex.getMessage(), ex);
            }
        }

    }

    public ICmdHandler<? extends GeneratedMessageV3> getCommandHandler(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return commandHandlerMap.get(clazz);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    private Set<Class<?>> listSubClazz(String packageName) {
        ApplicationContext context = new AnnotationConfigApplicationContext(packageName);
        Map<String, Object> beans = context.getBeansWithAnnotation(Component.class);
        Set<Class<?>> resultSet = new HashSet<>();
        beans.forEach((beanName, bean) -> {
            Class<?> clazz = bean.getClass();
            if (ICmdHandler.class.isAssignableFrom(clazz)) {
                resultSet.add(clazz);
            }
        });
        return resultSet;
    }
}
