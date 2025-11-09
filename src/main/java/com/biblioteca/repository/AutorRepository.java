package com.biblioteca.repository;

import com.biblioteca.entity.Autor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class AutorRepository {

    @PersistenceContext
    EntityManager em;

    public List<Autor> listAll() {
        return em.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(a) FROM Autor a", Long.class).getSingleResult();
    }

    public void persist(Autor autor) {
        em.persist(autor);
    }

    public boolean deleteById(Long id) {
        Autor autor = em.find(Autor.class, id);
        if (autor != null) {
            em.remove(autor);
            return true;
        }
        return false;
    }
}
