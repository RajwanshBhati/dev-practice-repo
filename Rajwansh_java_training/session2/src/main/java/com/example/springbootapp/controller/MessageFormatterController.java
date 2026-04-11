package com.example.springbootapp.controller;

import com.example.springbootapp.dto.ApiResponse;
import com.example.springbootapp.service.MessageFormatterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageFormatterController {

    private final MessageFormatterService messageFormatterService;

    public MessageFormatterController(MessageFormatterService messageFormatterService) {
        this.messageFormatterService = messageFormatterService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<String>> getMessage(
            @RequestParam String type,
            @RequestParam(required = false) String context) {

        String formattedMessage =
                messageFormatterService.formatMessage(type, context);

        return ResponseEntity.ok(
                ApiResponse.success("Message formatted successfully", formattedMessage)
        );
    }
}