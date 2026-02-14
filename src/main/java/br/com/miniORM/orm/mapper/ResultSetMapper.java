package br.com.miniORM.orm.mapper;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;

public class ResultSetMapper {

    public static <T> T map(ResultSet rs, Class<T> clazz) throws Exception {

        T obj = clazz.getDeclaredConstructor().newInstance();

        for(Field field : clazz.getDeclaredFields()){

            field.setAccessible(true);

            Object value = rs.getObject(toSnake(field.getName()));

            if(value instanceof Timestamp ts &&
               field.getType() == LocalDateTime.class){

                field.set(obj, ts.toLocalDateTime());

            }else{
                field.set(obj, value);
            }
        }

        return obj;
    }

    private static String toSnake(String s){
        return s.replaceAll("([a-z])([A-Z])","$1_$2").toLowerCase();
    }
}