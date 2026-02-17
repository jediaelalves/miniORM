# miniORM - Mini ORM em Java com JDBC

![Java](https://img.shields.io/badge/Java-17+-blue)
![MySQL](https://img.shields.io/badge/MySQL-8+-orange)
![JDBC](https://img.shields.io/badge/JDBC-Driver-lightgrey)

Um **mini ORM em Java** que demonstra técnicas de persistência de dados usando **JDBC puro**, **annotations**, **reflection** e **generics**, sem frameworks externos.

> Objetivo: apresentar conceitos de programação avançada em Java de forma prática, com CRUD completo e menu interativo.

---

## 🔹 Funcionalidades


- ✅ Criação automática de tabelas a partir de classes anotadas (`@Table`, `@Id`, `@Column`)  
- ✅ Geração automática de **Foreign Keys**  
- ✅ CRUD genérico para qualquer entidade  
- ✅ Conversão automática entre tipos Java e SQL  
- ✅ Suporte a:  
  - `String`  
  - `Integer`  
  - `BigDecimal`  
  - `Boolean`  
  - `LocalDate`  
  - `LocalDateTime`  
  - `Enum`  
- ✅ Mapeamento automático de **ResultSet** usando Reflection  
- ✅ Detecção de dependências entre tabelas para criação ordenada (FKs)  
- ✅ Conversão automática de nomes de atributos para **snake_case** no banco  
- ✅ Gerenciamento seguro de conexões com **try-with-resources**  
- ✅ Menu interativo via terminal para testes rápidos

## 🔹 Estrutura do Projeto

```
   br.com.miniORM/
    ├─ app/            → Menu interativo (Main.java)
    ├─ annotations/    → @Table, @Id, @Column
    ├─ config/         → Inicialização automática do banco
    ├─ connection/     → ConnectionFactory
    ├─ model/          → Entidades (Pessoa, Categoria, Produto)
    ├─ orm/
    │   ├─ mapper/     → ResultSetMapper (reflection)
    │   └─ sql/        → SqlGenerator (SQL dinâmico)
    └─ repository/     → Repository<T> genérico
    resources/
    └─ application.properties
```

---

## 🔹 Entidades

### ✅ Pessoa

- nome  
- telefone  
- email  

---

### ✅ Categoria

- id  
- descricao  
- dataHora  



---

### ✅ Produto (com Foreign Key)

**Recursos importantes:**

- Relacionamento com **Categoria**  
- Suporte a **BigDecimal** para valores monetários e estoque  
- Produtos **pesáveis** com enum de unidade de medida  
- Datas de criação e validade  
- Suporte a enums para unidade de medida  

**Exemplo de relação gerada automaticamente no banco:**

```sql
FOREIGN KEY (categoria_id) REFERENCES categorias(id)
```
---

## 🔹 Criação automática de tabelas

O `DatabaseInitializer`:

- Escaneia o pacote de entidades  
- Detecta dependências entre tabelas (FKs)  
- Cria primeiro tabelas sem dependência  
- Gera constraints automaticamente  

> Isso evita erros clássicos de criação fora de ordem.

---

## 🔹 Geração dinâmica de SQL

O `SqlGenerator` cria automaticamente SQL para:

- INSERT  
- UPDATE  
- DELETE  
- FIND BY ID  
- FIND ALL  

> Tudo baseado em Reflection — sem necessidade de SQL fixo para cada entidade.

---

## 🔹 ResultSet Mapper

O `ResultSetMapper` converte resultados do banco diretamente em objetos Java, suportando:

- Enums → mapeamento automático para `ENUM(...)` no MySQL  
- LocalDate / LocalDateTime → conversão automática  
- Relacionamentos (FK) → instancia entidades relacionadas com ID  
- Conversão de tipos avançados: BigDecimal, Boolean, LocalDate, LocalDateTime  

> Funciona de forma similar a ORMs maiores como Hibernate, mas totalmente transparente.

---

## 🔹 Técnicas aplicadas

- **Annotations customizadas** → mapeamento de classe → tabela, campo → coluna  
- **Reflection avançada** → leitura de anotações, instanciamento dinâmico e acesso a campos privados  
- **Generics** → Repository<T> genérico para qualquer entidade  
- **JDBC puro** → conexão e execução de SQL  
- **Try-with-resources** → gerenciamento seguro de conexões  
- **Detecção de dependências** → criação de tabelas na ordem correta  
- **Snake case automático** → conversão de nomes Java → SQL  
- **Mapeamento de Enums** → criação de ENUMs no banco  
- **Relacionamentos (FK)** → geração automática e instanciamento de objetos relacionados  
- **Tipos avançados** → BigDecimal, LocalDate, LocalDateTime, Boolean  

---

## 🔹 Pontos fortes

- Leve e transparente  
- Controle total sobre SQL  
- Fácil de estender com novas entidades  
- Código modular e extensível  
- Excelente para estudar **como ORMs funcionam internamente**  

---

## 🔹 Limitações

Ainda não possui:

- Lazy Loading  
- Queries complexas  
- Paginação  
- Cache de entidades  
- Migrações de schema  
- Relacionamentos `OneToMany` automáticos  

> O foco é demonstrar **técnicas de Java** de forma educativa.

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
2 - Categorias
3 - Produtos
0 - Sair
```

---

## 🔹 Como rodar

1. Configure `application.properties` com seu MySQL.  
2. Compile e execute:

```bash
mvn clean compile exec:java -Dexec.mainClass="br.com.miniORM.app.Main"
```

3. Siga o menu interativo para testar CRUD e relacionamentos.

---

## 🔹 Conclusão

O **miniORM** é uma **demonstração prática** de técnicas avançadas de Java aplicadas à persistência de dados:

- JDBC  
- Reflection  
- Annotations  
- Generics  
- Conversão automática de tipos  
- CRUD seguro com conexão ao banco  
- Suporte a FK, Enums e datas  

> É uma base para aprendizado 


