package org.somecompany.commands.topic;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;
import org.somecompany.server.ServerHandler;
import org.somecompany.exceptions.UsernameTakenException;
import org.somecompany.models.Topic;
import org.somecompany.util.Json;
import org.somecompany.util.LoggerUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

/**
 * Picocli-based command class.
 * 
 * Provides a 'login' command for client
 */
@Command(
    name = "create topic",
    description = "Creation of a topic",
    mixinStandardHelpOptions = true
)
public class CreateTopicCommandCli implements Runnable {
    @Option(
        names = "-n",
        description = "Name of the topic",
        required = true
    )
    private String topicName;
    
    private ChannelHandlerContext ctx;
    private ServerHandler serverHandler;
    
    public CreateTopicCommandCli(ChannelHandlerContext ctx, ServerHandler serverHandler) {
        this.ctx = ctx;
        this.serverHandler = serverHandler;
    }

    @Override
    public void run() {
        String username = serverHandler.getCurrentUsername(ctx);
        Topic newTopic = new Topic(topicName, username);
        
        try {
            Path directory = Paths.get("src/main/resources");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            Path topicsFilePath = directory.resolve("topics.json");
            List<Topic> existingTopics = new ArrayList<>();
            
            // Read existing topics if file exists
            if (Files.exists(topicsFilePath)) {
                File topicsFile = topicsFilePath.toFile();
                String fileContent = new String(Files.readAllBytes(topicsFilePath));
                
                // Only try to parse if file is not empty
                if (fileContent != null && !fileContent.trim().isEmpty()) {
                    try {
                        // Try to parse as a list of topics
                        existingTopics = Json.convertJsonToTopicList(fileContent);
                    } catch (Exception e) {
                        // If file exists but doesn't contain a list, check if it contains a single topic
                        Topic singleTopic = Json.convertJsonToModel(fileContent, Topic.class);
                        if (singleTopic != null) {
                            existingTopics.add(singleTopic);
                        }
                    }
                }
            }
            
            // Check for duplicate topic name
            boolean topicExists = false;
            for (Topic topic : existingTopics) {
                if (topic.getName().equals(topicName)) {
                    topicExists = true;
                    break;
                }
            }
            
            if (topicExists) {
                ctx.writeAndFlush("Topic with name '" + topicName + "' already exists.\n");
                LoggerUtil.info("Topic creation failed: Topic with name '" + topicName + "' already exists.");
                return;
            }
            
            // Add new topic to the list
            existingTopics.add(newTopic);
            
            // Convert the list to JSON and save
            String json = Json.convertTopicListToJson(existingTopics);
            Files.write(topicsFilePath, json.getBytes());
            
            ctx.writeAndFlush("Topic '" + topicName + "' created successfully.\n");
            LoggerUtil.info("Topic '" + topicName + "' created by user '" + username + "'");
        } catch(IOException e) {
            LoggerUtil.error("Error creating topic: " + e.getMessage());
            ctx.writeAndFlush("Error creating topic: " + e.getMessage() + "\n");
        }
    }

    public String getTopicName() {
        return topicName;
    }
}