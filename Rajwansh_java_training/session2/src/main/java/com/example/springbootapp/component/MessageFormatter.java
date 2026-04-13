package com.example.springbootapp.component;


//Here I am creating an interface called MessageFormatter which will be implemented by different classes to format messages in different ways. The getSupportedType method will return the type of formatting supported by the implementation, and the format method will take a string input and return the formatted string based on the implementation.
public interface MessageFormatter {

   
    String getSupportedType();

    String format(String context);
}
