package br.com.miniORM.app;

import br.com.miniORM.config.DatabaseInitializer;
import br.com.miniORM.model.*;
import br.com.miniORM.repository.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        DatabaseInitializer.init();

        Repository<Pessoa> pessoaRepo = new Repository<>(Pessoa.class);
        Repository<Categoria> categoriaRepo = new Repository<>(Categoria.class);
        Repository<Produto> produtoRepo = new Repository<>(Produto.class);

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("""
                    
                    ===== MINI ORM =====
                    1 - Pessoas
                    2 - Categoria
                    3 - Produtos
                    0 - Sair
                    """);

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {

                case 1 -> menuPessoa(scanner, pessoaRepo);
                case 2 -> menuCategoria(scanner, categoriaRepo);
                case 3 -> menuProduto(scanner, produtoRepo);
                case 0 -> {
                    System.out.println("sistema finalizado");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // ================= PESSOA =================
    private static void menuPessoa(Scanner scanner, Repository<Pessoa> repo) throws Exception {
        System.out.println("""
                
                --- PESSOAS ---
                1 - Listar
                2 - Incluir
                3 - Alterar
                4 - Excluir
                """);

        int op = Integer.parseInt(scanner.nextLine());

        switch (op) {

            case 1 -> repo.findAll().forEach(System.out::println);

            case 2 -> {
                System.out.print("Nome: ");
                String nome = scanner.nextLine();

                System.out.print("Telefone: ");
                String telefone = scanner.nextLine();

                System.out.print("Email: ");
                String email = scanner.nextLine();

                repo.insert(new Pessoa(nome, telefone, email));
                System.out.println("Pessoa cadastrada!");
            }

            case 3 -> {
                List<Pessoa> pessoas = repo.findAll();
                pessoas.forEach(System.out::println);

                System.out.print("ID da pessoa: ");
                int id = Integer.parseInt(scanner.nextLine());

                Pessoa pessoa = pessoas.stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElse(null);

                if (pessoa == null) {
                    System.out.println("Pessoa não encontrada!");
                    return;
                }

                System.out.print("Novo nome: ");
                pessoa.setNome(scanner.nextLine());

                repo.update(pessoa);
                System.out.println("Pessoa atualizada!");
            }

            case 4 -> {
                repo.findAll().forEach(System.out::println);

                System.out.print("ID para excluir: ");
                int id = Integer.parseInt(scanner.nextLine());

                repo.delete(id);
                System.out.println("Pessoa removida!");
            }
        }
    }

    // ================= CATEGORIA =================
    private static void menuCategoria(Scanner scanner, Repository<Categoria> repo) throws Exception {
        System.out.println("""
                
                --- CATEGORIAS ---
                1 - Listar
                2 - Incluir
                3 - Excluir
                """);

        int op = Integer.parseInt(scanner.nextLine());

        switch (op) {

            case 1 -> repo.findAll().forEach(System.out::println);

            case 2 -> {
                System.out.print("Título: ");
                String titulo = scanner.nextLine();

                repo.insert(new Categoria(titulo, LocalDateTime.now()));
                System.out.println("Categoria criada!");
            }

            case 3 -> {
                repo.findAll().forEach(System.out::println);

                System.out.print("ID da Catogira: ");
                int id = Integer.parseInt(scanner.nextLine());

                repo.delete(id);
                System.out.println("Categoria removida!");
            }
        }
    }

    // ================= PRODUTO =================
    private static void menuProduto(Scanner scanner, Repository<Produto> repo) throws Exception {
        System.out.println("""
                
                --- PRODUTOS ---
                1 - Listar
                2 - Incluir
                3 - Alterar
                4 - Excluir
                5 - Preço com desconto
                """);

        int op = Integer.parseInt(scanner.nextLine());

        switch (op) {

            case 1 -> repo.findAll().forEach(System.out::println);

            case 2 -> {
                System.out.print("Nome: ");
                String nome = scanner.nextLine();

                System.out.print("Preço: ");
                BigDecimal preco = new BigDecimal(scanner.nextLine());

                System.out.print("Estoque: ");
                BigDecimal estoque = new BigDecimal(scanner.nextLine());

                System.out.print("Ativo (true/false): ");
                boolean ativo = Boolean.parseBoolean(scanner.nextLine());

                System.out.print("Validade (yyyy-mm-dd): ");
                LocalDate validade = LocalDate.parse(scanner.nextLine());

                System.out.println("Categoria: 1-ELETRONICOS 2-ALIMENTOS 3-ROUPAS 4-MOVEIS");
                Repository<Categoria> repoCategorias = new Repository<>(Categoria.class);
                repoCategorias.findAll().forEach(System.out::println);
                
                
                int cat = Integer.parseInt(scanner.nextLine());
                Categoria categoria = repoCategorias.findById(cat).get();
            //    Produto.Categoria categoria;
            //    switch (cat) {
            //    case 1 -> categoria = Produto.Categoria.BEBIDAS;
            //    case 2 -> categoria = Produto.Categoria.CONFEITARIA;
            //    case 3 -> categoria = Produto.Categoria.MERCEARIA;
            //    case 4 -> categoria = Produto.Categoria.FRIOS;
            //    default -> {
            //         throw new IllegalArgumentException("Categoria inválida: " + cat);          
             //        }
             //   }
            

                Produto produto = new Produto();
                
                produto.setNome(nome);
                produto.setPreco(preco);
                produto.setEstoque(estoque);
                produto.setAtivo(ativo);
                produto.setValidade(validade);
                produto.setCategoria(categoria);

                

                repo.insert(produto);
                System.out.println("Produto cadastrado!");
            }

            case 3 -> {
                List<Produto> produtos = repo.findAll();
                produtos.forEach(System.out::println);

                System.out.print("ID do produto: ");
                int id = Integer.parseInt(scanner.nextLine());

                Produto produto = produtos.stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElse(null);

                if (produto == null) {
                    System.out.println("Produto não encontrado!");
                    return;
                }

                System.out.print("Novo nome: ");
                produto.setNome(scanner.nextLine());

                System.out.print("Novo preço: ");
                produto.setPreco(new BigDecimal(scanner.nextLine()));

                repo.update(produto);
                System.out.println("Produto atualizado!");
            }

            case 4 -> {
                repo.findAll().forEach(System.out::println);

                System.out.print("ID para excluir: ");
                int id = Integer.parseInt(scanner.nextLine());

                repo.delete(id);
                System.out.println("Produto removido!");
            }

            case 5 -> {
                List<Produto> produtos = repo.findAll();
                produtos.forEach(System.out::println);

                System.out.print("ID do produto para calcular desconto: ");
                int id = Integer.parseInt(scanner.nextLine());

                Produto produto = produtos.stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElse(null);

                if (produto == null) {
                    System.out.println("Produto não encontrado!");
                    return;
                }

                System.out.print("Percentual de desconto: ");
                double perc = Double.parseDouble(scanner.nextLine());

                BigDecimal precoComDesconto = produto.calcularPrecoComDesconto(perc);
                System.out.println("Preço com desconto: " + precoComDesconto);
            }
        }
    }
}
