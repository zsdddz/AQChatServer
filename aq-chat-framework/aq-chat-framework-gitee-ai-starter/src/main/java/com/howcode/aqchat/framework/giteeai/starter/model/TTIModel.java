package com.howcode.aqchat.framework.giteeai.starter.model;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 11:56
 */
public interface TTIModel {
    //文字转图片
    byte[] textToImage(String message);
}
