package com.howcode.aqchat.ai.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.howcode.aqchat.ai.AIResult;
import com.howcode.aqchat.ai.IAiService;
import com.howcode.aqchat.ai.config.GenerationParamHolder;
import com.howcode.aqchat.ai.model.QWResult;
import com.howcode.aqchat.common.enums.AIMessageStatusEnum;
import io.reactivex.Flowable;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.function.Consumer;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-06-10 17:24
 */
@Service
@Primary
public class QWAiService implements IAiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QWAiService.class);

    @Resource
    private GenerationParamHolder generationParamHolder;

    @Override
    public String getAnswer(String question) {
        return "";
    }

    @Override
    public void streamCallWithMessage(String userMsg, Consumer<AIResult> consumer) {
        Generation gen = new Generation();
        Message message = Message.builder().role(Role.USER.getValue()).content(userMsg).build();
        try {
            streamCallWithMessage(gen, message, consumer);
        } catch (Exception e) {
            LOGGER.error("Error occurred while calling AI service", e);
            throw new RuntimeException(e);
        }
    }

    private void streamCallWithMessage(Generation gen, Message message, Consumer<AIResult> consumer)
            throws NoApiKeyException, ApiException, InputRequiredException {
        GenerationParam generationParam = generationParamHolder.getGenerationParam();
        if (null == generationParam) {
            QWResult qwResult = new QWResult();
            qwResult.setContent("系统AI未启用，请联系管理员");
            qwResult.setStatus(AIMessageStatusEnum.FAIL.getCode());
            consumer.accept(qwResult);
            return;
        }
        generationParam.setMessages(Collections.singletonList(message));
        Flowable<GenerationResult> result = gen.streamCall(generationParam);
        result.blockingForEach(r -> {
            String content = r.getOutput().getChoices().get(0).getMessage().getContent();
            String finishReason = r.getOutput().getChoices().get(0).getFinishReason();
            QWResult qwResult = new QWResult();
            qwResult.setContent(content);
            qwResult.setStatus("stop".equals(finishReason) ? AIMessageStatusEnum.END.getCode() : AIMessageStatusEnum.WAIT.getCode());
            consumer.accept(qwResult);
        });
        LOGGER.info("Stream call with message completed");
    }

}
