package com.example.challenge_mottu_java.controller.web;

import com.example.challenge_mottu_java.model.Bike;
import com.example.challenge_mottu_java.model.User;
import com.example.challenge_mottu_java.service.BikeService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/bike")
public class BikeWebController {

    private final BikeService bikeService;
    private final MessageSource messageSource;

    public BikeWebController(BikeService bikeService, MessageSource messageSource) {
        this.bikeService = bikeService;
        this.messageSource = messageSource;
    }

    @GetMapping("/form")
    public String form(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Bike bike = new Bike();
        bike.setCourt(user.getCourt());

        model.addAttribute("bike", bike);
        model.addAttribute("court", user.getCourt());
        return "bike-form";
    }

    @PostMapping("/form")
    public String save(@Valid Bike bike, BindingResult result, RedirectAttributes redirect, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (result.hasErrors()) {
            model.addAttribute("court", user.getCourt());
            return "bike-form";
        }

        try {
            bike.setCourt(user.getCourt());
            bikeService.createBike(bike);

            var message = messageSource.getMessage("bike.create.success", null, LocaleContextHolder.getLocale());
            redirect.addFlashAttribute("message", message != null ? message : "Moto cadastrada com sucesso!");
            return "redirect:/web";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/bike/form";
        }
    }

    @GetMapping("/{placa}")
    public String detalhes(@PathVariable("placa") String placa, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Bike bike = bikeService.findByPlacaAndCourt(placa, user.getCourt());
        if (bike == null) {
            model.addAttribute("error", "Moto não encontrada neste pátio.");
            return "redirect:/web/dashboard"; // ou alguma página de erro
        }

        model.addAttribute("user",user);
        model.addAttribute("bike", bike);
        model.addAttribute("court", user.getCourt());
        return "bike-details"; // nome do template Thymeleaf
    }

    @PostMapping("/delete/{id}")
    public String deleteBike(@PathVariable String id) {
        bikeService.deleteBike(id);
        return "redirect:/web";
    }
}