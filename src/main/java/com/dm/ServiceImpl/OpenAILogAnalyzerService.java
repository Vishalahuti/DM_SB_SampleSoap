package com.dm.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAILogAnalyzerService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";

    @Value("${openai.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    public String analyzeLogWithOpenAI(String logText) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("prompt", "Analyze this log: " + logText);
        body.put("max_tokens", 1000);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            OPENAI_API_URL,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        return response.getBody();
    }
}
