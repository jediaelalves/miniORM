package br.com.miniORM.orm.sql;

import java.lang.reflect.Field;
import java.util.*;

import br.com.miniORM.annotations.*;

public class SqlGenerator {

    // ================= INSERT =================

    public static String insert(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        List<String> cols = new ArrayList<>();
        List<String> params = new ArrayList<>();

        for(Field f : clazz.getDeclaredFields()){

            if(f.isAnnotationPresent(Id.class))
                continue;

            cols.add(getColumnName(f)); // 🔥 CORREÇÃO
            params.add("?");
        }

        return "INSERT INTO " + table.name() +
                "(" + String.join(",", cols) + ")" +
                " VALUES (" + String.join(",", params) + ")";
    }

    // ================= FIND =================

    public static String findById(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        return "SELECT * FROM " + table.name() + " WHERE id=?";
    }

    public static String findAll(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        return "SELECT * FROM " + table.name();
    }

    // ================= DELETE =================

    public static String delete(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        return "DELETE FROM " + table.name() + " WHERE id=?";
    }

    // ================= UPDATE =================

    public static String update(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        List<String> sets = new ArrayList<>();

        for(Field f : clazz.getDeclaredFields()){

            if(f.isAnnotationPresent(Id.class))
                continue;

            sets.add(getColumnName(f) + "=?"); // 🔥 CORREÇÃO
        }

        return "UPDATE " + table.name() +
                " SET " + String.join(",", sets) +
                " WHERE id=?";
    }

    // ================= MÉTODO MAIS IMPORTANTE DO ORM =================

    private static String getColumnName(Field field){

        // Se tiver @Column
        if(field.isAnnotationPresent(Column.class)){

            Column col = field.getAnnotation(Column.class);

            if(!col.name().isBlank())
                return col.name();
        }

        // fallback -> snake_case
        return toSnake(field.getName());
    }

    private static String toSnake(String s){
        return s.replaceAll("([a-z])([A-Z])","$1_$2").toLowerCase();
    }
}
