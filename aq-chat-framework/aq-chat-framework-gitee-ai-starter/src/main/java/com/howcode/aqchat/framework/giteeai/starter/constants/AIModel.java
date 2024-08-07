package com.howcode.aqchat.framework.giteeai.starter.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 11:42
 */
public class AIModel {
    public static final String GITEE_AI = "gitee_ai";
    public static final Map<String,String> AI_MODEL_MAP = new HashMap<>();
    public static final String LLAMA3_70B_CHINESE_CHAT = "llama3-70b-chinese-chat";
    public static final String BGE_LARGE_ZH_V1_5 = "bge-large-zh-v1.5";
    //bge-small-zh-v1.5
    public static final String BGE_SMALL_ZH_V1_5 = "bge-small-zh-v1.5";
    //Qwen2-7B-Instruct
    public static final String QWEN2_7B_INSTRUCT = "Qwen2-7B-Instruct";
    public static final String QWEN2_72_B_INSTRUCT = "Qwen2-72B-Instruct";
    //stable-diffusion-xl-base-1.0
    public static final String STABLE_DIFFUSION_XL_BASE_1_0 = "stable-diffusion-xl-base-1.0";
    //stable-diffusion-3-medium
    public static final String STABLE_DIFFUSION_3_MEDIUM = "stable-diffusion-3-medium";
    //ChatTTS
    public static final String CHAT_TTS = "ChatTTS";

    public static final String CHAT_MODEL_CODE = "#{chatModelCode}";
    public static final String TTI_MODEL_CODE = "#{ttiModelCode}";
    public static final String TTV_MODEL_CODE = "#{ttvModelCode}";

    //AI翻译引导词
    public static final String AI_TRANSLATE_GUIDE_WORD = "将以下内容翻译为英语，只需要对应英语即可，不能出现其他任何内容，需要翻译内容为：";

    static {
        AI_MODEL_MAP.put(LLAMA3_70B_CHINESE_CHAT,"https://ai.gitee.com/api/inference/serverless/"+CHAT_MODEL_CODE+"/chat/completions");
        AI_MODEL_MAP.put(BGE_LARGE_ZH_V1_5,"https://ai.gitee.com/api/inference/serverless/"+CHAT_MODEL_CODE+"/embeddings");
        AI_MODEL_MAP.put(BGE_SMALL_ZH_V1_5,"https://ai.gitee.com/api/inference/serverless/"+CHAT_MODEL_CODE+"/embeddings");
        AI_MODEL_MAP.put(QWEN2_7B_INSTRUCT,"https://ai.gitee.com/api/inference/serverless/"+CHAT_MODEL_CODE+"/chat/completions");
        AI_MODEL_MAP.put(QWEN2_72_B_INSTRUCT,"https://ai.gitee.com/api/inference/serverless/"+CHAT_MODEL_CODE+"/chat/completions");
        AI_MODEL_MAP.put(STABLE_DIFFUSION_XL_BASE_1_0,"https://ai.gitee.com/api/inference/serverless/"+TTI_MODEL_CODE+"/text-to-image");
        AI_MODEL_MAP.put(STABLE_DIFFUSION_3_MEDIUM,"https://ai.gitee.com/api/inference/serverless/"+TTI_MODEL_CODE+"/text-to-image");
        AI_MODEL_MAP.put(CHAT_TTS,"https://ai.gitee.com/api/inference/serverless/"+TTV_MODEL_CODE+"/text-to-speech");
    }
}
