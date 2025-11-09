package com.biblioteca.mapper;

import com.biblioteca.dto.LivroDTO;
import com.biblioteca.entity.Livro;

public class LivroMapper {

    public static LivroDTO toDTO(Livro livro) {
        if (livro == null) return null;
        return new LivroDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAutor() != null ? livro.getAutor().getNome() : null,
                livro.getDisponivel()
        );
    }
}
