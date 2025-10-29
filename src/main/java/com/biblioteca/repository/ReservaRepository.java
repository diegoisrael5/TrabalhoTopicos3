package com.biblioteca.repository;

import com.biblioteca.entity.Reserva;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ReservaRepository {

    @PersistenceContext
    EntityManager em;

    public List<Reserva> findAll() {
        return em.createQuery("SELECT r FROM Reserva r", Reserva.class).getResultList();
    }

    public Reserva findAtivaPorLivro(Long livroId) {
        return em.createQuery(
            "SELECT r FROM Reserva r WHERE r.livro.id = :id AND r.ativa = true", Reserva.class)
            .setParameter("id", livroId)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }

    @Transactional
    public void persist(Reserva reserva) {
        em.persist(reserva);
    }

    @Transactional
    public void desativarReserva(Long id) {
        Reserva reserva = em.find(Reserva.class, id);
        if (reserva != null) {
            reserva.setAtiva(false);
            em.merge(reserva);
        }
    }
}
