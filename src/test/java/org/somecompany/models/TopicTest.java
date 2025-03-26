package org.somecompany.models;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.somecompany.util.Json;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TopicTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void pojoToJsonStringTest() {
        Topic topic = new Topic("topic1", "username1");

        String json = Json.convertToJsonString(topic);
        String expected = "{\"name\":\"topic1\",\"author\":\"username1\",\"vote_list\":null}";

        assertEquals(expected, json);
    }

    @Test
    public void jsonStringToPojoTest() {
        String json = "{\"name\":\"topic1\",\"author\":\"username1\",\"vote_list\":null}";

        Topic topic = Json.convertJsonToModel(json, Topic.class);

        assertEquals("topic1", topic.getName());
        assertNull(topic.getVoteList());
        assertEquals("username1", topic.getAuthor());
    }

    @Test
    public void jsonFileToPojoTest() {
        URL resourceUrl = getClass().getClassLoader().getResource("topicTest.json");
        File file = new File(resourceUrl.getFile());

        Topic topic = Json.convertJsonToModel(file, Topic.class);

        assertEquals("topic1", topic.getName());
        assertNull(topic.getVoteList());
        assertEquals("username1", topic.getAuthor());
    }
}