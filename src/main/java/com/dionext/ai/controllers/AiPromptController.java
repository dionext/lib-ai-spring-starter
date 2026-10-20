package com.dionext.ai.controllers;

import com.dionext.ai.entity.AiPrompt;
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
@RequestMapping("/aiprompts")
public class AiPromptController {

    private final AiPromptService aiPromptService;

    public AiPromptController(AiPromptService aiPromptService) {
        this.aiPromptService = aiPromptService;
    }

    @GetMapping
    public String listAiPrompts(
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
        Page<AiPrompt> aiPromptPage;
        
        if (search != null && !search.trim().isEmpty()) {
            aiPromptPage = aiPromptService.search(search, pageable);
            model.addAttribute("search", search);
        } else {
            aiPromptPage = aiPromptService.findAll(pageable);
        }
        
        model.addAttribute("aiprompts", aiPromptPage.getContent());
        model.addAttribute("currentPage", aiPromptPage.getNumber());
        model.addAttribute("totalPages", aiPromptPage.getTotalPages());
        model.addAttribute("totalItems", aiPromptPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        
        // Add page numbers for pagination
        if (aiPromptPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(
                    Math.max(0, page - 2), 
                    Math.min(page + 2, aiPromptPage.getTotalPages() - 1))
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        return "aiprompts/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("aiPrompt", new AiPrompt());
        return "aiprompts/edit";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<AiPrompt> aiPrompt = aiPromptService.findById(id);
        if (aiPrompt.isEmpty()) {
            return "redirect:/aiprompts";
        }
        model.addAttribute("aiPrompt", aiPrompt.get());
        return "aiprompts/edit";
    }

    @PostMapping
    public String saveAiPrompt(@Valid @ModelAttribute("aiPrompt") AiPrompt aiPrompt, 
                            BindingResult result, 
                            Model model) {
        if (result.hasErrors()) {
            return "aiprompts/edit";
        }
        
        aiPromptService.save(aiPrompt);
        return "redirect:/aiprompts";
    }

    @GetMapping("/{id}/delete")
    public String deleteAiPrompt(@PathVariable Long id) {
        aiPromptService.deleteById(id);
        return "redirect:/aiprompts";
    }

    @GetMapping("/{id}/view")
    public String viewAiPrompt(@PathVariable Long id, Model model) {
        Optional<AiPrompt> aiPrompt = aiPromptService.findById(id);
        if (aiPrompt.isEmpty()) {
            return "redirect:/aiprompts";
        }
        model.addAttribute("aiPrompt", aiPrompt.get());
        return "aiprompts/view";
    }
} 