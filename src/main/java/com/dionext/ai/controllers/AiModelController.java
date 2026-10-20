package com.dionext.ai.controllers;

import com.dionext.ai.entity.AiModel;
import com.dionext.ai.entity.AiLaunchType;
import com.dionext.ai.services.AiModelService;
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
@RequestMapping("/aimodels")
public class AiModelController {

    private final AiModelService aiModelService;

    public AiModelController(AiModelService aiModelService) {
        this.aiModelService = aiModelService;
    }

    @GetMapping
    public String listAiModels(
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
        Page<AiModel> aiModelPage;
        
        if (search != null && !search.trim().isEmpty()) {
            aiModelPage = aiModelService.search(search, pageable);
            model.addAttribute("search", search);
        } else {
            aiModelPage = aiModelService.findAll(pageable);
        }
        
        model.addAttribute("aimodels", aiModelPage.getContent());
        model.addAttribute("currentPage", aiModelPage.getNumber());
        model.addAttribute("totalPages", aiModelPage.getTotalPages());
        model.addAttribute("totalItems", aiModelPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        
        // Add page numbers for pagination
        if (aiModelPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(
                    Math.max(0, page - 2), 
                    Math.min(page + 2, aiModelPage.getTotalPages() - 1))
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        return "aimodels/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("aiModel", new AiModel());
        model.addAttribute("launchTypes", AiLaunchType.values());
        return "aimodels/edit";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<AiModel> aiModel = aiModelService.findById(id);
        if (aiModel.isEmpty()) {
            return "redirect:/aimodels";
        }
        model.addAttribute("aiModel", aiModel.get());
        model.addAttribute("launchTypes", AiLaunchType.values());
        return "aimodels/edit";
    }

    @PostMapping
    public String saveAiModel(@Valid @ModelAttribute("aiModel") AiModel aiModel, 
                            BindingResult result, 
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("launchTypes", AiLaunchType.values());
            return "aimodels/edit";
        }
        
        aiModelService.save(aiModel);
        return "redirect:/aimodels";
    }

    @GetMapping("/{id}/delete")
    public String deleteAiModel(@PathVariable Long id) {
        aiModelService.deleteById(id);
        return "redirect:/aimodels";
    }

    @GetMapping("/{id}/view")
    public String viewAiModel(@PathVariable Long id, Model model) {
        Optional<AiModel> aiModel = aiModelService.findById(id);
        if (aiModel.isEmpty()) {
            return "redirect:/aimodels";
        }
        model.addAttribute("aiModel", aiModel.get());
        return "aimodels/view";
    }
} 