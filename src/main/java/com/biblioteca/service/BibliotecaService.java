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

    // ======================================================
    // LISTAGENS
    // ======================================================
    public List<Autor> listarTodosAutores() {
        return autorRepository.findAll();
    }

    public List<Livro> listarTodosLivros() {
        return livroRepository.findAll();
    }

    public List<Livro> listarLivrosDisponiveis() {
        return livroRepository.findByDisponivel(true);
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.findAtivos();
    }

    // ======================================================
    // ESTATÍSTICAS
    // ======================================================
    public long contarTotalLivros() {
        return livroRepository.count();
    }

    public long contarLivrosDisponiveis() {
        return livroRepository.countByDisponivel(true);
    }

    public long contarEmprestimosAtivos() {
        return emprestimoRepository.countAtivos();
    }

    public long contarTotalAutores() {
        return autorRepository.count();
    }

    // ======================================================
    // OPERAÇÕES DE NEGÓCIO (Empréstimos e Devoluções)
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

        if (Boolean.FALSE.equals(livro.getDisponivel())) {
            throw new IllegalStateException("O livro '" + livro.getTitulo() + "' não está disponível para empréstimo.");
        }

        // Define as datas do empréstimo
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));

        // Atualiza o status e persiste
        livro.setDisponivel(false);
        em.merge(livro);
        em.persist(emprestimo);
        em.flush(); // garante sincronização imediata
    }

    @Transactional
    public void registrarDevolucao(Emprestimo emprestimo) {
        if (emprestimo == null || emprestimo.getId() == null) {
            throw new IllegalArgumentException("Empréstimo inválido.");
        }

        Emprestimo emprestimoGerenciado = em.find(Emprestimo.class, emprestimo.getId());
        if (emprestimoGerenciado == null) {
            throw new IllegalStateException("Empréstimo não encontrado.");
        }

        // Marca devolução e atualiza
        emprestimoGerenciado.setDataDevolucao(LocalDate.now());
        em.merge(emprestimoGerenciado);

        // Libera o livro novamente
        Livro livro = emprestimoGerenciado.getLivro();
        if (livro != null) {
            livro.setDisponivel(true);
            em.merge(livro);
        }

        em.flush(); // sincroniza imediatamente com o banco
    }

    // ======================================================
    // CADASTRO DE AUTORES E LIVROS
    // ======================================================
    @Transactional
    public void cadastrarAutor(Autor autor) {
        if (autor == null) {
            throw new IllegalArgumentException("Autor inválido.");
        }
        em.persist(autor);
    }

    @Transactional
    public void cadastrarLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro inválido.");
        }

        if (livro.getDisponivel() == null) {
            livro.setDisponivel(true);
        }

        em.persist(livro);
    }

    // ======================================================
    // REMOÇÃO DE REGISTROS
    // ======================================================
    @Transactional
    public void removerLivro(Long id) {
        Livro livro = em.find(Livro.class, id);
        if (livro != null) {
            em.remove(livro);
        } else {
            throw new IllegalStateException("Livro não encontrado para exclusão.");
        }
    }

    @Transactional
    public void removerAutor(Long id) {
        Autor autor = em.find(Autor.class, id);
        if (autor != null) {
            em.remove(autor);
        } else {
            throw new IllegalStateException("Autor não encontrado para exclusão.");
        }
    }

    @Inject
    ReservaRepository reservaRepository;

    @Transactional
    public void registrarReserva(String nomeUsuario, String emailUsuario, Livro livro) {
        if (livro == null || livro.getId() == null) {
            throw new IllegalArgumentException("Livro inválido para reserva.");
        }

        if (Boolean.TRUE.equals(livro.getDisponivel())) {
            throw new IllegalStateException("O livro está disponível — faça o empréstimo diretamente.");
        }

        // Verifica se já existe reserva ativa
        if (reservaRepository.findAtivaPorLivro(livro.getId()) != null) {
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

}
