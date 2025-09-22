package com.example.challenge_mottu_java.controller.web;

import com.example.challenge_mottu_java.dto.BikeDTO;
import com.example.challenge_mottu_java.model.Pending;
import com.example.challenge_mottu_java.model.User;
import com.example.challenge_mottu_java.service.BikeService;
import com.example.challenge_mottu_java.service.PendingService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/web/pending")
public class PendingWebController {

    private final PendingService pendingService;
    private final BikeService bikeService;
    private final MessageSource messageSource;

    public PendingWebController(PendingService pendingService, BikeService bikeService, MessageSource messageSource) {
        this.pendingService = pendingService;
        this.bikeService = bikeService;
        this.messageSource = messageSource;
    }

    @GetMapping("/form")
    public String form(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<BikeDTO> bikes = bikeService.getBikeByAcessCode(user.getCourt().getAcessCode());

        model.addAttribute("pending", new Pending());
        model.addAttribute("bikes", bikes);
        return "pending-form";
    }

    @PostMapping("/form")
    public String save(@Valid Pending pending, BindingResult result, RedirectAttributes redirect, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (result.hasErrors()) {
            List<BikeDTO> bikes = bikeService.getBikeByAcessCode(user.getCourt().getAcessCode());
            model.addAttribute("bikes", bikes);
            return "pending-form";
        }

        try {
            pendingService.createPending(pending);

            var message = messageSource.getMessage("pending.create.success", null, LocaleContextHolder.getLocale());
            redirect.addFlashAttribute("message", message != null ? message : "Pendência cadastrada com sucesso!");
            return "redirect:/web";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/pending/form";
        }
    }
}