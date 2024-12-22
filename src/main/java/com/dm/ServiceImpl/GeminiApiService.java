package com.dm.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeminiApiService {
	@Autowired 
	Environment env;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Constructor
    public GeminiApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    // Method to call Gemini API
    public String callGeminiApi(List<String> parts) {
        String url = env.getProperty("golu.Url") + "?" + "key=" + env.getProperty("golu.sec");

        try {
            // Create the payload as a JSON structure
            String payload = createPayload(parts);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);

            // Send the request and return the response body
            return restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();

        } catch (Exception ex) {
            // Handle any exception here
            ex.printStackTrace();
            return null;
        }
    }

    // Method to create JSON payload using ObjectMapper
    private String createPayload(List<String> parts) throws Exception {
        // Map structure to represent the payload
        Map<String, Object> payloadMap = new HashMap<>();
        Map<String, Object> contentMap = new HashMap<>();
        List<Map<String, String>> partList = new ArrayList<>();

        // Add each part to the list
        for (String part : parts) {
            Map<String, String> partMap = new HashMap<>();
            partMap.put("text", part);
            partList.add(partMap);
        }

        // Structure it under "contents" and "parts"
        contentMap.put("parts", partList);
        List<Map<String, Object>> contentsList = new ArrayList<>();
        contentsList.add(contentMap);
        payloadMap.put("contents", contentsList);

        // Convert the payload map to a JSON string
        return objectMapper.writeValueAsString(payloadMap);
    }
}