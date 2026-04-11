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

    public MessageFormatterService(List<MessageFormatter> formatters) {
        this.formatterRegistry = formatters.stream()
                .collect(Collectors.toMap(
                        f -> f.getSupportedType().toUpperCase(),
                        f -> f
                ));
    }

    public String formatMessage(String type, String context) {

        MessageFormatter formatter = formatterRegistry.get(type.toUpperCase());

        if (formatter == null) {
            throw new InvalidMessageTypeException(type);
        }

        return formatter.format(context);
    }
}