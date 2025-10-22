# 📚 Biblioteca Digital

Um sistema web desenvolvido para **gerenciar uma biblioteca digital**, feito com **Quarkus**, **Jakarta Faces (JSF)** e **PostgreSQL**.  
O projeto aplica os conceitos de **arquitetura em camadas**, **JPA com relacionamentos**, **injeção de dependência (CDI)** e **transações automáticas** de forma prática e didática.

---

## 🧱 Estrutura do Sistema

O sistema é dividido em três entidades principais:

- **Autor** → um autor pode ter vários livros.  
- **Livro** → pertence a um autor e pode ter vários empréstimos.  
- **Empréstimo** → registra os usuários que pegaram livros emprestados.

A interface exibe tudo em um painel moderno, responsivo e fácil de navegar, desenvolvido com **Jakarta Faces**.

---

## ⚙️ Tecnologias Utilizadas

| Camada | Tecnologia |
|--------|-------------|
| Backend | Quarkus + Hibernate ORM (JPA) |
| Frontend | Jakarta Faces (JSF) |
| Banco de Dados | PostgreSQL |
| Arquitetura | MVC (Entity → Repository → Service → Bean) |

---

## 💡 Funcionalidades

✅ Painel com estatísticas (total de livros, autores e empréstimos ativos)  
✅ Listagem dinâmica de autores, livros e empréstimos  
✅ Controle de disponibilidade de livros  
✅ Interface estilizada com HTML + CSS responsivo  
✅ Banco inicial com dados de exemplo (autores, livros e empréstimos)  

---

## 🚀 Como Executar

# 2. Entrar na pasta
cd biblioteca-digital

# 3. Rodar o projeto
./mvnw compile quarkus:dev
