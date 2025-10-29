package com.biblioteca.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_usuario", nullable = false, length = 100)
    private String nomeUsuario;

    @Column(name = "email_usuario", nullable = false, length = 150)
    private String emailUsuario;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDate dataDevolucaoPrevista;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    // 🔹 Construtor padrão exigido pelo JPA
    public Emprestimo() {}

    // 🔹 Construtor básico
    public Emprestimo(String nomeUsuario, String emailUsuario, Livro livro) {
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.livro = livro;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = LocalDate.now().plusDays(14); // padrão de 14 dias
    }

    // 🔹 Método de negócio: verifica se o empréstimo está ativo
    public boolean isAtivo() {
        return this.dataDevolucao == null;
    }

    // 🔹 Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }

    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }

    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) { this.dataDevolucao = dataDevolucao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    // 🔹 Método auxiliar para exibição (opcional)
    @Override
    public String toString() {
        return String.format("%s (%s) - Livro: %s", nomeUsuario, emailUsuario,
                livro != null ? livro.getTitulo() : "N/A");
    }
}
