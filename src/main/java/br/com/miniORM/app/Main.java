package br.com.miniORM.app;

import br.com.miniORM.config.DatabaseInitializer;
import br.com.miniORM.model.*;
import br.com.miniORM.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        DatabaseInitializer.init();

        Repository<Pessoa> pessoaRepo = new Repository<>(Pessoa.class);
        Repository<Pauta> pautaRepo = new Repository<>(Pauta.class);

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("""
                    
                    ===== MINI ORM =====
                    1 - Pessoas
                    2 - Pautas
                    0 - Sair
                    """);

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {

                case 1 -> menuPessoa(scanner, pessoaRepo);
                case 2 -> menuPauta(scanner, pautaRepo);
                case 0 -> {
                    System.out.println("Encerrando...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // ================= PESSOA =================

    private static void menuPessoa(Scanner scanner,
                                  Repository<Pessoa> repo) throws Exception {

        System.out.println("""
                
                --- PESSOAS ---
                1 - Listar
                2 - Incluir
                3 - Alterar
                4 - Excluir
                """);

        int op = Integer.parseInt(scanner.nextLine());

        switch (op) {

            case 1 -> repo.findAll()
                    .forEach(System.out::println);

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

    // ================= PAUTA =================

    private static void menuPauta(Scanner scanner,
                                 Repository<Pauta> repo) throws Exception {

        System.out.println("""
                
                --- PAUTAS ---
                1 - Listar
                2 - Incluir
                3 - Excluir
                """);

        int op = Integer.parseInt(scanner.nextLine());

        switch (op) {

            case 1 -> repo.findAll()
                    .forEach(System.out::println);

            case 2 -> {

                System.out.print("Título: ");
                String titulo = scanner.nextLine();

                repo.insert(new Pauta(
                        titulo,
                        LocalDateTime.now()
                ));

                System.out.println("Pauta criada!");
            }

            case 3 -> {

                repo.findAll().forEach(System.out::println);

                System.out.print("ID da pauta: ");
                int id = Integer.parseInt(scanner.nextLine()); 
                		

                repo.delete(id);

                System.out.println("Pauta removida!");
            }
        }
    }
}
