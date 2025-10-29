package com.biblioteca.repository;

import com.biblioteca.entity.Autor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class AutorRepository {

    @PersistenceContext
    EntityManager em;

    public List<Autor> findAll() {
        return em.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(a) FROM Autor a", Long.class).getSingleResult();
    }

    public Autor findById(Long id) {
        return em.find(Autor.class, id);
    }

    @Transactional
    public void persist(Autor autor) {
        em.persist(autor);
    }

    @Transactional
    public Autor update(Autor autor) {
        return em.merge(autor);
    }

    @Transactional
    public void deleteById(Long id) {
        Autor a = em.find(Autor.class, id);
        if (a != null) em.remove(a);
    }
}
