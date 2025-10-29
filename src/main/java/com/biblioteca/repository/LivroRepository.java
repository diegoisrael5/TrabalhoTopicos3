package com.biblioteca.repository;

import com.biblioteca.entity.Livro;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class LivroRepository {

    @PersistenceContext
    EntityManager em;

    // =========================================================
    // LISTAGENS E CONSULTAS
    // =========================================================

    public List<Livro> findAll() {
        // LEFT JOIN FETCH para evitar LazyInitializationException ao acessar autor
        return em.createQuery(
            "SELECT l FROM Livro l LEFT JOIN FETCH l.autor",
            Livro.class
        ).getResultList();
    }

    public Livro findById(Long id) {
        return em.find(Livro.class, id);
    }

    public List<Livro> findByDisponivel(boolean disponivel) {
        return em.createQuery(
            "SELECT l FROM Livro l LEFT JOIN FETCH l.autor WHERE l.disponivel = :disp",
            Livro.class
        ).setParameter("disp", disponivel).getResultList();
    }

    public long count() {
        return em.createQuery(
            "SELECT COUNT(l) FROM Livro l",
            Long.class
        ).getSingleResult();
    }

    public long countByDisponivel(boolean disponivel) {
        return em.createQuery(
            "SELECT COUNT(l) FROM Livro l WHERE l.disponivel = :disp",
            Long.class
        ).setParameter("disp", disponivel).getSingleResult();
    }

    // =========================================================
    // OPERAÇÕES DE PERSISTÊNCIA
    // =========================================================

    @Transactional
    public void persist(Livro livro) {
        em.persist(livro);
    }

    @Transactional
    public Livro update(Livro livro) {
        return em.merge(livro);
    }

    @Transactional
    public void deleteById(Long id) {
        Livro l = em.find(Livro.class, id);
        if (l != null) {
            em.remove(l);
        }
    }
}
