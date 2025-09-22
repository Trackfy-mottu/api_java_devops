package com.example.challenge_mottu_java.controller.web;

import com.example.challenge_mottu_java.model.Court;
import com.example.challenge_mottu_java.model.User;
import com.example.challenge_mottu_java.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import com.example.challenge_mottu_java.dto.CourtDTO;
import com.example.challenge_mottu_java.service.CourtService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/web")
public class RegisterFormController {

    private final CourtService courtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterFormController(CourtService courtService, UserService userService, PasswordEncoder passwordEncoder) {
        this.courtService = courtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        List<CourtDTO> courts = courtService.getAllCourts();
        model.addAttribute("courts", courts);
        model.addAttribute("user", new User());
        return "register-form";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            List<CourtDTO> courts = courtService.getAllCourts();
            model.addAttribute("courts", courts);
            return "register-form";
        }

        try {
            Court court = courtService.getCourtByAcessCode(user.getCourt().getAcessCode());
            user.setCourt(court);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.createUser(user);
            return "redirect:/web/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/web/bike/form";
        }
    }
}
