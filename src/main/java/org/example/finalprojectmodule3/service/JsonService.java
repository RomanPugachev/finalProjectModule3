package org.example.finalprojectmodule3.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.SneakyThrows;
import org.example.finalprojectmodule3.service.deserialization.SubjectDeserializerModule;




public class JsonService {
    @Getter
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new SubjectDeserializerModule())
            .registerModule(new JavaTimeModule());
    @SneakyThrows
    public String toJson(Object object){
        return objectMapper.writeValueAsString(object);
    }
    @SneakyThrows
    public <T> T fromJson(String json, Class<T> clazz){
        return objectMapper.readValue(json, clazz);
    }
}
