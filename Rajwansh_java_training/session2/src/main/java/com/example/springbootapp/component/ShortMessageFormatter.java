package com.example.springbootapp.component;

import org.springframework.stereotype.Component;

@Component
public class ShortMessageFormatter implements MessageFormatter {
    

    //I am implementing the MessageFormatter interface in the ShortMessageFormatter class. The getSupportedType method returns "SHORT" to indicate that this formatter supports short message formatting. The format method takes a string input (context) and returns a formatted string that includes the context as part of a short message format.
    @Override
    public String getSupportedType() {
        return "SHORT";
    }

    @Override
    public String format(String context) {
        return "Short message: " + context;
    }
}