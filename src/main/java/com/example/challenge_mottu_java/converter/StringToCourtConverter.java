package com.example.challenge_mottu_java.converter;


import com.example.challenge_mottu_java.model.Court;
import com.example.challenge_mottu_java.service.CourtService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCourtConverter implements Converter<String, Court> {

    private final CourtService courtService;

    public StringToCourtConverter(CourtService courtService) {
        this.courtService = courtService;
    }

    @Override
    public Court convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return courtService.getCourtByAcessCode(source);
    }
}
