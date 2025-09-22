package com.example.challenge_mottu_java.controller.api;

import com.example.challenge_mottu_java.dto.CourtDTO;
import com.example.challenge_mottu_java.model.Court;
import com.example.challenge_mottu_java.service.CourtService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("api/court")
public class CourtController {

    private final CourtService courtService;
    private final MessageSource messageSource;
    Logger log = LoggerFactory.getLogger(getClass());

    public CourtController(CourtService courtService, MessageSource messageSource) {
        this.courtService = courtService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{acessCode}")
    public ResponseEntity<CourtDTO> getCourtByAcessCode(@PathVariable String acessCode) {
        try {
            return ResponseEntity.ok(courtService.getCourtByAcessCode(acessCode).courtToDTO());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourtDTO>> getAllCourts() {
        List<CourtDTO> courts = courtService.getAllCourts();
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/form")
    public String form(Court court) {
        return "court-form";
    }

    @PostMapping()
    public ResponseEntity<CourtDTO> createCourt(@RequestBody @Valid Court court) {
        try {
            CourtDTO newCourt = courtService.createCourt(court);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCourt);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/form")
    public String save(@Valid Court court, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) return "court-form";

        try {
            courtService.createCourt(court);
            var message = messageSource.getMessage("court.create.success", null, LocaleContextHolder.getLocale());
            redirect.addFlashAttribute("message", message);
            return "redirect:/court";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/court/form";
        }
    }

    @PutMapping("/{acessCode}")
    public ResponseEntity<CourtDTO> updateCourt(@PathVariable String acessCode, @RequestBody @Valid Court newCourt) {
        try {
            CourtDTO court = courtService.updateCourt(acessCode, newCourt);
            return ResponseEntity.ok(court);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{acessCode}")
    public ResponseEntity<Void> deleteCourt(@PathVariable String acessCode) {
        try {
            courtService.deleteCourt(acessCode);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}