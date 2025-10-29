package com.biblioteca.api;

import com.biblioteca.dto.LivroDTO;
import com.biblioteca.mapper.LivroMapper;
import com.biblioteca.service.BibliotecaService;
import com.biblioteca.entity.Livro;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/livros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LivroResource {

    @Inject
    BibliotecaService service;

    @GET
    public List<LivroDTO> listarLivros() {
        return service.listarTodosLivros()
                      .stream()
                      .map(LivroMapper::toDTO)
                      .toList();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Livro livro = service.listarTodosLivros()
                             .stream()
                             .filter(l -> l.getId().equals(id))
                             .findFirst()
                             .orElse(null);

        if (livro == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Livro não encontrado.")
                           .build();
        }

        return Response.ok(LivroMapper.toDTO(livro)).build();
    }
}
