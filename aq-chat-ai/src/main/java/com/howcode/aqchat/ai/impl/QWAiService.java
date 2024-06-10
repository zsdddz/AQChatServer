package com.howcode.aqchat.ai.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.howcode.aqchat.ai.AIResult;
import com.howcode.aqchat.ai.IAiService;
import com.howcode.aqchat.ai.model.QWResult;
import com.howcode.aqchat.common.config.AQChatConfig;
import io.reactivex.Flowable;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-06-10 17:24
 */
@Service
public class QWAiService implements IAiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QWAiService.class);

    @Resource
    private AQChatConfig aqChatConfig;

    @Override
    public String getAnswer(String question) {
        return "";
    }

    @Override
    public void streamCallWithMessage(String userMsg, Consumer<AIResult> consumer) {
        Generation gen = new Generation();
        Message message = Message.builder().role(Role.USER.getValue()).content(userMsg).build();
        try{
            streamCallWithMessage(gen, message, consumer);
        }catch (Exception e){
            LOGGER.error("Error occurred while calling AI service", e);
            throw new RuntimeException(e);
        }
    }

    private void streamCallWithMessage(Generation gen, Message message, Consumer<AIResult> consumer)
            throws NoApiKeyException, ApiException, InputRequiredException {
        GenerationParam param = buildGenerationParam(message);
        Flowable<GenerationResult> result = gen.streamCall(param);
        result.blockingForEach(r -> {
            String content = r.getOutput().getChoices().get(0).getMessage().getContent();
            String finishReason = r.getOutput().getChoices().get(0).getFinishReason();
            QWResult qwResult = new QWResult();
            qwResult.setContent(content);
            qwResult.setStatus("stop".equals(finishReason) ? 1 : 0);
            consumer.accept(qwResult);
        });
        LOGGER.info("Stream call with message completed");
    }


    private GenerationParam buildGenerationParam(Message userMsg) {
        return GenerationParam.builder()
                .apiKey(aqChatConfig.getAiConfig().getApiKey())
                .model(aqChatConfig.getAiConfig().getModel())
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8)
                .incrementalOutput(true)
                .build();
    }

}
