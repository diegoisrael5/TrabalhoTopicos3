# 🧩 Como Expor uma API REST de Livros no Projeto **Biblioteca Digital**

## 🎯 Introdução
Neste tutorial, você aprenderá a **criar uma API REST (JAX-RS)** para listar e consultar livros utilizando o **framework Quarkus**.  
Essa melhoria permitirá que o sistema “Biblioteca Digital” disponibilize seus dados para consumo externo — como aplicativos móveis, painéis de BI ou sistemas integradores — mantendo o código já existente com **JSF** totalmente funcional.

A proposta desta extensão é demonstrar como reutilizar o `BibliotecaService` e as entidades existentes, aplicando o padrão REST de forma simples, eficiente e escalável.

---

## ⚙️ Pré-requisitos

Antes de iniciar, verifique se você possui os seguintes itens instalados e configurados:

- **Projeto “Biblioteca Digital”** funcionando localmente.
- **Java 17+** instalado e configurado.
- **Apache Maven** configurado (versão 3.8 ou superior).
- **PostgreSQL** em execução (o mesmo usado no projeto original).
- **Quarkus CLI** (opcional, para execução mais rápida).
- Um cliente para teste de requisições, como **Postman**, **Insomnia** ou **curl**.

---

## 🚀 Objetivo do Tutorial

Criar endpoints REST para:
1. Listar todos os livros cadastrados no sistema.
2. Consultar detalhes de um livro específico por ID.

Esses endpoints retornarão os dados no formato **JSON**, possibilitando o consumo em aplicações externas.

---

## 🪜 Passo a Passo da Implementação

### 1️⃣ Adicionar (ou confirmar) a dependência RESTEasy Reactive

Verifique se no arquivo `pom.xml` já existe a dependência abaixo. Caso não exista, adicione dentro da seção `<dependencies>`:

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-resteasy-reactive-jackson</artifactId>
</dependency>

2️⃣ Criar o DTO (Data Transfer Object)

O DTO define os dados que serão retornados na API.
Crie o arquivo src/main/java/com/biblioteca/dto/LivroDTO.java:

package com.biblioteca.dto;

public record LivroDTO(
        Long id,
        String titulo,
        String isbn,
        String autorNome,
        Boolean disponivel
) {}

3️⃣ Criar o Mapper (Conversor entre Entidade e DTO)

Crie o arquivo src/main/java/com/biblioteca/mapper/LivroMapper.java:

package com.biblioteca.mapper;

import com.biblioteca.dto.LivroDTO;
import com.biblioteca.entity.Livro;

public class LivroMapper {

    public static LivroDTO toDTO(Livro livro) {
        if (livro == null) return null;
        return new LivroDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAutor() != null ? livro.getAutor().getNome() : null,
                livro.getDisponivel()
        );
    }
}

4️⃣ Criar o recurso REST (endpoint)

Crie o arquivo src/main/java/com/biblioteca/api/LivroResource.java:

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

    // Endpoint para listar todos os livros
    @GET
    public List<LivroDTO> listarLivros() {
        return service.listarTodosLivros()
                      .stream()
                      .map(LivroMapper::toDTO)
                      .toList();
    }

    // Endpoint para buscar um livro por ID
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

5️⃣ (Opcional) Habilitar CORS no Quarkus

Se quiser testar a API a partir de um frontend externo, habilite o CORS adicionando no src/main/resources/application.properties:

quarkus.http.cors=true
quarkus.http.cors.origins=*
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS


Isso permite que o navegador aceite requisições de outras origens (ideal para aplicações web).

6️⃣ (Opcional) Criar um teste de integração

Crie o arquivo src/test/java/com/biblioteca/api/LivroResourceTest.java:

package com.biblioteca.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class LivroResourceTest {

    @Test
    public void testListarLivrosEndpoint() {
        given()
          .when().get("/api/livros")
          .then()
             .statusCode(200)
             .body(notNullValue());
    }

    @Test
    public void testBuscarLivroPorId() {
        given()
          .when().get("/api/livros/1")
          .then()
             .statusCode(anyOf(is(200), is(404)));
    }
}

✅ Verificando o Resultado

Execute o projeto no terminal:

mvn quarkus:dev


Teste os endpoints no navegador ou Postman:

📘 Listar livros: http://localhost:8080/api/livros

🔎 Buscar livro por ID: http://localhost:8080/api/livros/1

Exemplo de retorno esperado:

[
  {
    "id": 1,
    "titulo": "O Guarani",
    "isbn": "978-85-01-001",
    "autorNome": "José de Alencar",
    "disponivel": true
  },
  {
    "id": 2,
    "titulo": "Dom Casmurro",
    "isbn": "978-85-01-002",
    "autorNome": "Machado de Assis",
    "disponivel": true
  }
]

🔍 Testando e Integrando

Com a API funcionando, agora qualquer sistema pode consumir os dados da biblioteca.
Por exemplo, um aplicativo mobile pode chamar GET /api/livros e exibir os livros disponíveis para empréstimo.

📚 Referências (Pesquisa Externa)

📘 Quarkus - RESTEasy Reactive JSON Guide

📗 Jakarta RESTful Web Services (JAX-RS)

📙 JSON Serialization with Jackson

🧩 Conclusão

Com este tutorial, você aprendeu a expor endpoints RESTful dentro de um projeto Quarkus baseado em JSF, aproveitando toda a camada de serviço já existente.
Essa melhoria:

Facilita integrações externas (mobile, dashboards, APIs).

Reduz o acoplamento entre o backend e o frontend.

Moderniza a arquitetura do sistema.

O projeto agora está pronto para evoluir para uma plataforma híbrida — com interface JSF e suporte REST completo.

✍️ Autor: Diego Oliveira
📅 Projeto: Trabalho Tópicos Especiais III - Biblioteca Digital (2025)