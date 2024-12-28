package com.example.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    public static String writeValueAsString(String value) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(value);
    }
}
