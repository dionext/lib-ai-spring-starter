package com.dionext.ai.controllers;

import com.dionext.ai.entity.AiRequest;
import com.dionext.ai.services.AiRequestWebService;
import com.dionext.ai.services.AiModelService;
import com.dionext.ai.services.AiPromptService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/airequests")
public class AiRequestController {

    private final AiRequestWebService aiRequestWebService;
    private final AiModelService aiModelService;
    private final AiPromptService aiPromptService;

    public AiRequestController(AiRequestWebService aiRequestWebService, 
                              AiModelService aiModelService,
                              AiPromptService aiPromptService) {
        this.aiRequestWebService = aiRequestWebService;
        this.aiModelService = aiModelService;
        this.aiPromptService = aiPromptService;
    }

    @GetMapping
    public String listAiRequests(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id,desc") String sort,
            @RequestParam(name = "search", required = false) String search) {
        
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<AiRequest> aiRequestPage;
        
        if (search != null && !search.trim().isEmpty()) {
            aiRequestPage = aiRequestWebService.search(search, pageable);
            model.addAttribute("search", search);
        } else {
            aiRequestPage = aiRequestWebService.findAll(pageable);
        }
        
        model.addAttribute("airequests", aiRequestPage.getContent());
        model.addAttribute("currentPage", aiRequestPage.getNumber());
        model.addAttribute("totalPages", aiRequestPage.getTotalPages());
        model.addAttribute("totalItems", aiRequestPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        
        // Add page numbers for pagination
        if (aiRequestPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(
                    Math.max(0, page - 2), 
                    Math.min(page + 2, aiRequestPage.getTotalPages() - 1))
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        return "airequests/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("aiRequest", new AiRequest());
        model.addAttribute("aiModels", aiModelService.findAll());
        model.addAttribute("aiPrompts", aiPromptService.findAll());
        return "airequests/edit";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<AiRequest> aiRequest = aiRequestWebService.findById(id);
        if (aiRequest.isEmpty()) {
            return "redirect:/airequests";
        }
        model.addAttribute("aiRequest", aiRequest.get());
        model.addAttribute("aiModels", aiModelService.findAll());
        model.addAttribute("aiPrompts", aiPromptService.findAll());
        return "airequests/edit";
    }

    @GetMapping("/{id}/view")
    public String viewAiRequest(@PathVariable Long id, Model model) {
        Optional<AiRequest> aiRequest = aiRequestWebService.findById(id);
        if (aiRequest.isEmpty()) {
            return "redirect:/airequests";
        }
        model.addAttribute("aiRequest", aiRequest.get());
        
        // Add model and prompt directly from entity references
        if (aiRequest.get().getAiModel() != null) {
            model.addAttribute("aiModel", aiRequest.get().getAiModel());
        }
        
        if (aiRequest.get().getAiPrompt() != null) {
            model.addAttribute("aiPrompt", aiRequest.get().getAiPrompt());
        }
        
        return "airequests/view";
    }

    @PostMapping
    public String saveAiRequest(@Valid @ModelAttribute("aiRequest") AiRequest aiRequest, 
                            BindingResult result, 
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("aiModels", aiModelService.findAll());
            model.addAttribute("aiPrompts", aiPromptService.findAll());
            return "airequests/edit";
        }
        
        aiRequestWebService.save(aiRequest);
        return "redirect:/airequests";
    }

    @GetMapping("/{id}/delete")
    public String deleteAiRequest(@PathVariable Long id) {
        aiRequestWebService.deleteById(id);
        return "redirect:/airequests";
    }
} 