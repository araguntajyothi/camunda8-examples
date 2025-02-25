package com.camunda.wrapper.utils;

import com.camunda.wrapper.exceptions.TechnicalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUtils {

    private static ObjectMapper mapper;

    private JsonUtils() {
    }

    public static <T> T fromJsonFile(Path path, Class<T> type) throws IOException {
        return getObjectMapper().readValue(path.toFile(), type);
    }

    private static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }

    public static String toJson(Object object) throws IOException {
        return getObjectMapper().writeValueAsString(object);
    }

    public static void toJsonFile(Path path, Object object) throws IOException {
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(path.toFile(), object);
    }

    public static JsonNode toJsonNode(InputStream is) throws IOException {
        return getObjectMapper().readTree(is);
    }

    public static JsonNode toJsonNode(String json) throws IOException {
        return getObjectMapper().readTree(json);
    }

    public static <T> T toObject(String json, Class<T> type) {
        try {
            return getObjectMapper().readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new TechnicalException(e);
        }
    }

    public static <T> T toParametrizedObject(String json, TypeReference<T> type) {
        try {
            return getObjectMapper().readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new TechnicalException(e);
        }
    }
}
