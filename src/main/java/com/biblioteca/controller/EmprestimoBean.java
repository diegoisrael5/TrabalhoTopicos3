package com.biblioteca.controller;

import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.service.BibliotecaService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named
@SessionScoped
public class EmprestimoBean implements Serializable {

    @Inject
    BibliotecaService bibliotecaService;

    private Emprestimo novoEmprestimo;
    private List<Emprestimo> emprestimos;
    private List<Livro> livrosDisponiveis;

    @PostConstruct
    public void init() {
        novoEmprestimo = new Emprestimo();
        carregarListas();
    }

    public void carregarListas() {
        emprestimos = bibliotecaService.listarEmprestimosAtivos();
        livrosDisponiveis = bibliotecaService.listarLivrosDisponiveis();
    }

    // ================================================
    // SALVAR NOVO EMPRÉSTIMO
    // ================================================
    public void salvar() {
        try {
            // Validações
            if (novoEmprestimo.getLivro() == null) {
                addMessage("Selecione um livro para o empréstimo.", FacesMessage.SEVERITY_WARN);
                return;
            }
            if (novoEmprestimo.getNomeUsuario() == null || novoEmprestimo.getNomeUsuario().isBlank()) {
                addMessage("Informe o nome do usuário.", FacesMessage.SEVERITY_WARN);
                return;
            }
            if (novoEmprestimo.getEmailUsuario() == null || novoEmprestimo.getEmailUsuario().isBlank()) {
                addMessage("Informe o e-mail do usuário.", FacesMessage.SEVERITY_WARN);
                return;
            }

            // Define as datas automaticamente
            novoEmprestimo.setDataEmprestimo(LocalDate.now());
            novoEmprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(14));

            bibliotecaService.registrarEmprestimo(novoEmprestimo);

            addMessage("📘 Empréstimo registrado com sucesso!", FacesMessage.SEVERITY_INFO);
            carregarListas();
            novoEmprestimo = new Emprestimo(); // limpa o formulário

        } catch (Exception e) {
            addMessage("❌ Erro ao registrar empréstimo: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    // ================================================
    // DEVOLVER LIVRO
    // ================================================
    public void devolver(Emprestimo emprestimo) {
        try {
            bibliotecaService.registrarDevolucao(emprestimo);
            addMessage("📗 Livro devolvido com sucesso!", FacesMessage.SEVERITY_INFO);
            carregarListas();
        } catch (Exception e) {
            addMessage("❌ Erro ao processar devolução: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    // ================================================
    // RESERVAR LIVRO
    // ================================================
    public void reservar(Livro livro) {
        try {
            if (livro == null) {
                addMessage("Selecione um livro válido para reserva.", FacesMessage.SEVERITY_WARN);
                return;
            }

            // Dados mínimos para reservar
            String nomeUsuario = novoEmprestimo.getNomeUsuario();
            String emailUsuario = novoEmprestimo.getEmailUsuario();

            if (nomeUsuario == null || nomeUsuario.isBlank() ||
                emailUsuario == null || emailUsuario.isBlank()) {
                addMessage("Informe o nome e e-mail do usuário antes de reservar.", FacesMessage.SEVERITY_WARN);
                return;
            }

            bibliotecaService.registrarReserva(nomeUsuario, emailUsuario, livro);
            addMessage("📙 Reserva registrada com sucesso!", FacesMessage.SEVERITY_INFO);

            carregarListas();

        } catch (Exception e) {
            addMessage("❌ Erro ao reservar livro: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    // ================================================
    // UTILITÁRIO PARA MENSAGENS
    // ================================================
    private void addMessage(String msg, FacesMessage.Severity tipo) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, msg, null));
    }

    // ================================================
    // GETTERS / SETTERS
    // ================================================
    public Emprestimo getNovoEmprestimo() {
        return novoEmprestimo;
    }

    public void setNovoEmprestimo(Emprestimo novoEmprestimo) {
        this.novoEmprestimo = novoEmprestimo;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public List<Livro> getLivrosDisponiveis() {
        return livrosDisponiveis;
    }
}
