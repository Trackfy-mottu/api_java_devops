package com.example.challenge_mottu_java.converter;

import com.example.challenge_mottu_java.model.Bike;
import com.example.challenge_mottu_java.repository.BikeRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBikeConverter implements Converter<String, Bike> {

    private final BikeRepository bikeRepository;

    public StringToBikeConverter(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    @Override
    public Bike convert(String source) {
        return bikeRepository.findById(source)
                .orElseThrow(() -> new RuntimeException("Moto não encontrada: " + source));
    }
}