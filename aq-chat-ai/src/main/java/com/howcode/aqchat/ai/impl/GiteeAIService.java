package com.howcode.aqchat.ai.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.howcode.aqchat.ai.AIResult;
import com.howcode.aqchat.ai.IAiService;
import com.howcode.aqchat.ai.model.GiteeResult;
import com.howcode.aqchat.ai.parameter.MessageRecord;
import com.howcode.aqchat.common.enums.AIMessageStatusEnum;
import com.howcode.aqchat.common.enums.MsgTypeEnum;
import com.howcode.aqchat.common.utils.ALIOSSHelper;
import com.howcode.aqchat.framework.giteeai.starter.GiteeAIClient;
import com.howcode.aqchat.framework.giteeai.starter.session.Message;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 14:04
 */
@Service
public class GiteeAIService implements IAiService {

    @Resource
    private GiteeAIClient giteeAIClient;
    @Resource
    private ALIOSSHelper aliossHelper;

    @Override
    public String getAnswer(String question) {
        return giteeAIClient.chat(question);
    }

    @Override
    public void streamCallWithMessage(String userMsg, Consumer<AIResult> consumer) {
        chat(userMsg, null, consumer);
    }

    @Override
    public void chat(String message, List<MessageRecord> messages, Consumer<AIResult> consumer) {
        List<Message> messageList = convertMessageRecordToMessage(messages);
        giteeAIClient.streamChat(message, messageList, data -> {
            JSONObject parse = JSONObject.parseObject(data);
            JSONArray choices = parse.getJSONArray("choices");
            JSONObject choicesIn = choices.getJSONObject(0);
            String finishReason = choicesIn.getString("finish_reason");
            if (finishReason != null && finishReason.equals("stop")) {
                GiteeResult giteeResult = new GiteeResult();
                giteeResult.setStatus(AIMessageStatusEnum.END.getCode());
                consumer.accept(giteeResult);
                return;
            }
            JSONObject delta = choicesIn.getJSONObject("delta");
            String content = delta.getString("content");
            if (content != null && !content.isEmpty()) {
                GiteeResult giteeResult = new GiteeResult();
                giteeResult.setContent(content);
                giteeResult.setStatus(AIMessageStatusEnum.WAIT.getCode());
                consumer.accept(giteeResult);
            }
        });
    }

    //将List<MessageRecord> 转换为List<Message>
    private List<Message> convertMessageRecordToMessage(List<MessageRecord> messages) {
        if (messages == null || messages.isEmpty()) {
            return new ArrayList<>();
        }
        List<Message> messageList = new ArrayList<>();
        for (MessageRecord messageRecord : messages) {
            Message message = new Message();
            message.setContent(messageRecord.getContent());
            message.setRole(messageRecord.getRole());
            messageList.add(message);
        }
        return messageList;
    }

    //生成当前时间 年份-月份-日期
    private String generateCurrentTime() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }


    @Override
    public AIResult textToImage(String text) {
        byte[] bytes = giteeAIClient.textToImage(text);
        String fileName = "image/" + generateCurrentTime() + "/" + UUID.randomUUID() + ".png";
        String upload = aliossHelper.upload(fileName, bytes);
        GiteeResult giteeResult = new GiteeResult();
        giteeResult.setContent(upload);
        giteeResult.setType(MsgTypeEnum.TEXT.getCode());
        giteeResult.setStatus(AIMessageStatusEnum.END.getCode());
        return giteeResult;
    }

    @Override
    public AIResult textToVoice(String text) {
        byte[] bytes = giteeAIClient.textToVoice(text);
        String fileName = "voice/" + generateCurrentTime() + "/" + UUID.randomUUID() + ".mp3";
        String upload = aliossHelper.upload(fileName, bytes);
        GiteeResult giteeResult = new GiteeResult();
        giteeResult.setContent(upload);
        giteeResult.setType(MsgTypeEnum.VOICE.getCode());
        giteeResult.setStatus(AIMessageStatusEnum.END.getCode());
        return giteeResult;
    }
}
