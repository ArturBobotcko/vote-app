package org.somecompany.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.somecompany.models.Model;
import org.somecompany.models.Topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Json {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToJsonString(Model model) {
        try {
            return objectMapper.writeValueAsString(model);  
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Model> T convertJsonToModel(String json, Class<T> modelClass) {
        try {
            return objectMapper.readValue(json, modelClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Model> T convertJsonToModel(File file, Class<T> modelClass) {
        try {
            return objectMapper.readValue(file, modelClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Convert a list of topics to JSON string
     */
    public static String convertTopicListToJson(List<Topic> topics) {
        try {
            return objectMapper.writeValueAsString(topics);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }
    
    /**
     * Convert JSON string to a list of topics
     */
    public static List<Topic> convertJsonToTopicList(String json) {
        try {
            /**
             * When deserializing JSON into a generic collection like List<Topic>, 
             * Jackson needs to know what type of objects should be in that list. 
             * Without this information, Jackson would only know to create a List, 
             * but wouldn't know to convert each element to a Topic object.
             */
            return objectMapper.readValue(json, new TypeReference<List<Topic>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}