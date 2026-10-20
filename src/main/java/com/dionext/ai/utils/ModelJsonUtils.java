package com.dionext.ai.utils;

import com.dionext.ai.entity.AiModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utility class for managing the model.json file
 */
public class ModelJsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(ModelJsonUtils.class);
    private static final String MODEL_JSON_PATH = "model.json";
    
    /**
     * Load AI models from the model.json file
     * 
     * @return List of AiModel objects
     * @throws IOException if the file cannot be read
     */
    public static List<AiModel> loadModelsFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource(MODEL_JSON_PATH);
        InputStream inputStream = resource.getInputStream();
        
        return objectMapper.readValue(
            inputStream, 
            new TypeReference<List<AiModel>>() {}
        );
    }
    
    /**
     * Export AI models to the model.json file
     * 
     * @param models List of AiModel objects to export
     * @param outputPath Path where the file should be saved
     * @throws IOException if the file cannot be written
     */
    public static void exportModelsToJson(List<AiModel> models, String outputPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        String json = objectMapper.writeValueAsString(models);
        Files.write(Paths.get(outputPath), json.getBytes(StandardCharsets.UTF_8));
        
        logger.info("Exported {} AI models to {}", models.size(), outputPath);
    }
    
    /**
     * Read the model.json file as a string
     * 
     * @return The contents of the model.json file
     * @throws IOException if the file cannot be read
     */
    public static String readModelJsonAsString() throws IOException {
        ClassPathResource resource = new ClassPathResource(MODEL_JSON_PATH);
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
    }
} 