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

        String topic = (context != null && !context.isBlank())
                ? context
                : "your request";

        return "===== LONG MESSAGE =====\n" +
       "Topic   : " + topic + "\n" +
       "Status  : Success\n" +
       "Priority: High\n" +
       "Details : Request processed using long format\n" +
       "Result  : Completed\n" +
       "========================";
    }
}