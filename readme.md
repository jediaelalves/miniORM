# miniORM - Mini ORM em Java com JDBC

![Java](https://img.shields.io/badge/Java-17+-blue)
![MySQL](https://img.shields.io/badge/MySQL-8+-orange)
![JDBC](https://img.shields.io/badge/JDBC-Driver-lightgrey)

Um **mini ORM em Java** que demonstra técnicas de persistência de dados usando **JDBC puro**, **annotations**, **reflection** e **generics**, sem frameworks externos.

> Objetivo: apresentar conceitos de programação avançada em Java de forma prática, com CRUD completo e menu interativo.

---

## 🔹 Funcionalidades

- Criação automática de tabelas no MySQL a partir de classes anotadas (`@Table`, `@Id`, `@Column`)
- CRUD básico para as entidades:
  - **Pessoa** (nome, telefone, email)
  - **Pauta** (descrição, data/hora)
- Menu interativo no terminal para:
  - Listar registros
  - Inserir novos registros
  - Atualizar registros existentes
  - Excluir registros
- Conversão automática de tipos Java (`String`, `Integer`, `LocalDateTime`) para SQL
- Gerenciamento seguro de conexões com **try-with-resources**

---

## 🔹 Estrutura do Projeto

```
br.com.miniORM/
├─ app/           → Menu interativo (Main.java)
├─ config/        → Inicialização do banco (DatabaseInitializer.java)
├─ connection/    → Gerenciamento de conexões (ConnectionFactory.java)
├─ model/         → Classes de entidades (Pessoa.java, Pauta.java)
├─ repository/    → CRUD genérico (Repository.java)
└─ annotations/   → Anotações (@Table, @Id, @Column)
resources/
└─ application.properties  → Configuração do banco
```

---

## 🔹 Configuração do Banco

Arquivo: `src/main/resources/application.properties`

```properties
db.url=jdbc:mysql://localhost:3306/miniorm?serverTimezone=UTC
db.user=root
db.password=senha
```

> O banco será criado automaticamente se não existir.  

---

## 🔹 Demonstração do Menu Interativo

```
===== MINI ORM =====
1 - Pessoas
2 - Pautas
0 - Sair
```

### Menu Pessoas
```
--- PESSOAS ---
1 - Listar
2 - Incluir
3 - Alterar
4 - Excluir
```

### Menu Pautas
```
--- PAUTAS ---
1 - Listar
2 - Incluir
3 - Excluir
```

### Exemplo de execução:

```
===== MINI ORM =====
1 - Pessoas
2 - Pautas
0 - Sair
> 1

--- PESSOAS ---
1 - Listar
2 - Incluir
3 - Alterar
4 - Excluir
> 2
Nome: João
Telefone: 119999999
Email: joao@email.com
Pessoa cadastrada!

--- PESSOAS ---
1 - Listar
2 - Incluir
3 - Alterar
4 - Excluir
> 1
Pessoa{id=1, nome='João', telefone='119999999', email='joao@email.com'}
```

---

## 🔹 Técnicas aplicadas

- **Annotations** → mapear classes e atributos para tabelas e colunas
- **Reflection** → ler as anotações e gerar SQL dinamicamente
- **Generics** → repositório genérico `Repository<T>`
- **JDBC** → comunicação direta com MySQL
- **Try-with-resources** → conexões seguras sem vazamento
- **Estrutura modular** → pacotes organizados por responsabilidade
- **Menu interativo** → demonstração prática do CRUD

---

## 🔹 Pontos fortes

- Leve e transparente  
- Controle total da SQL  
- Flexível para estudar novas entidades  
- Código modular e extensível  
- Permite compreender na prática o funcionamento de um mini ORM  

---

## 🔹 Limitações

- Relacionamentos entre tabelas (`OneToMany`, `ManyToOne`) não suportados  
- Cache de entidades e otimização de queries não implementados  
- Métodos como `findById` e consultas dinâmicas ainda não existem  
- Atualização automática de schema ou migrações não suportadas  

> O foco é **demonstrar técnicas de Java**, não entregar um ORM completo de produção.

---

## 🔹 Como rodar

1. Configure `application.properties` com seu MySQL.
2. Compile e execute:

```bash
mvn clean compile exec:java -Dexec.mainClass="br.com.miniORM.app.Main"
```

3. Siga o menu interativo para testar **CRUD**.

---

## 🔹 Conclusão

Este miniORM é uma **demonstração educativa** das técnicas de Java aplicadas à persistência de dados:  

- JDBC  
- Reflection  
- Annotations  
- Generics  
- CRUD seguro com conexão ao banco

> É uma base sólida para aprendizado e expansão futura.


