package com.bhagya.meeting_notes_summarizer.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bhagya.meeting_notes_summarizer.dto.SummaryResponse;
import com.bhagya.meeting_notes_summarizer.dto.TranscriptRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Service
public class SummarizationService {

    @Value("${groq.api.key}")
    private String apiKey;

    @PostConstruct
    public void checkKey() {
        System.out.println(
        apiKey != null
        ? "API Key Loaded"
        : "API Key Missing"
        );
    }
    
    @Autowired
    private Environment environment;

    @PostConstruct
    public void printProfiles() {
        System.out.println(
        Arrays.toString(environment.getActiveProfiles())
        );
    }
        // API Key Loaded
        // [local]
    private final WebClient webClient;

    public SummarizationService(WebClient.Builder webClientBuilder) {

        this.webClient = webClientBuilder
                .baseUrl("https://api.groq.com/openai/v1")
                .build();
    }

    public SummaryResponse summarize(TranscriptRequest request) {

        String transcript = request.getTranscript();

        String prompt = """
                You are an AI meeting assistant.

                Analyze the meeting transcript and generate:

                1. Short Summary
                2. Key Discussion Points
                3. Action Items

                Return ONLY valid JSON.

                Format:
                {
                "summary": "short summary",
                "actionItems": [
                    "item1",
                    "item2"
                ]
                }

                Keep the response concise, exact format and professional.

                Even if transcript is small, infer reasonable action items.
                
                Transcript:
                %s
                """.formatted(transcript);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-8b-instant",

                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                )
        );

        try {

            String response = webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response);

            String aiResponse = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            JsonNode aiJson = mapper.readTree(aiResponse);

            String summary = aiJson
                    .path("summary")
                    .asText();

            List<String> actionItems = new ArrayList<>();

            for (JsonNode item : aiJson.path("actionItems")) {
                actionItems.add(item.asText());
            }

            return new SummaryResponse(
                    summary,
                    actionItems
            );

        } catch (Exception ex) {

            return new SummaryResponse(
                    "Error generating summary: " + ex.getMessage(),
                    List.of()
            );
        }
    }
}