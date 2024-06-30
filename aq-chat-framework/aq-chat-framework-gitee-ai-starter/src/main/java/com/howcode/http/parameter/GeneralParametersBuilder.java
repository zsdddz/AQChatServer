package com.howcode.http.parameter;

import com.howcode.session.Message;

import java.util.List;

/**
 * @Description 建造者模式  构建GeneralParameters
 * @Author ZhangWeinan
 * @Date 2024/6/29 22:15
 */
public class GeneralParametersBuilder {
    private final GeneralParameters generalParameters;

    public GeneralParametersBuilder() {
        generalParameters = new GeneralParameters();
    }

    public GeneralParametersBuilder messages(List<Message> message) {
        generalParameters.setMessages(message);
        return this;
    }

    public GeneralParametersBuilder stream(Boolean stream) {
        generalParameters.setStream(stream);
        return this;
    }

    public GeneralParametersBuilder inputs(String inputs) {
        generalParameters.setInputs(inputs);
        return this;
    }

    public GeneralParameters build() {
        return generalParameters;
    }

}
