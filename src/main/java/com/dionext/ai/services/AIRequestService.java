package com.dionext.ai.services;

import com.dionext.ai.entity.*;
import com.dionext.ai.repositories.AiModelRepository;
import com.dionext.ai.repositories.AiPromptRepository;
import com.dionext.ai.repositories.AiRequestRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
public class AIRequestService {
    @Autowired
    private AiModelRepository aiModelRepository;
    @Autowired
    private AiPromptRepository aiPromptRepository;
    @Autowired
    private AiRequestRepository aiRequestRepository;

    //private OpenAiChatModel openAiClientOpenAi;
    //private OllamaChatModel chatModel;
    /*
    @Autowired
    private ChatClient chatClientOpenAi;
    @Autowired
    private ChatClient chatClientPerplexity;
    @Autowired
    private ChatClient chatClientMistral;
     */
    @Autowired
    private ApplicationContext applicationContext;

    public AIRequestService() {
    }

    public ChatClient getChatClient(AiModel aiModel){
        if (aiModel.getChatClientBean() == null) throw new RuntimeException("ChatClientBean must be set for aiModel with id = " + aiModel.getId());
        ChatClient chatClient = applicationContext.getBean(aiModel.getChatClientBean(), ChatClient.class);
        if (chatClient == null) throw new RuntimeException("ChatClientBean not found set for aiModel with id = " + aiModel.getId());
        return chatClient;
    }

    @PostConstruct
    void postConstruct() {
        List<AiModel> aiModels = aiModelRepository.findAll();
        if (aiModels.size() == 0) {
            //1
            AiModel aiModel = new AiModel();
            aiModel.setId(1L);
            aiModel.setChatClientBean("chatClientPerplexity");
            aiModel.setLaunchType(AiLaunchType.BROKER);
            aiModel.setProvider("LLaMA");//LLaMA (Large Language Model Meta AI)
            aiModel.setProviderUrl("https://www.llama.com/");
            aiModel.setBroker("Perplexity");
            aiModel.setBrokerUrl("https://www.perplexity.ai");
            aiModel.setModel("llama-3.1-sonar-small-128k-online");//These models will be deprecated and will no longer be available to use after 2/22/2025
            aiModel.setModelUrl("https://www.llama.com/docs/model-cards-and-prompt-formats/llama3_1/");
            aiModel.setPricePerRequest(BigDecimal.valueOf(0.005));
            aiModel.setPricePer1MAll(BigDecimal.valueOf(0.2));
            aiModelRepository.save(aiModel);

            //2
            aiModel = new AiModel();
            aiModel.setId(2L);
            aiModel.setChatClientBean("chatClientOllama");
            aiModel.setProvider("Mistral");
            aiModel.setProviderUrl("https://mistral.ai");
            aiModel.setModel("mistral");
            aiModel.setModelUrl("https://mistral.ai/en/models");
            aiModel.setBroker("Ollama");
            aiModel.setBrokerUrl("https://ollama.com");
            aiModel.setLaunchType(AiLaunchType.LOCAL);
            aiModel.setPricePerRequest(BigDecimal.valueOf(0));
            aiModel.setPricePer1MAll(BigDecimal.valueOf(0));
            aiModelRepository.save(aiModel);

            //OpenAI  You can select between models such as: gpt-4o, gpt-4o-mini, gpt-4-turbo, gpt-3.5-turbo, and more.
            //3
            aiModel = new AiModel();
            aiModel.setId(3L);
            aiModel.setChatClientBean("chatClientOpenAi");
            aiModel.setProvider("OpenAi");
            aiModel.setProviderUrl("https://openai.com/");
            aiModel.setModel("gpt-4o");
            aiModel.setModelUrl("https://openai.com/index/gpt-4o-system-card/");
            aiModel.setLaunchType(AiLaunchType.PROVIDER);
            aiModel.setPricePer1MInput(BigDecimal.valueOf(2.5));//Cached input: $1.25 / 1M tokens
            aiModel.setPricePer1MOutput(BigDecimal.valueOf(10.0));
            aiModelRepository.save(aiModel);

            //4
            aiModel = new AiModel();
            aiModel.setId(4L);
            aiModel.setChatClientBean("chatClientOpenAi");
            aiModel.setProvider("OpenAi");
            aiModel.setProviderUrl("https://openai.com/");
            aiModel.setModel("gpt-4o-mini");
            aiModel.setModelUrl("https://openai.com/index/gpt-4o-mini-advancing-cost-efficient-intelligence/");
            aiModel.setLaunchType(AiLaunchType.PROVIDER);
            aiModel.setPricePer1MInput(BigDecimal.valueOf(0.150));//Cached input: $0.075 / 1M tokens
            aiModel.setPricePer1MOutput(BigDecimal.valueOf(0.600));
            aiModelRepository.save(aiModel);

            //5
            aiModel = new AiModel();
            aiModel.setId(5L);
            aiModel.setChatClientBean("chatClientPerplexity");
            aiModel.setLaunchType(AiLaunchType.BROKER);
            aiModel.setProvider("LLaMA");//LLaMA (Large Language Model Meta AI)
            aiModel.setProviderUrl("https://www.llama.com/");
            aiModel.setBroker("Perplexity");
            aiModel.setBrokerUrl("https://www.perplexity.ai");
            aiModel.setModel("sonar");//Команда создала Sonar на основе модели Meta Llama 3.3 70B, доработав её с помощью дополнительного обучения для улучшения поисковых возможностей. Э
            aiModel.setModelUrl("https://www.llama.com/docs/model-cards-and-prompt-formats/llama3_3/");
            aiModel.setPricePerRequest(BigDecimal.valueOf(0.005));
            aiModel.setPricePer1MInput(BigDecimal.valueOf(1.0));
            aiModel.setPricePer1MOutput(BigDecimal.valueOf(1.0));
            aiModelRepository.save(aiModel);

            //6
            aiModel = new AiModel();
            aiModel.setId(6L);
            aiModel.setChatClientBean("chatClientPerplexity");
            aiModel.setLaunchType(AiLaunchType.BROKER);
            aiModel.setProvider("LLaMA");//LLaMA (Large Language Model Meta AI)
            aiModel.setProviderUrl("https://www.llama.com/");
            aiModel.setBroker("Perplexity");
            aiModel.setBrokerUrl("https://www.perplexity.ai");
            aiModel.setModel("sonar-pro");//Команда создала Sonar на основе модели Meta Llama 3.3 70B, доработав её с помощью дополнительного обучения для улучшения поисковых возможностей. Э
            aiModel.setModelUrl("https://www.llama.com/docs/model-cards-and-prompt-formats/llama3_3/");
            aiModel.setPricePerRequest(BigDecimal.valueOf(0.005));
            aiModel.setPricePer1MInput(BigDecimal.valueOf(3.0));
            aiModel.setPricePer1MOutput(BigDecimal.valueOf(15.0));
            aiModelRepository.save(aiModel);

            //7
            aiModel = new AiModel();
            aiModel.setId(7L);
            aiModel.setChatClientBean("chatClientPerplexity");
            aiModel.setLaunchType(AiLaunchType.BROKER);
            aiModel.setProvider("LLaMA");//LLaMA (Large Language Model Meta AI)
            aiModel.setProviderUrl("https://www.llama.com/");
            aiModel.setBroker("Perplexity");
            aiModel.setBrokerUrl("https://www.perplexity.ai");
            aiModel.setModel("sonar-reasoning");//Команда создала Sonar на основе модели Meta Llama 3.3 70B, доработав её с помощью дополнительного обучения для улучшения поисковых возможностей. Э
            aiModel.setModelUrl("https://www.llama.com/docs/model-cards-and-prompt-formats/llama3_3/");
            aiModel.setPricePerRequest(BigDecimal.valueOf(0.005));
            aiModel.setPricePer1MInput(BigDecimal.valueOf(1.0));
            aiModel.setPricePer1MOutput(BigDecimal.valueOf(5.0));
            aiModelRepository.save(aiModel);

            //8
            aiModel = new AiModel();
            aiModel.setId(8L);
            aiModel.setChatClientBean("chatClientPerplexity");
            aiModel.setLaunchType(AiLaunchType.BROKER);
            aiModel.setProvider("LLaMA");//LLaMA (Large Language Model Meta AI)
            aiModel.setProviderUrl("https://www.llama.com/");
            aiModel.setBroker("Perplexity");
            aiModel.setBrokerUrl("https://www.perplexity.ai");
            aiModel.setModel("sonar-reasoning-pro");//Команда создала Sonar на основе модели Meta Llama 3.3 70B, доработав её с помощью дополнительного обучения для улучшения поисковых возможностей. Э
            aiModel.setModelUrl("https://www.llama.com/docs/model-cards-and-prompt-formats/llama3_3/");
            aiModel.setPricePerRequest(BigDecimal.valueOf(0.005));
            aiModel.setPricePer1MInput(BigDecimal.valueOf(2.0));
            aiModel.setPricePer1MOutput(BigDecimal.valueOf(8.0));
            aiModelRepository.save(aiModel);

            //9
            //"The model `o3-mini` does not exist or you do not have access to it.",
            //Update: You now have access to OpenAI o1 and OpenAI o3-mini in the API.
            //https://platform.openai.com/docs/guides/rate-limits
            aiModel = new AiModel();
            aiModel.setId(9L);
            aiModel.setChatClientBean("chatClientOpenAi");
            aiModel.setProvider("OpenAi");
            aiModel.setProviderUrl("https://openai.com/");
            aiModel.setModel("o3-mini");
            aiModel.setModelUrl("https://openai.com/index/openai-o3-mini/");
            aiModel.setLaunchType(AiLaunchType.PROVIDER);
            aiModel.setPricePer1MInput(BigDecimal.valueOf(1.1));//Cached input: $0.075 / 1M tokens
            aiModel.setPricePer1MOutput(BigDecimal.valueOf(4.4));
            aiModelRepository.save(aiModel);

            //10
            //TODO "Unsupported value: 'messages[0].role' does not support 'system' with this model.",
            //после удаления system message
            //I/O error on POST request for "https://api.openai.com/v1/chat/completions": null
            aiModel = new AiModel();
            aiModel.setId(10L);
            aiModel.setChatClientBean("chatClientOpenAi");
            aiModel.setProvider("OpenAi");
            aiModel.setProviderUrl("https://openai.com/");
            aiModel.setModel("o1-mini");
            aiModel.setModelUrl("https://openai.com/index/openai-o1-mini-advancing-cost-efficient-reasoning/");
            aiModel.setLaunchType(AiLaunchType.PROVIDER);
            aiModel.setPricePer1MInput(BigDecimal.valueOf(1.1));//Cached input: $0.075 / 1M tokens
            aiModel.setPricePer1MOutput(BigDecimal.valueOf(4.4));
            aiModelRepository.save(aiModel);

        }

    }
    public Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariant(String externalDomain, String externalEntity, String externalVariant) {
        return aiRequestRepository.findByExternalDomainAndExternalEntityAndExternalVariant(
                externalDomain, externalEntity, externalVariant);
    }
    public Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariantAndExternalId(String externalDomain, String externalEntity, String externalVariant, String externalId) {
        return aiRequestRepository.findByExternalDomainAndExternalEntityAndExternalVariantAndExternalId(
                externalDomain, externalEntity, externalVariant, externalId);
    }
    public Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariantAndExternalIdAndAiModelIdAndAiPromptId(String externalDomain, String externalEntity, String externalVariant, String externalId, Long aiModelId, Long aiPromptId) {
        return aiRequestRepository.findByExternalDomainAndExternalEntityAndExternalVariantAndExternalIdAndAiModelIdAndAiPromptId(
                externalDomain, externalEntity, externalVariant, externalId, aiModelId, aiPromptId);
    }

    public AiLogInfo createAiLogInfo(Long aiModelId,
                                     Long aiPromptId,
                                     AiRequest aiRequest){
        if (aiModelId == null) throw new RuntimeException("aiModelId can not be null");
        if (aiPromptId == null) throw new RuntimeException("aiPromptId can not be null");
        AiModel aiModel = aiModelRepository.findById(aiModelId).orElse(null);
        if (aiModel == null) throw new RuntimeException("AiModel not found by id = " + aiModelId);
        AiPrompt aiPrompt =  aiPromptRepository.findById(aiPromptId).orElse(null);
        if (aiPrompt == null) throw new RuntimeException("aiPrompt not found by id = " + aiPromptId);


        AiLogInfo aiLogInfo = new AiLogInfo(aiModel, aiPrompt, aiRequest);
        aiLogInfo.aiRequest().setAiPromptId(aiLogInfo.aiPrompt().getId());
        aiLogInfo.aiRequest().setAiModelId(aiLogInfo.aiModel().getId());
        return aiLogInfo;
    }

    public void prepareAiLogInfo(AiLogInfo aiLogInfo,
                                      String externalDomain,
                                      String externalEntity,
                                      String externalVariant,
                                      String externalId) {
        /*
        if ((aiPrompt.getSystemPromptTempl() != null && !aiPrompt.getSystemPromptTempl().equals(systemPromptTemplate))
                || (aiPrompt.getSystemPromptTempl() == null && systemPromptTemplate != null)){
            aiPrompt.setSystemPromptTempl(systemPromptTemplate);
            aiPromptRepository.save(aiPrompt);
        }
        if ((aiPrompt.getUserPromptTempl() != null && !aiPrompt.getUserPromptTempl().equals(userPromptTemplate))
                || (aiPrompt.getUserPromptTempl() == null && userPromptTemplate != null)){
            aiPrompt.setUserPromptTempl(userPromptTemplate);
            aiPromptRepository.save(aiPrompt);
        }
         */


        aiLogInfo.aiRequest().setExternalDomain(externalDomain);
        aiLogInfo.aiRequest().setExternalEntity(externalEntity);
        aiLogInfo.aiRequest().setExternalVariant(externalVariant);
        aiLogInfo.aiRequest().setExternalId(externalId);
    }

    public void postProccessAiLogInfo(AiLogInfo aiLogInfo) {
        AiModel aiModel = aiLogInfo.aiModel();
        AiRequest aiRequest = aiLogInfo.aiRequest();
        //AiPrompt aiPrompt = aiLogInfo.aiPrompt();

        double cost = calculateCost(aiModel, aiRequest);
        aiRequest.setCost(BigDecimal.valueOf(cost));
        aiRequestRepository.save(aiRequest);
    }

    static public double calculateCost(AiModel aiModel, AiRequest aiRequest) {
        double cost = 0.0;
        if (nvl(aiModel.getPricePer1MAll()) != 0.0) {
            cost += (nvl(aiRequest.getPromptTokens()) + nvl(aiRequest.getCompletionTokens()))
                    * nvl(aiModel.getPricePer1MAll()) / 1000000.0;
        } else {
            cost += nvl(aiRequest.getPromptTokens()) * nvl(aiModel.getPricePer1MInput()) / 1000000.0;
            cost += nvl(aiRequest.getCompletionTokens()) * nvl(aiModel.getPricePer1MOutput()) / 1000000.0;
        }
        cost += (nvl(aiModel.getPricePerRequest()));
        return cost;
    }

    static private double nvl(BigDecimal value){
        return value != null? value.doubleValue() : 0.0;
    }
    static private double nvl(Long value){
        return value != null? value.doubleValue() : 0.0;
    }

}
