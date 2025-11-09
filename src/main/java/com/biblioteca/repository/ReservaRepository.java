package com.biblioteca.repository;

import com.biblioteca.entity.Reserva;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositório responsável pelas operações de Reserva.
 * 
 * Com o Panache, já temos acesso aos métodos:
 *  - listAll() → lista todas as reservas
 *  - findById(id) → busca uma reserva pelo ID
 *  - persist(reserva) → salva uma reserva
 *  - deleteById(id) → remove pelo ID
 *  - count() → total de reservas
 */
@ApplicationScoped
public class ReservaRepository implements PanacheRepository<Reserva> {

    /**
     * Retorna a reserva ativa de um livro específico, caso exista.
     *
     * @param idLivro ID do livro
     * @return Reserva ativa ou null se não existir
     */
    public Reserva findAtivaPorLivro(Long idLivro) {
        return find("livro.id = ?1 and ativa = true", idLivro)
                .firstResultOptional()
                .orElse(null);
    }
}
