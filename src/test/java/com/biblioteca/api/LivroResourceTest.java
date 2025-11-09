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
