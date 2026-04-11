package com.example.springbootapp.component;

public interface MessageFormatter {

   
    String getSupportedType();

    String format(String context);
}
