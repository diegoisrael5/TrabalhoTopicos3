package com.biblioteca.repository;

import com.biblioteca.entity.Livro;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class LivroRepository implements PanacheRepository<Livro> {

    // =========================================================
    // LISTAGENS E CONSULTAS
    // =========================================================

    /** Retorna todos os livros com seus autores (evita LazyInitializationException). */
    public List<Livro> listarTodosComAutor() {
        return find("SELECT l FROM Livro l LEFT JOIN FETCH l.autor").list();
    }

    /** Retorna os livros conforme disponibilidade (true = disponível). */
    public List<Livro> buscarPorDisponibilidade(boolean disponivel) {
        return find("disponivel", disponivel).list();
    }

    /** Conta o total de livros. */
    public long contar() {
        return count();
    }

    /** Conta livros por status de disponibilidade. */
    public long contarPorDisponibilidade(boolean disponivel) {
        return find("disponivel", disponivel).count();
    }

    // =========================================================
    // OPERAÇÕES DE PERSISTÊNCIA
    // =========================================================

    @Transactional
    public void salvar(Livro livro) {
        if (livro.getId() == null) {
            persist(livro);
        } else {
            getEntityManager().merge(livro);
        }
    }

    @Transactional
    public void excluirPorId(Long id) {
        deleteById(id);
    }
}
