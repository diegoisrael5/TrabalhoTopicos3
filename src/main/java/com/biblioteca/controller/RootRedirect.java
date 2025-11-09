package com.biblioteca.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/")
public class RootRedirect {

    @GET
    public Response redirectToIndex() {
        // Redireciona a raiz para o index.xhtml
        return Response.status(Response.Status.FOUND)
                       .header("Location", "/index.xhtml")
                       .build();
    }
}
