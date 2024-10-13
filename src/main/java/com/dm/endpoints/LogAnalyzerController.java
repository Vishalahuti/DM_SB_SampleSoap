package com.dm.endpoints;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dm.ServiceImpl.LogAnalyzerServiceAi;
import com.dm.ServiceImpl.LogAnalyzerServiceImpl;
import com.dm.ServiceImpl.OpenAILogAnalyzerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LogAnalyzerController {

    @Autowired
    private LogAnalyzerServiceImpl logAnalyzerService;
    
    @Autowired
    private LogAnalyzerServiceAi LogAnalyzerServiceAi;
    @Autowired
    OpenAILogAnalyzerService openAILogAnalyzerService;
    
    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeLog(@RequestBody String logText) throws Exception {
//        Map<String, String> analysisResults = logAnalyzerService.analyzeLog(logText);
//        String res = openAILogAnalyzerService.analyzeLogWithOpenAI(logText);
    	String res = LogAnalyzerServiceAi.analyzeStackTrace(logText);
    	String response = formatResponseAsHtml(res);
        return ResponseEntity.ok(response);
    }
    
    public String formatResponseAsHtml(String jsonResponse) throws Exception {
        // Parse the JSON response (assuming it comes as a String)
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Extract the 'text' part
        String text = rootNode
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        // Convert markdown-like structure to HTML
        String htmlContent = convertMarkdownToHtml(text);

        // Return HTML content wrapped in a basic HTML structure
        return "<html><body>" + htmlContent + "</body></html>";
    }

    private String convertMarkdownToHtml(String text) {
        // Replace markdown-like headings
        text = text.replaceAll("## (.*)\n", "<h2>$1</h2>\n");
        text = text.replaceAll("\\*\\*(.*)\\*\\*", "<b>$1</b>");
        text = text.replaceAll("`(.*?)`", "<code>$1</code>");
        
        // Add line breaks for new lines
        text = text.replaceAll("\n", "<br>");

        // You can also add more markdown parsing rules here
        return text;
    }

}

