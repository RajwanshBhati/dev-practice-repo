package com.example.springbootapp.component;

import org.springframework.stereotype.Component;


@Component
public class LongMessageFormatter implements MessageFormatter {

    @Override
    public String getSupportedType() {
        return "LONG";
    }

    @Override
    public String format(String context) {

        String topic = context;

        // Here I handle null or empty case
        if (topic == null || topic.trim().isEmpty()) {
            topic = "your request";
        }

        String message = "LONG MESSAGE\n";
        message += "Topic   : " + topic + "\n";
        message += "Status  : Success\n";
        message += "Priority: High\n";
        message += "Details : Request processed using long format\n";
        message += "Result  : Completed\n";
        message += "Note    : This is a detailed message format for better readability.";

        return message;
    }
}