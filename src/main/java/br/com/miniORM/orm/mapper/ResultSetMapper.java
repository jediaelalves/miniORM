package br.com.miniORM.orm.mapper;

import br.com.miniORM.annotations.Column;
import br.com.miniORM.annotations.Id;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ResultSetMapper {

    public static <T> T map(ResultSet rs, Class<T> clazz) throws Exception {

        T entity = clazz.getDeclaredConstructor().newInstance();

        for(Field field : clazz.getDeclaredFields()){

            field.setAccessible(true);

            String columnName = getColumnName(field);
            Object value = rs.getObject(columnName);

            if(value == null)
                continue;

            Class<?> type = field.getType();

            // 🔥 ENUM
            if(type.isEnum()){
                Object enumValue =
                        Enum.valueOf((Class<Enum>) type, value.toString());

                field.set(entity, enumValue);
                continue;
            }

            // 🔥 LocalDateTime
            if(type.equals(LocalDateTime.class)){
                field.set(entity, ((Timestamp)value).toLocalDateTime());
                continue;
            }

            // 🔥 LocalDate
            if(type.equals(LocalDate.class)){
                field.set(entity, rs.getDate(columnName).toLocalDate());
                continue;
            }

            // 🔥 RELACIONAMENTO (FK)
            if(isEntity(type)){

                Object related = type.getDeclaredConstructor().newInstance();

                Field idField = getIdField(type);
                idField.setAccessible(true);

                idField.set(related, value);

                field.set(entity, related);

                continue;
            }

            field.set(entity, value);
        }

        return entity;
    }

    private static boolean isEntity(Class<?> type){
        return type.isAnnotationPresent(br.com.miniORM.annotations.Table.class);
    }

    private static Field getIdField(Class<?> clazz){

        for(Field f : clazz.getDeclaredFields()){
            if(f.isAnnotationPresent(Id.class))
                return f;
        }

        throw new RuntimeException("Entidade sem @Id: " + clazz.getSimpleName());
    }

    private static String getColumnName(Field field){

        if(field.isAnnotationPresent(Column.class)){

            Column col = field.getAnnotation(Column.class);

            if(!col.name().isBlank())
                return col.name();
        }

        return toSnake(field.getName());
    }

    private static String toSnake(String s){
        return s.replaceAll("([a-z])([A-Z])","$1_$2").toLowerCase();
    }
}
