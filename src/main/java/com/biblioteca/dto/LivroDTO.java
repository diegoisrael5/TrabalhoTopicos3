package com.biblioteca.dto;

public record LivroDTO(
        Long id,
        String titulo,
        String isbn,
        String autorNome,
        Boolean disponivel
) {}
