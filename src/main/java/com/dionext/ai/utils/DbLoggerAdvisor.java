package com.dionext.ai.utils;

import com.dionext.ai.entity.AiLogInfo;
import com.dionext.ai.entity.AiPrompt;
import com.dionext.ai.entity.AiRequest;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DbLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private AiPrompt aiPrompt = null;
    private AiRequest aiRequest = null;

    private long startTime = 0;
    public DbLoggerAdvisor(AiLogInfo aiLogInfo){
        this.aiPrompt = aiLogInfo.aiPrompt();
        this.aiRequest = aiLogInfo.aiRequest();
    }

    private int order;

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public int getOrder() {
        return this.order;
    }


    private AdvisedRequest before(AdvisedRequest request) {
        aiRequest.setUserPrompt(request.userText());

        //system
        StringBuilder systemPrompt = new StringBuilder();
        if (request.systemText() != null)
            systemPrompt.append(request.systemText());
        for (var e : request.adviseContext().entrySet()){
            systemPrompt.append(e.getValue());
        }
        List<Message> messages =  request.messages();
        if (messages != null){
            for(Message message : messages){
                if (message.getMessageType() == MessageType.SYSTEM && message.getText() != null){
                    systemPrompt.append(message.getText());
                }
            }
        }
        if (systemPrompt.length() > 0)
            aiRequest.setSystemPrompt(systemPrompt.toString());

        Double temperature = request.chatOptions().getTemperature();
        if (temperature != null) {
            aiRequest.setTemperature(BigDecimal.valueOf(temperature));
        }
        startTime = System.currentTimeMillis();
        return request;
    }


    private void observeAfter(AdvisedResponse advisedResponse) {
        ChatResponse chatResponse = advisedResponse.response();
        aiRequest.setResult(chatResponse.getResult().getOutput().getContent());
        aiRequest.setDateTime(LocalDateTime.now());
        aiRequest.setDuration(System.currentTimeMillis() - startTime);
        aiRequest.setPromptTokens(chatResponse.getMetadata().getUsage().getPromptTokens());
        aiRequest.setGenerationTokens(chatResponse.getMetadata().getUsage().getGenerationTokens());
    }

    public String toString() {
        return SimpleLoggerAdvisor.class.getSimpleName();
    }

    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        this.observeAfter(advisedResponse);
        return advisedResponse;
    }

    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }
}
