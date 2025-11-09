package com.biblioteca.repository;

import com.biblioteca.entity.Emprestimo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Repositório responsável pelas operações de Empréstimo.
 * 
 * Com o Panache, já temos métodos prontos como:
 *  - listAll()
 *  - findById(id)
 *  - persist(emprestimo)
 *  - deleteById(id)
 *  - count()
 */
@ApplicationScoped
public class EmprestimoRepository implements PanacheRepository<Emprestimo> {

    /**
     * Retorna todos os empréstimos, carregando livro e autor juntos.
     */
    public List<Emprestimo> listarTodosComLivro() {
        return getEntityManager()
                .createQuery(
                        "SELECT e FROM Emprestimo e " +
                        "LEFT JOIN FETCH e.livro l " +
                        "LEFT JOIN FETCH l.autor",
                        Emprestimo.class)
                .getResultList();
    }

    /**
     * Retorna apenas os empréstimos ativos (sem data de devolução).
     */
    public List<Emprestimo> listarAtivos() {
        return find("dataDevolucao is null").list();
    }

    /**
     * Conta quantos empréstimos estão ativos.
     */
    public long contarAtivos() {
        return find("dataDevolucao is null").count();
    }
}
