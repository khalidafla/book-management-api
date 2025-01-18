package com.example.books.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorDto(
        Long id,
        @NotBlank String name,
        @Positive Integer age,
        @PositiveOrZero Long followersNumber
) {
}
