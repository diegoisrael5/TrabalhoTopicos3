package com.biblioteca.controller;

import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Livro;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.service.BibliotecaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("bibliotecaBean")
@ViewScoped
public class BibliotecaBean implements Serializable {

    @Inject
    BibliotecaService service;

    private List<Autor> autores;
    private List<Livro> livros;
    private List<Emprestimo> emprestimosAtivos;

    private long totalLivros;
    private long livrosDisponiveis;
    private long emprestimosAtivosCount;
    private long totalAutores;

    @PostConstruct
    public void init() {
        carregarDados();
        carregarEstatisticas();
    }

    public void carregarDados() {
        try {
            this.autores = service.listarTodosAutores();
            this.livros = service.listarTodosLivros();
            this.emprestimosAtivos = service.listarEmprestimosAtivos();
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            this.autores = Collections.emptyList();
            this.livros = Collections.emptyList();
            this.emprestimosAtivos = Collections.emptyList();
        }
    }

    public void carregarEstatisticas() {
        try {
            this.totalLivros = service.contarTotalLivros();
            this.livrosDisponiveis = service.contarLivrosDisponiveis();
            this.emprestimosAtivosCount = service.contarEmprestimosAtivos();
            this.totalAutores = service.contarTotalAutores();
        } catch (Exception e) {
            System.err.println("Erro ao carregar estatísticas: " + e.getMessage());
            this.totalLivros = 0;
            this.livrosDisponiveis = 0;
            this.emprestimosAtivosCount = 0;
            this.totalAutores = 0;
        }
    }

    public void recarregarDados() {
        init();
    }

    // Getters (JSF acessa via EL)
    public List<Autor> getAutores() { return autores; }
    public List<Livro> getLivros() { return livros; }
    public List<Emprestimo> getEmprestimosAtivos() { return emprestimosAtivos; }

    public long getTotalLivros() { return totalLivros; }
    public long getLivrosDisponiveis() { return livrosDisponiveis; }
    public long getEmprestimosAtivosCount() { return emprestimosAtivosCount; }
    public long getTotalAutores() { return totalAutores; }
}
