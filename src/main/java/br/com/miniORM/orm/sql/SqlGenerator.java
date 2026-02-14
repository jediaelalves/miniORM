package br.com.miniORM.orm.sql;

import java.lang.reflect.Field;
import java.util.*;

import br.com.miniORM.annotations.*;

public class SqlGenerator {

    public static String insert(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        List<String> cols = new ArrayList<>();
        List<String> params = new ArrayList<>();

        for(Field f : clazz.getDeclaredFields()){

            if(f.isAnnotationPresent(Id.class))
                continue;

            cols.add(toSnake(f.getName()));
            params.add("?");
        }

        return "INSERT INTO " + table.name() +
                "(" + String.join(",", cols) + ")" +
                " VALUES (" + String.join(",", params) + ")";
    }

    public static String findById(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        return "SELECT * FROM " + table.name() + " WHERE id=?";
    }

    public static String findAll(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        return "SELECT * FROM " + table.name();
    }

    public static String delete(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        return "DELETE FROM " + table.name() + " WHERE id=?";
    }

    public static String update(Class<?> clazz){

        Table table = clazz.getAnnotation(Table.class);

        List<String> sets = new ArrayList<>();

        for(Field f : clazz.getDeclaredFields()){

            if(f.isAnnotationPresent(Id.class))
                continue;

            sets.add(toSnake(f.getName()) + "=?");
        }

        return "UPDATE " + table.name() +
                " SET " + String.join(",", sets) +
                " WHERE id=?";
    }

    private static String toSnake(String s){
        return s.replaceAll("([a-z])([A-Z])","$1_$2").toLowerCase();
    }
}