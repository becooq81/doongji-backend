package com.find.doongji.openai.controller;

import com.find.doongji.openai.client.ChatGPTClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/openai")
@RequiredArgsConstructor
public class OpenAIController {

    private final ChatGPTClient chatGPTClient;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String prompt) {
        return ResponseEntity.ok(chatGPTClient.chat(prompt));
    }
}
