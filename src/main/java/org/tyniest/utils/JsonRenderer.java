package org.tyniest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

public class JsonRenderer {
    
    private static ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static String toJSON(Object o) {
        return mapper.writeValueAsString(o);
    }
}
