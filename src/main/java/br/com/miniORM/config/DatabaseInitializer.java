package br.com.miniORM.config;

import br.com.miniORM.annotations.*;
import br.com.miniORM.connection.ConnectionFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DatabaseInitializer {

    private static final Map<Class<?>, String> SQL_TYPES = new HashMap<>();

    static {
        SQL_TYPES.put(String.class, "VARCHAR");
        SQL_TYPES.put(Integer.class, "INT");
        SQL_TYPES.put(BigDecimal.class, "DECIMAL(15,2)");
        SQL_TYPES.put(Boolean.class, "BOOLEAN");
        SQL_TYPES.put(LocalDate.class, "DATE");
        SQL_TYPES.put(LocalDateTime.class, "DATETIME");
    }

    public static void init() {
        try (Connection conn = ConnectionFactory.getConnection()) {
            criarTabelas(conn, "br.com.miniORM.model");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar banco", e);
        }
    }

    private static void criarTabelas(Connection conn, String pacote) throws Exception {

        List<Class<?>> classes = buscarClassesComTabela(pacote);

        // 🔥 MUITO IMPORTANTE — cria primeiro tabelas sem dependência
        classes.sort(Comparator.comparingInt(DatabaseInitializer::contarDependencias));

        for (Class<?> clazz : classes) {
            System.out.println("Criando tabela para: " + clazz.getSimpleName());
            create(conn, clazz);
        }
    }

    // Conta quantas entidades existem dentro da classe (para ordenar criação)
    private static int contarDependencias(Class<?> clazz) {
        int count = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().isAnnotationPresent(Table.class)) {
                count++;
            }
        }
        return count;
    }

    private static List<Class<?>> buscarClassesComTabela(String pacote) throws Exception {

        List<Class<?>> lista = new ArrayList<>();
        String caminho = pacote.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(caminho);

        if (resource == null) {
            throw new IllegalArgumentException("Pacote não encontrado: " + pacote);
        }

        File diretorio = new File(resource.getFile());

        for (File arquivo : Objects.requireNonNull(diretorio.listFiles())) {

            if (arquivo.getName().endsWith(".class")) {

                String nomeClasse = pacote + "." + arquivo.getName().replace(".class", "");
                Class<?> clazz = Class.forName(nomeClasse);

                if (clazz.isAnnotationPresent(Table.class)) {
                    lista.add(clazz);
                }
            }
        }

        return lista;
    }

    private static void create(Connection conn, Class<?> clazz) throws Exception {

        Table table = clazz.getAnnotation(Table.class);

        if (table == null) {
            throw new RuntimeException(
                    "Classe " + clazz.getSimpleName() + " não possui @Table"
            );
        }

        StringBuilder sql = new StringBuilder(
                "CREATE TABLE IF NOT EXISTS " + table.name() + " ("
        );

        List<String> foreignKeys = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {

            String columnName = toSnakeCase(field.getName());

            // ID
            if (field.isAnnotationPresent(Id.class)) {

                Id id = field.getAnnotation(Id.class);

                sql.append(columnName)
                        .append(" INT PRIMARY KEY");

                if (id.autoIncrement()) {
                    sql.append(" AUTO_INCREMENT");
                }

                sql.append(",");
                continue;
            }

            Class<?> fieldType = field.getType();

            // 🔥 RELACIONAMENTO — detecta outra entidade
            if (fieldType.isAnnotationPresent(Table.class)) {

                Table refTable = fieldType.getAnnotation(Table.class);

                String fkColumn = columnName + "_id";

                sql.append(fkColumn).append(" INT,");

                foreignKeys.add(
                        "FOREIGN KEY (" + fkColumn + ") REFERENCES "
                                + refTable.name() + "(id)"
                );

                continue;
            }

            Column col = field.getAnnotation(Column.class);
            String sqlType = resolveSqlType(field, col);

            sql.append(columnName).append(" ").append(sqlType);

            boolean nullable = col == null || col.nullable();
            if (!nullable) {
                sql.append(" NOT NULL");
            }

            sql.append(",");
        }

        // remove última vírgula
        sql.deleteCharAt(sql.length() - 1);

        // adiciona FKs
        for (String fk : foreignKeys) {
            sql.append(", ").append(fk);
        }

        sql.append(");");

        try (Statement st = conn.createStatement()) {
            System.out.println("Executando SQL:");
            System.out.println(sql);
            st.execute(sql.toString());
        }
    }

    private static String resolveSqlType(Field field, Column col) {

        Class<?> type = field.getType();

        // ENUM
        if (type.isEnum()) {

            Object[] constants = type.getEnumConstants();

            StringBuilder sb = new StringBuilder("ENUM(");

            for (Object c : constants) {
                sb.append("'").append(c.toString()).append("',");
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");

            return sb.toString();
        }

        if (!SQL_TYPES.containsKey(type)) {
            throw new RuntimeException("Tipo não suportado: " + type.getSimpleName());
        }

        String baseType = SQL_TYPES.get(type);

        if (type == String.class) {

            int length = (col != null) ? col.length() : 255;
            return baseType + "(" + length + ")";
        }

        return baseType;
    }

    private static String toSnakeCase(String value) {
        return value
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }
}
