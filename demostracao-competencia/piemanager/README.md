# PIE Manager API

O sistema é uma plataforma acadêmica completa para gerenciamento de projetos e apresentações universitárias, com autenticação de usuários e controle de acesso por perfil. A aplicação possui tela de login com validação de credenciais, persistência de sessão em localStorage e menu dinâmico conforme o tipo de usuário (aluno, professor, coordenador ou avaliador externo). O sistema também garante segurança básica ao impedir acesso às telas sem autenticação e disponibiliza funcionalidades de logout e navegação restrita por permissões.

A plataforma contempla diversos módulos administrativos com operações de cadastro, edição, listagem e exclusão para cursos, períodos letivos, turmas, alunos, professores, coordenadores, avaliadores externos, grupos de projeto, projetos, estandes e horários de apresentação. Todos os módulos possuem validações específicas de negócio, como unicidade de dados, relacionamentos obrigatórios, restrições de exclusão quando houver vínculos ativos e mecanismos de busca e confirmação antes de remover registros. As informações são persistidas no localStorage, mantendo os dados disponíveis localmente no navegador.

Além da gestão acadêmica, o sistema organiza todo o fluxo de projetos estudantis, permitindo formar grupos entre 2 e 6 alunos, associar orientadores, cadastrar projetos únicos por grupo, registrar avaliações com notas e comentários, além de controlar estandes e horários de apresentação sem conflitos. O objetivo central é oferecer uma solução integrada para administração de feiras, bancas e projetos acadêmicos, garantindo integridade dos dados, regras acadêmicas consistentes e uma experiência organizada para todos os perfis de usuários envolvidos.

## Padroes estabelecidos no projeto

### Arquitetura
- REST para exposicao da API.
- MVC para organizacao das camadas.
- DTOs para trafego de dados entre controller e service.
- Service layer para regras de negocio.
- Repository layer com Spring Data JPA.
- Tratamento global de excecoes com resposta consistente.
- CORS configurado para permitir acesso de qualquer dominio.
- Documentacao da API com Swagger/OpenAPI.
- Autenticacao e autorizacao com Spring Security e JWT.
- Banco de dados local com SQLite.

## Como executar

### Pre-requisitos
- Java 17 instalado
- Permissao para executar o Maven Wrapper do projeto

### Rodar a aplicacao
No Windows:

```powershell
./mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

A API iniciara, por padrao, na porta `8080`.

## 3. Acessar Swagger
Abra no navegador:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`

## Padrao de dominio
- O package base do projeto e `com.unisales.piemanager`.
- A pasta deve ser organizada conforme o padrao MVC e deve seguir a seguinte estrutura:
```
User/ (domain)
    User.java (Model)
    UserController.java (Controller)
    UserService.java (Service)
    UserRepository.java (Repository)
    dto/ (DTOs)
```
### 1. Criar o usuario admin inicial

```bash
curl -X POST http://localhost:8080/api/public/bootstrap
```

## Tecnologias utilizadas
- Java 17
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT com JJWT
- Bean Validation
- Springdoc OpenAPI / Swagger UI
- SQLite JDBC
- Hibernate Community Dialects
- Maven Wrapper

## Configuracao da aplicacao

As configuracoes principais estao em [src/main/resources/application.properties](src/main/resources/application.properties):

- Banco SQLite: `jdbc:sqlite:piemanager.db`
- Hibernate DDL: `update`
- Swagger UI: `/swagger-ui.html`
- JWT expiration: `3600000` ms

## Observacoes
- O arquivo do banco SQLite sera criado ou atualizado na raiz do projeto como `piemanager.db`.
- O package valido utilizado no codigo e `com.unisales.piemanager`.
- O arquivo [HELP.md](HELP.md) contem as anotacoes geradas inicialmente pelo Spring Boot, mas o guia principal do projeto passa a ser este `README.md`.
