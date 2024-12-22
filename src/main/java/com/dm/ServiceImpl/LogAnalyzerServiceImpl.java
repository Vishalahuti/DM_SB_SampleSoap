package com.dm.ServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogAnalyzerServiceImpl {
	@Autowired
	Environment env;

    public Map<String, String> analyzeLog(String logText) {
        Map<String, String> analysisResults = new HashMap<>();
     
        // Step 1: Remove ANSI escape codes
        String cleanLog = logText.replaceAll("\\u001B\\[[;\\d]*m", "");

        // Step 2: Regex to capture log details
        Pattern logPattern = Pattern.compile(
            "(?<timestamp>\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{2}:\\d{2})\\s+(?<level>[A-Z]+)\\s+(?<thread>\\d+)\\s+---\\s+\\[(?<threadName>[^]]+)]\\s+(?<logger>[^:]+)\\s+:\\s+(?<message>.*)"
        );

        Matcher matcher = logPattern.matcher(cleanLog);
        
        // Use StringBuilder for multi-line log handling (stack traces, etc.)
        StringBuilder messageBuilder = new StringBuilder();
        boolean isMultiLine = false;

        while (matcher.find()) {
            String timestamp = matcher.group("timestamp");
            String level = matcher.group("level");
            String logger = matcher.group("logger");
            String message = matcher.group("message");

            // If a message contains a stack trace, concatenate it.
            if (level.equals("ERROR") || level.equals("WARN")) {
                isMultiLine = true;
                messageBuilder.append(message).append("\n");
            } else if (isMultiLine) {
                messageBuilder.append(message).append("\n");
            }

            analysisResults.put("Timestamp", timestamp);
            analysisResults.put("Level", level);
            analysisResults.put("Logger", logger);
            analysisResults.put("Message", message);

            // Provide troubleshooting tips based on the message content
            String tip = getTroubleshootingTip(message);
            analysisResults.put("Tip", tip);
        }

        // Step 3: Handle cases where stack trace or errors are multi-line
        if (isMultiLine) {
            analysisResults.put("FullMessage", messageBuilder.toString());
        }

        return analysisResults;
    }

    private String getTroubleshootingTip(String message) {
        if (message.contains("NullPointerException")) {
            return "Check for null references in your code.";
        } else if (message.contains("ArrayIndexOutOfBoundsException")) {
            return "Ensure you are not accessing indices outside the array bounds.";
        } else if (message.contains("UT026010")) {
            return "Check your WebSocket buffer pool configuration.";
        } else if (message.contains("Communications link failure")) {
            return "Check your database connectivity and credentials.";
        } else if (message.contains("BeanCreationException")) {
            return "Ensure that the required database driver dependency is included in your project.";
        } else if (message.contains("Failed to determine DatabaseDriver")) {
            return "Check your database configuration in application.properties or application.yml and ensure that the correct driver dependency is included.";
        } else {
            return "Consult the documentation for more information.";
        }
    }
}
