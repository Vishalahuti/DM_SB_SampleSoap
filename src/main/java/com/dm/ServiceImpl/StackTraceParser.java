package com.dm.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackTraceParser {

    public static Map<String, Object> parseStackTrace(String stackTrace) {
        Map<String, Object> parsedInfo = new HashMap<>();
        
        // Pattern to match exception type and message
        Pattern exceptionPattern = Pattern.compile("^(\\S+Exception):\\s(.+)$");
        Pattern causePattern = Pattern.compile("^Caused by: (\\S+):\\s(.+)$");

        List<String> stackLines = Arrays.asList(stackTrace.split("\\r?\\n"));

        for (String line : stackLines) {
            // Match exception type and message
            Matcher exceptionMatcher = exceptionPattern.matcher(line);
            if (exceptionMatcher.find()) {
                parsedInfo.put("exceptionType", exceptionMatcher.group(1));
                parsedInfo.put("message", exceptionMatcher.group(2));
            }

            // Match caused by line
            Matcher causeMatcher = causePattern.matcher(line);
            if (causeMatcher.find()) {
                parsedInfo.put("causedBy", causeMatcher.group(1));
                parsedInfo.put("causeMessage", causeMatcher.group(2));
            }
        }

        // Extract the first 5 stack trace elements (or customize based on need)
        List<String> relevantTrace = new ArrayList<>();
        for (String line : stackLines) {
            if (line.startsWith("\tat")) {
                relevantTrace.add(line.trim());
                if (relevantTrace.size() >= 5) {
                    break;
                }
            }
        }
        parsedInfo.put("relevantTrace", relevantTrace);

        return parsedInfo;
    }
}