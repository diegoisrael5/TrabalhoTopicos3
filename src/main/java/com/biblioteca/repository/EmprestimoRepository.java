package com.biblioteca.repository;

import com.biblioteca.entity.Emprestimo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class EmprestimoRepository {

    @PersistenceContext
    EntityManager em;

    public List<Emprestimo> findAll() {
        return em.createQuery(
            "SELECT e FROM Emprestimo e LEFT JOIN FETCH e.livro l LEFT JOIN FETCH l.autor",
            Emprestimo.class
        ).getResultList();
    }

    public List<Emprestimo> findAtivos() {
        return em.createQuery(
            "SELECT e FROM Emprestimo e LEFT JOIN FETCH e.livro l LEFT JOIN FETCH l.autor " +
            "WHERE e.dataDevolucao IS NULL",
            Emprestimo.class
        ).getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(e) FROM Emprestimo e", Long.class).getSingleResult();
    }

    public long countAtivos() {
        return em.createQuery(
            "SELECT COUNT(e) FROM Emprestimo e WHERE e.dataDevolucao IS NULL",
            Long.class
        ).getSingleResult();
    }

    public Emprestimo findById(Long id) {
        return em.find(Emprestimo.class, id);
    }

    @Transactional
    public void persist(Emprestimo emprestimo) {
        em.persist(emprestimo);
    }

    @Transactional
    public Emprestimo update(Emprestimo emprestimo) {
        return em.merge(emprestimo);
    }
}
