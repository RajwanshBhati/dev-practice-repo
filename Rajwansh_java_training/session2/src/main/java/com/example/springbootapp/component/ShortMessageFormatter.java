package com.example.springbootapp.component;

import org.springframework.stereotype.Component;

@Component
public class ShortMessageFormatter implements MessageFormatter {

    @Override
    public String getSupportedType() {
        return "SHORT";
    }

    @Override
    public String format(String context) {
        return "Short message: " + context;
    }
}