package com.dionext.ai.services;

import com.dionext.ai.entity.*;
import com.dionext.ai.repositories.AiModelRepository;
import com.dionext.ai.repositories.AiPromptRepository;
import com.dionext.ai.repositories.AiRequestRepository;
import com.dionext.ai.utils.ModelJsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
public class AIRequestService {
    private static final Logger logger = LoggerFactory.getLogger(AIRequestService.class);
    
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
        if (aiModels.isEmpty()) {
            try {
                // Load models from JSON file using the utility class
                List<AiModel> modelsFromJson = ModelJsonUtils.loadModelsFromJson();
                
                logger.info("Loaded {} AI models from model.json", modelsFromJson.size());
                
                // Save all models to the database
                for (AiModel model : modelsFromJson) {
                    // Convert primitive numeric types to BigDecimal if needed
                    /*
                    if (model.getPricePerRequest() != null && !(model.getPricePerRequest() instanceof BigDecimal)) {
                        model.setPricePerRequest(BigDecimal.valueOf(model.getPricePerRequest().doubleValue()));
                    }
                    if (model.getPricePer1MAll() != null && !(model.getPricePer1MAll() instanceof BigDecimal)) {
                        model.setPricePer1MAll(BigDecimal.valueOf(model.getPricePer1MAll().doubleValue()));
                    }
                    if (model.getPricePer1MInput() != null && !(model.getPricePer1MInput() instanceof BigDecimal)) {
                        model.setPricePer1MInput(BigDecimal.valueOf(model.getPricePer1MInput().doubleValue()));
                    }
                    if (model.getPricePer1MOutput() != null && !(model.getPricePer1MOutput() instanceof BigDecimal)) {
                        model.setPricePer1MOutput(BigDecimal.valueOf(model.getPricePer1MOutput().doubleValue()));
                    }
                     */
                    
                    aiModelRepository.save(model);
                    logger.info("Saved AI model: {} - {}", model.getProvider(), model.getModel());
                }
            } catch (IOException e) {
                logger.error("Failed to load AI models from model.json", e);
                // Fallback to creating a default model if JSON loading fails
            }
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
