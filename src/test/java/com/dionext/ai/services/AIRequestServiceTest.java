package com.dionext.ai.services;

import com.dionext.ai.entity.AiModel;
import com.dionext.ai.repositories.AiModelRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AIRequestServiceTest {

    @Mock
    private AiModelRepository aiModelRepository;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private AIRequestService aiRequestService;

    @Test
    public void testPostConstructLoadsModelsFromJson() throws IOException {
        // Arrange
        List<AiModel> emptyList = new ArrayList<>();
        when(aiModelRepository.findAll()).thenReturn(emptyList);
        
        // Mock saving models
        when(aiModelRepository.save(any(AiModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        aiRequestService.postConstruct();
        
        // Assert
        // Verify that save was called at least once (models were loaded from JSON)
        verify(aiModelRepository, atLeastOnce()).save(any(AiModel.class));
    }
} 