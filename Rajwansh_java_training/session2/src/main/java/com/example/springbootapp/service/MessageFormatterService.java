package com.example.springbootapp.service;

import com.example.springbootapp.component.MessageFormatter;
import com.example.springbootapp.exception.InvalidMessageTypeException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageFormatterService {

    private final Map<String, MessageFormatter> formatterRegistry;


    public List<String> getSupportedTypes() {
    return formatterRegistry.keySet().stream().toList();
}

     // I am using constructor injection to inject a list of MessageFormatter implementations into the MessageFormatterService. The constructor takes a list of MessageFormatter instances and creates a registry (a Map) that maps the supported message type (in uppercase) to the corresponding formatter instance. 
    public MessageFormatterService(List<MessageFormatter> formatters) {
        this.formatterRegistry = formatters.stream()
                .collect(Collectors.toMap(
                        f -> f.getSupportedType().toUpperCase(),
                        f -> f
                ));
    }
    
    // I implemented the formatMessage method to format a message based on the specified type and context. The method looks up the appropriate MessageFormatter from the registry using the provided type (converted to uppercase for case-insensitive matching). If no formatter is found for the given type, it throws an InvalidMessageTypeException. Otherwise, it calls the format method of the found formatter with the provided context and returns the formatted message.
    public String formatMessage(String type, String context) {

        MessageFormatter formatter = formatterRegistry.get(type.toUpperCase());

        if (formatter == null) {
            throw new InvalidMessageTypeException(type);
        }

        return formatter.format(context);
    }
}