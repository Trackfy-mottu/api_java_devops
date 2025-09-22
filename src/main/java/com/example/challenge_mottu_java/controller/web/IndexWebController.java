package com.example.challenge_mottu_java.controller.web;

import com.example.challenge_mottu_java.dto.BikeDTO;
import com.example.challenge_mottu_java.dto.CourtDTO;
import com.example.challenge_mottu_java.model.User;
import com.example.challenge_mottu_java.service.BikeService;
import com.example.challenge_mottu_java.service.CourtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web")
public class IndexWebController {

    private final CourtService courtservice;
    private final BikeService bikeService;

    public IndexWebController(CourtService courtservice, BikeService bikeService) {
        this.courtservice = courtservice;
        this.bikeService = bikeService;
    }

    @GetMapping
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        CourtDTO court = courtservice.getCourtByAcessCode(user.getCourt().getAcessCode())
                .courtToDTO();
        List<BikeDTO> bikes = bikeService.getBikeByAcessCode(user.getCourt().getAcessCode());

        // Garantir que bikes nunca seja null
        if (bikes == null) {
            bikes = List.of(); // Lista vazia ao invés de null
        }


        Map<String, Integer> bikesStats = new HashMap<>();
        bikesStats.put("prontasParaUso", 0);
        bikesStats.put("emUso", 0);
        bikesStats.put("emManutencao", 0);
        bikesStats.put("descarte", 0);

        // Só processar se houver bikes
        for (BikeDTO bike : bikes) {
            if (bike != null && bike.status() != null) {
                switch (bike.status().name()) {
                    case "ProntoParaUso":
                        bikesStats.put("prontasParaUso", bikesStats.get("prontasParaUso") + 1);
                        break;
                    case "EmUso":
                        bikesStats.put("emUso", bikesStats.get("emUso") + 1);
                        break;
                    case "Manutencao":
                        bikesStats.put("emManutencao", bikesStats.get("emManutencao") + 1);
                        break;
                    case "Descarte":
                        bikesStats.put("descarte", bikesStats.get("descarte") + 1);
                        break;
                }
            }
        }

        model.addAttribute("court", court);
        model.addAttribute("bikes", bikes);
        model.addAttribute("user", user);
        model.addAttribute("bikesStats", bikesStats);
        return "index";
    }
}