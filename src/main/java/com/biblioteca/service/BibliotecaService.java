package com.biblioteca.service;

import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.entity.Reserva;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.ReservaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BibliotecaService {

    @Inject
    EntityManager em;

    @Inject
    AutorRepository autorRepository;

    @Inject
    LivroRepository livroRepository;

    @Inject
    EmprestimoRepository emprestimoRepository;

    @Inject
    ReservaRepository reservaRepository;

    // ======================================================
    // LISTAGENS
    // ======================================================
    public List<Autor> listarTodosAutores() {
        return autorRepository.listAll();
    }

    public List<Livro> listarTodosLivros() {
        return livroRepository.listarTodosComAutor();
    }

    public List<Livro> listarLivrosDisponiveis() {
        return livroRepository.buscarPorDisponibilidade(true);
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.listarAtivos();
    }

    // ======================================================
    // ESTATÍSTICAS
    // ======================================================
    public long contarTotalLivros() {
        return livroRepository.contar();
    }

    public long contarLivrosDisponiveis() {
        return livroRepository.contarPorDisponibilidade(true);
    }

    public long contarEmprestimosAtivos() {
        return emprestimoRepository.contarAtivos();
    }

    public long contarTotalAutores() {
        return autorRepository.count();
    }

    // ======================================================
    // OPERAÇÕES DE NEGÓCIO: EMPRÉSTIMOS
    // ======================================================
    @Transactional
    public void registrarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getLivro() == null) {
            throw new IllegalArgumentException("Dados do empréstimo inválidos.");
        }

        Livro livro = em.find(Livro.class, emprestimo.getLivro().getId());
        if (livro == null) {
            throw new IllegalStateException("Livro não encontrado no banco de dados.");
        }

        if (!livro.getDisponivel()) {
            throw new IllegalStateException("O livro '" + livro.getTitulo() + "' não está disponível para empréstimo.");
        }

        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(14));

        livro.setDisponivel(false);
        em.merge(livro);
        em.persist(emprestimo);
        em.flush();
    }

    // ======================================================
    // OPERAÇÕES DE NEGÓCIO: DEVOLUÇÃO
    // ======================================================
    @Transactional
    public void registrarDevolucao(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getId() == null) {
            throw new IllegalArgumentException("Empréstimo inválido.");
        }

        Emprestimo emprestimoGerenciado = em.find(Emprestimo.class, emprestimo.getId());
        if (emprestimoGerenciado == null) {
            throw new IllegalStateException("Empréstimo não encontrado.");
        }

        emprestimoGerenciado.setDataDevolucao(LocalDate.now());
        em.merge(emprestimoGerenciado);

        Livro livro = emprestimoGerenciado.getLivro();
        if (livro != null) {
            livro.setDisponivel(true);
            em.merge(livro);
        }

        em.flush();
    }

    // ======================================================
    // RESERVAS
    // ======================================================
    @Transactional
    public void registrarReserva(String nomeUsuario, String emailUsuario, Livro livro) {
        if (livro == null || livro.getId() == null) {
            throw new IllegalArgumentException("Livro inválido para reserva.");
        }

        if (livro.getDisponivel()) {
            throw new IllegalStateException("O livro está disponível — faça o empréstimo diretamente.");
        }

        Reserva reservaExistente = reservaRepository.findAtivaPorLivro(livro.getId());
        if (reservaExistente != null) {
            throw new IllegalStateException("Já existe uma reserva ativa para este livro.");
        }

        Reserva reserva = new Reserva();
        reserva.setNomeUsuario(nomeUsuario);
        reserva.setEmailUsuario(emailUsuario);
        reserva.setLivro(livro);
        reserva.setDataReserva(LocalDate.now());
        reserva.setAtiva(true);

        reservaRepository.persist(reserva);
    }

    // ======================================================
    // CADASTROS
    // ======================================================
    @Transactional
    public void cadastrarAutor(Autor autor) {
        if (autor == null) {
            throw new IllegalArgumentException("Autor inválido.");
        }
        autorRepository.persist(autor);
    }

    @Transactional
    public void cadastrarLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro inválido.");
        }

        if (livro.getDisponivel() == null) {
            livro.setDisponivel(true);
        }

        livroRepository.persist(livro);
    }

    // ======================================================
    // REMOÇÕES
    // ======================================================
    @Transactional
    public void removerLivro(Long id) {
        if (!livroRepository.deleteById(id)) {
            throw new IllegalStateException("Livro não encontrado para exclusão.");
        }
    }

    @Transactional
    public void removerAutor(Long id) {
        if (!autorRepository.deleteById(id)) {
            throw new IllegalStateException("Autor não encontrado para exclusão.");
        }
    }
}
