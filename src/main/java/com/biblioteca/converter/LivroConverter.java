package com.biblioteca.converter;

import com.biblioteca.entity.Livro;
import com.biblioteca.repository.LivroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "livroConverter", managed = true)
@ApplicationScoped
public class LivroConverter implements Converter<Livro> {

    @Inject
    LivroRepository livroRepository;

    @Override
    public Livro getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return livroRepository.findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Livro livro) {
        if (livro == null || livro.getId() == null) {
            return "";
        }
        return livro.getId().toString();
    }
}
