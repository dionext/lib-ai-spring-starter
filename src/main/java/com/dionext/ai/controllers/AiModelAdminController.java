package com.dionext.ai.controllers;

import com.dionext.ai.entity.AiModel;
import com.dionext.ai.repositories.AiModelRepository;
import com.dionext.ai.utils.ModelJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for AI model administration tasks
 */
@Controller
@RequestMapping("/aimodels/admin")
public class AiModelAdminController {
    private static final Logger logger = LoggerFactory.getLogger(AiModelAdminController.class);
    
    @Autowired
    private AiModelRepository aiModelRepository;
    
    /**
     * Display the admin page
     */
    @GetMapping
    public String adminPage(Model model) {
        try {
            String jsonContent = ModelJsonUtils.readModelJsonAsString();
            model.addAttribute("jsonContent", jsonContent);
        } catch (IOException e) {
            logger.error("Failed to read model.json", e);
            model.addAttribute("error", "Failed to read model.json: " + e.getMessage());
        }
        
        return "aimodels/admin";
    }
    
    /**
     * Export current models to JSON
     */
    @GetMapping("/export")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> exportModels() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<AiModel> models = aiModelRepository.findAll();
            
            // Export to a temporary file
            String tempDir = System.getProperty("java.io.tmpdir");
            String outputPath = Paths.get(tempDir, "model-export.json").toString();
            
            ModelJsonUtils.exportModelsToJson(models, outputPath);
            
            response.put("success", true);
            response.put("message", "Exported " + models.size() + " models to " + outputPath);
            response.put("filePath", outputPath);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to export models", e);
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get the current model.json content
     */
    @GetMapping("/json")
    @ResponseBody
    public ResponseEntity<String> getModelJson() {
        try {
            String jsonContent = ModelJsonUtils.readModelJsonAsString();
            return ResponseEntity.ok(jsonContent);
        } catch (IOException e) {
            logger.error("Failed to read model.json", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to read model.json: " + e.getMessage());
        }
    }
} 