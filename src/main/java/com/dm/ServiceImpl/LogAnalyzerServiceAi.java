package com.dm.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class LogAnalyzerServiceAi {

	private final GeminiApiService geminiApiService;
	@Autowired
	Environment env;

	public LogAnalyzerServiceAi(GeminiApiService geminiApiService) {
		this.geminiApiService = geminiApiService;
	}

	public String analyzeStackTrace(String stackTrace) {
		Map<String, Object> parsedInfo = StackTraceParser.parseStackTrace(stackTrace);

		// Create parts to send to the Gemini API
		List<String> parts = List.of("Analyze and give solution for:", "Exception: " + parsedInfo.get("exceptionType"),
				"Message: " + parsedInfo.get("message"), "Cause: " + parsedInfo.get("causedBy"),
				"Cause Message: " + parsedInfo.get("causeMessage"),
				"Relevant Trace: " + String.join("\n", (List<String>) parsedInfo.get("relevantTrace")));

		return geminiApiService.callGeminiApi(parts);
	}
}