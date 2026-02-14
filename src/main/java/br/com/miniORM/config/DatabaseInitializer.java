package br.com.miniORM.config;

import br.com.miniORM.annotations.*;
import br.com.miniORM.connection.ConnectionFactory;
import br.com.miniORM.model.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DatabaseInitializer {

    private static final Map<Class<?>, String> SQL_TYPES = new HashMap<>();

    static {
        SQL_TYPES.put(String.class, "VARCHAR");
        SQL_TYPES.put(Integer.class, "INT");
        SQL_TYPES.put(LocalDateTime.class, "DATETIME");
    }

    public static void init() {

        try (Connection conn = ConnectionFactory.getConnection()) {

            create(conn, Pauta.class);
            create(conn, Pessoa.class);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar banco", e);
        }
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

        for (Field field : clazz.getDeclaredFields()) {

            String columnName = toSnakeCase(field.getName());

            // ---------- ID ----------
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

            // ---------- COLUMN ----------
            Column col = field.getAnnotation(Column.class);

            String sqlType = resolveSqlType(field, col);

            sql.append(columnName)
                    .append(" ")
                    .append(sqlType);

            boolean nullable = col == null || col.nullable();

            if (!nullable) {
                sql.append(" NOT NULL");
            }

            sql.append(",");
        }

        // remove última vírgula
        sql.deleteCharAt(sql.length() - 1);
        sql.append(");");

        try (Statement st = conn.createStatement()) {

            System.out.println("Executando SQL:");
            System.out.println(sql);

            st.execute(sql.toString());
        }
    }

    private static String resolveSqlType(Field field, Column col) {

        Class<?> type = field.getType();

        if (!SQL_TYPES.containsKey(type)) {
            throw new RuntimeException(
                    "Tipo não suportado: " + type.getSimpleName()
            );
        }

        String baseType = SQL_TYPES.get(type);

        // Apenas String tem tamanho
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
