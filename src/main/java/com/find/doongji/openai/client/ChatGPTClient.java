package com.find.doongji.openai.client;

import com.find.doongji.openai.payload.request.ChatGPTRequest;
import com.find.doongji.openai.payload.response.ChatGPTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class ChatGPTClient implements OpenAIClient {

    @Value("${openai.chatgpt-url}")
    private String chatGPTUrl;

    @Value("${openai.chatgpt-model}")
    private String chatGPTModel;

    @Qualifier("openAIRestTemplate")
    private final RestTemplate openAIRestTemplate;


    @Override
    public String chat(String prompt) {
        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(chatGPTModel, prompt);
        ChatGPTResponse chatGPTResponse = openAIRestTemplate.postForObject(chatGPTUrl, chatGPTRequest, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

}
