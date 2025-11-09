package com.biblioteca.controller;

import com.biblioteca.entity.Reserva;
import com.biblioteca.repository.ReservaRepository;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ReservaBean implements Serializable {

    private Reserva reserva = new Reserva();

    @Inject
    private ReservaRepository reservaRepository;

    private List<Reserva> reservas;

    public void salvar() {
        reservaRepository.persist(reserva);
        reserva = new Reserva();
        reservas = reservaRepository.listAll();
    }

    public List<Reserva> getReservas() {
        if (reservas == null) {
            reservas = reservaRepository.listAll();
        }
        return reservas;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
}
