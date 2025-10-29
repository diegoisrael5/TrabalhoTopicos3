package com.biblioteca.converter;

import com.biblioteca.entity.Livro;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@FacesConverter(value = "livroConverter", managed = true)
@ApplicationScoped
public class LivroConverter implements Converter<Livro> {

    @Inject
    EntityManager em;

    @Override
    public Livro getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return em.find(Livro.class, Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Livro livro) {
        if (livro == null || livro.getId() == null) {
            return "";
        }
        return livro.getId().toString();
    }
}
