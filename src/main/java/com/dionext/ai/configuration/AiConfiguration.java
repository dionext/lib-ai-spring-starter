package com.dionext.ai.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.model.tool.LegacyToolCallingManager;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.boot.SpringBootConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

@SpringBootConfiguration
@Slf4j
public class AiConfiguration {

    private static final String OPENAI_BASE_URL = "https://api.openai.com/";
    private static final String DEFAULT_OPENAI_MODEL = "gpt-4o-mini";// "gpt-4o";

    private static final String DEEPSEEK_BASE_URL = "https://api.deepseek.com";
    private static final String DEFAULT_DEEPSEEK_MODEL = "deepseek-chat";

    private static final String PERPLEXITY_BASE_URL = "https://api.perplexity.ai";
    private static final String PERPLEXITY_COMPLETIONS_PATH = "/chat/completions";
    private static final String DEFAULT_PERPLEXITY_MODEL = "llama-3.1-sonar-small-128k-online";

    private static final String MISTRAL_BASE_URL = "https://api.mistral.ai";
    private static final String MISTRAL_DEFAULT_MODEL = "mistral-small-latest";

    private static final String GROQ_BASE_URL = "https://api.groq.com/openai";
    private static final String DEFAULT_GROQ_MODEL = "llama3-70b-8192";

    private static final String NVIDIA_BASE_URL = "https://integrate.api.nvidia.com";
    private static final String DEFAULT_NVIDIA_MODEL = "meta/llama-3.1-70b-instruct";

    @Bean
    public ChatClient chatClientOllama(OllamaChatModel ollamaChatModel) {
        ChatClient.Builder builder = ChatClient.builder(ollamaChatModel);
        return builder
                .build();
    }
    /*
    @Bean
    RestClientCustomizer restClientCustomizer() {
        return restClientBuilder -> {
            restClientBuilder
                    .requestFactory(new BufferingClientHttpRequestFactory(
                            ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                                    .withConnectTimeout(Duration.ofSeconds(60))
                                    .withReadTimeout(Duration.ofSeconds(120))
                            )));
        };
    }
     */

    @Bean
    public OpenAiApi openAiApiOpenAi() {
        //return new OpenAiApi(OPENAI_BASE_URL, System.getenv("OPENAI_API_KEY"));




        reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
                //.responseTimeout(Duration.ofMillis(180000))
                //.doOnConnected(conn -> conn
                //        .addHandler(new ReadTimeoutHandler(180, TimeUnit.SECONDS))
                //       .addHandler(new WriteTimeoutHandler(120)))
                //.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120000);
                .responseTimeout(Duration.ofMillis(6000))
                .doOnConnected(conn -> conn
                        .addHandler(new ReadTimeoutHandler(60, TimeUnit.SECONDS))
                        .addHandler(new WriteTimeoutHandler(60)))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000);

        WebClient.Builder webClientBuilder =  WebClient.builder();
        webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient));
        return OpenAiApi.builder()
                .baseUrl(OPENAI_BASE_URL)
                .webClientBuilder(webClientBuilder)
                .apiKey(new SimpleApiKey(System.getenv("OPENAI_API_KEY")))
                .build();
    }
    @Bean
    public OpenAiChatModel openAiClientOpenAi(OpenAiApi openAiApiOpenAi) {

        RetryTemplate retryTemplate = RetryTemplate.builder()
                //.maxAttempts(10)
                //.exponentialBackoff(Duration.ofMillis(2000), 5, Duration.ofMillis(3 * 60000))
                .maxAttempts(3)
                //.exponentialBackoff(Duration.ofMillis(2000), 5, Duration.ofMillis(3 * 60000))
                .retryOn(TransientAiException.class)
                .retryOn(org.springframework.web.client.ResourceAccessException.class)
                .retryOn(java.io.IOException.class)
                .retryOn(io.netty.handler.timeout.ReadTimeoutException.class)
                .withListener(new RetryListener() {

                    @Override
                    public <T extends Object, E extends Throwable> void onError(RetryContext context,
                                                                                RetryCallback<T, E> callback, Throwable throwable) {
                        log.warn("My Retry error. Retry count:" + context.getRetryCount(), throwable);
                    }
                })
                .build();


        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApiOpenAi)
                .retryTemplate(retryTemplate)
                //.retryTemplate(RetryUtils.DEFAULT_RETRY_TEMPLATE)
                .defaultOptions(OpenAiChatOptions.builder().model(DEFAULT_OPENAI_MODEL).build())
                .build();
        //OpenAiChatModel openAiChatModel = new OpenAiChatModel(openAiApiOpenAi,
         //       OpenAiChatOptions.builder().model(DEFAULT_OPENAI_MODEL).build());
        return openAiChatModel;
    }
    @Bean
    public ChatClient chatClientOpenAi(OpenAiChatModel openAiClientOpenAi) {
        ChatClient.Builder builder = ChatClient.builder(openAiClientOpenAi);
        return builder
                //.defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }



    @Bean
    public OpenAiApi openAiApiDeepseek() {
        //return new OpenAiApi(DEEPSEEK_BASE_URL, System.getenv("DEEPSEEK_API_KEY"));
        return OpenAiApi.builder()
                .baseUrl(DEEPSEEK_BASE_URL)
                .apiKey(new SimpleApiKey(System.getenv("DEEPSEEK_API_KEY")))
                .build();
    }
    @Bean
    public OpenAiChatModel openAiClientDeepseek(OpenAiApi openAiApiDeepseek) {
        return new OpenAiChatModel(openAiApiDeepseek, OpenAiChatOptions.builder().model(DEFAULT_DEEPSEEK_MODEL).build());
    }


    @Bean
    public OpenAiApi openAiApiPerplexity() {
        return new OpenAiApi(PERPLEXITY_BASE_URL, System.getenv("PERPLEXITY_API_KEY"), PERPLEXITY_COMPLETIONS_PATH,
                "/v1/embeddings", RestClient.builder(), WebClient.builder(),
                RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
    }
    @Bean
    public OpenAiChatModel openAiClientPerplexity(OpenAiApi openAiApiPerplexity) {
        return new OpenAiChatModel(openAiApiPerplexity, OpenAiChatOptions.builder().model(DEFAULT_PERPLEXITY_MODEL).build());
    }
    @Bean
    public ChatClient chatClientPerplexity(OpenAiChatModel openAiClientPerplexity) {
        ChatClient.Builder builder = ChatClient.builder(openAiClientPerplexity);
        return builder
                .build();
    }


    @Bean
    public OpenAiApi openAiApiMistral() {
        return OpenAiApi.builder()
                .baseUrl(MISTRAL_BASE_URL)
                .apiKey(new SimpleApiKey(System.getenv("MISTRAL_AI_API_KEY")))
                .build();
    }
    @Bean
    public OpenAiChatModel openAiClientMistral(OpenAiApi openAiApiMistral) {
        return OpenAiChatModel.builder()
                .openAiApi(openAiApiMistral)
                .toolCallingManager(LegacyToolCallingManager.builder().build())
                .defaultOptions(OpenAiChatOptions.builder().model(MISTRAL_DEFAULT_MODEL).build())
                .build();
    }
    @Bean
    public ChatClient chatClientMistral(OpenAiChatModel openAiClientMistral) {
        ChatClient.Builder builder = ChatClient.builder(openAiClientMistral);
        return builder
                .build();
    }



    @Bean
    public OpenAiApi openAiApiGroq() {
        return OpenAiApi.builder()
                .baseUrl(GROQ_BASE_URL)
                .apiKey(new SimpleApiKey(System.getenv("GROQ_API_KEY")))
                .build();
        //return new OpenAiApi(GROQ_BASE_URL, System.getenv("GROQ_API_KEY"));
    }
    @Bean
    public OpenAiChatModel openAiClientGroq(OpenAiApi openAiApiGroq) {
        return new OpenAiChatModel(openAiApiGroq, OpenAiChatOptions.builder().model(DEFAULT_GROQ_MODEL).build());
    }

    @Bean
    public OpenAiApi openAiApiNvidia() {
        return OpenAiApi.builder()
                .baseUrl(NVIDIA_BASE_URL)
                .apiKey(new SimpleApiKey(System.getenv("NVIDIA_API_KEY")))
                .build();
        //return new OpenAiApi(NVIDIA_BASE_URL, System.getenv("NVIDIA_API_KEY"));
    }
    @Bean
    public OpenAiChatModel openAiClientNvidia(OpenAiApi openAiApiNvidia) {
        return new OpenAiChatModel(openAiApiNvidia,
                OpenAiChatOptions.builder().maxTokens(2048).model(DEFAULT_NVIDIA_MODEL).build());
    }


}
