package com.howcode.aqchat.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.howcode.aqchat.constant.DarkChatConstant;
import com.howcode.aqchat.utils.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: ZhangWeinan
 * @Description: 指令处理器工厂
 * @date 2024-04-20 16:15
 */
@Component
public class CommandHandlerFactory implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandlerFactory.class);

    private final Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> commandHandlerMap = new HashMap<>();

    private void init() {
        // 获取包名称
        String packageName = CommandHandlerFactory.class.getPackage().getName();
        // 获取所有的 ICmdHandler 子类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(packageName + DarkChatConstant.MessageHandlerConstant.HANDLER_IMPLEMENTATION_PACKAGE_NAME, true, ICmdHandler.class);

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
                if (!currMethod.getName().equals(DarkChatConstant.MessageHandlerConstant.HANDLER_METHOD_NAME)) {
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
                // 创建指令处理器
                ICmdHandler<?> newHandler = (ICmdHandler<?>) cmdHandlerClazz.newInstance();

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
}
