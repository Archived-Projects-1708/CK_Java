package com.example.exambank.service;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

public class AIService {
    private final OpenAiService openAiService;

    public AIService(String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
    }

    public String generateAnswerHint(String questionText) {
        CompletionRequest request = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("Gợi ý đáp án cho câu hỏi Tiếng Nhật: " + questionText)
                .maxTokens(200)
                .build();

        return openAiService.createCompletion(request).getChoices().get(0).getText();
    }
}