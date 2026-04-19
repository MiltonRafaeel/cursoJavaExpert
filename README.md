# 🚀 Projeto Java Spring Expert

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=java&logoColor=white)](https://junit.org/junit5/)
[![Postman Docs](https://img.shields.io/badge/Postman-Documenta%C3%A7%C3%A3o-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://documenter.getpostman.com/view/51167813/2sBXqDtNzZ)

Este repositório reúne os projetos desenvolvidos durante o curso **Java Spring Professional**, com foco no desenvolvimento de aplicações back-end robustas com **Java** e **Spring Boot**, dando ênfase especial à **qualidade de software**, **testes automatizados**, **segurança** e **boas práticas de arquitetura**.

Entre os projetos presentes neste repositório, o principal é o **`dscommerce-mockmvc-ra`**, no qual foram aplicados testes de API, validações de comportamento da aplicação e práticas modernas de desenvolvimento back-end.

## 📚 Sobre o Repositório

Ao longo do curso, diversos projetos e desafios foram desenvolvidos para consolidar o aprendizado de forma prática. O conteúdo evolui desde a estruturação inicial de uma aplicação Spring Boot até cenários mais avançados, como:

- Testes unitários e de integração
- Testes de API (MockMvc e RestAssured)
- Cobertura de código com JaCoCo
- Segurança (Autenticação e Autorização com OAuth2/Spring Security)
- Implementação de casos de uso e regras de negócio

Este repositório representa essa trajetória de aprendizado, reunindo projetos complementares e estudos práticos realizados durante a formação de 120 horas.

## 🎯 Objetivo do Projeto

Construir aplicações back-end mais maduras e confiáveis, aplicando:

- Estruturação correta de projetos Spring Boot
- CRUD e organização em camadas
- Testes automatizados com JUnit e Mockito
- Validação de dados (Bean Validation)
- Segurança com controle de acesso e proteção de rotas
- Cobertura de código e identificação de trechos não testados

## 🧠 Conteúdos Aplicados

### 🔹 CRUD e Estruturação
- Organização em camadas (Controller, Service, Repository)
- Separação de responsabilidades e boas práticas de estruturação.

### 🔹 Testes Automatizados
- Testes unitários e uso de **Mockito/Spy**.
- Testes de integração de API com **MockMvc** e **RestAssured**.
- Validação de status HTTP, payloads e regras de negócio.

### 🔹 Segurança e Casos de Uso
- Controle de acesso com Spring Security.
- Implementação de fluxos reais como signup, finalização de pedidos e regras específicas de negócio.

### 🔹 Qualidade com JaCoCo
- Geração de relatórios de cobertura para garantir a confiabilidade da aplicação.

## 🧪 Projeto Principal

O principal projeto deste repositório é o **`dscommerce-mockmvc-ra`**, que concentra a aplicação dos principais conceitos de testes de integração, validação de endpoints e segurança em um nível avançado de maturidade técnica.

## 📝 Projetos Desenvolvidos no Repositório

| Projeto | Foco Principal |
|--------|-----------------|
| `dscommerce-mockmvc-ra` | **Projeto principal** com testes de API usando MockMvc |
| `desafio-dsmovie-jacoco` | Cobertura de código com JaCoCo e testes de Service |
| `dscommerce-restassured` | Testes de API com RestAssured |
| `desafio-validacao-seguranca` | Validação de dados e segurança |
| `desafio-movieflix-casos-de-uso` | Casos de uso e fluxos complexos de negócio |
| `desafio-empregados-auth` | Autenticação e controle de acesso |
| `Desafio-Empregados-tdd` | TDD aplicado a regras de negócio |
| `desafio-tdd-event-city` | Prática de TDD em cenário de API |
| `Backend/dscatalog` | Estudos e práticas iniciais com Spring Boot |
| `dscommerce-jacoco-aula-cap05` | Aplicação de cobertura de código no projeto base |
| `dsmovie-restassured` | Testes de API com RestAssured no projeto DSMovie |
| `spring-boot-3-4-3-noauth` | Estudos de rotas e segurança simplificada |
| `exemplomockspy` | Exemplos práticos com Mockito Spy |
| `aula-junit` | Fundamentos de testes com JUnit |
| `exer01-junit-vanilla` | Exercícios iniciais com JUnit puro |

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Framework:** Spring Boot (Web, JPA, Security, Validation)
- **Testes:** JUnit 5, Mockito, MockMvc, RestAssured, JaCoCo
- **Banco de Dados:** H2 (Testes) e PostgreSQL (Produção)
- **Ferramentas:** Maven, Postman, Git

## 📬 Documentação da API

A documentação da API está disponível publicamente no Postman:

[![Postman Docs](https://img.shields.io/badge/Acessar-Documenta%C3%A7%C3%A3o%20da%20API-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://documenter.getpostman.com/view/51167813/2sBXqDtNzZ)

## ▶️ Como Executar o Projeto Principal

```bash
# Clonar o repositório
git clone https://github.com/MiltonRafaeel/cursoJavaExpert.git

# Entrar na pasta do projeto principal
cd dscommerce-mockmvc-ra

# Executar os testes automatizados
./mvnw test

# Executar a aplicação
./mvnw spring-boot:run
