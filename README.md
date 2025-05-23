# 📅 Telegram Bot - Agenda e Notificações

Um bot inteligente para o Telegram, desenvolvido com Java e Spring Boot, que permite criar eventos, agendar lembretes e receber notificações diretamente no seu chat do Telegram. Ideal para quem quer se organizar de forma simples, rápida e integrada.

🚧 **Este projeto está em desenvolvimento ativo e passará por melhorias e refatorações ainda.**

---

## ✨ Funcionalidades

* ✅ Criar, editar e deletar eventos pessoais via Telegram.
* ⏰ Agendar notificações automáticas para serem enviadas antes do evento.
* 📬 Recebimento de alertas diretamente no chat do Telegram.
* 💬 Interface simples e intuitiva com comandos e botões interativos.
* 🔒 Armazenamento persistente com banco de dados relacional.

---

## 🛠️ Tecnologias Utilizadas

* **Java 17**
* **Spring Boot** (Framework principal)
* **Spring Data JPA** (Persistência de dados com MySQL ou H2)
* **Hibernate** (ORM)
* **Telegram Bot API** (Integração via webhook)
* **Maven** (Gerenciador de dependências e build)

---

## 🚀 Como Executar Localmente

### 1. Clone este repositório

```
git clone https://github.com/GuilhermeSaar/bot-telegram.git
```

### 2. Configure o banco de dados

No arquivo `application.yml` ou `application.properties`, insira suas credenciais do MySQL (ou configure H2 para testes locais).

### 3. Adicione o token do seu bot do Telegram

Também no arquivo de configuração, inclua o token fornecido pela BotFather e configure a URL pública para o webhook (caso necessário).

---

## 🤝 Contribuições

Este projeto é uma iniciativa pessoal com fins de aprendizagem. Sugestões, críticas construtivas e contribuições são muito bem-vindas!

---

## 🧠 Autor

Desenvolvido por **Guilherme** —
🔗 [GitHub](https://github.com/GuilhermeSaar)
