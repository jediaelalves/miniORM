package br.com.miniORM.repository;

import br.com.miniORM.annotations.Id;
import br.com.miniORM.connection.ConnectionFactory;
import br.com.miniORM.orm.mapper.ResultSetMapper;
import br.com.miniORM.orm.sql.SqlGenerator;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class Repository<T> {

    private final Class<T> clazz;

    public Repository(Class<T> clazz){
        this.clazz = clazz;
    }

    public void insert(T entity) throws Exception{

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(SqlGenerator.insert(clazz))){

            int index = 1;

            for(Field field : clazz.getDeclaredFields()){

                if(field.isAnnotationPresent(Id.class))
                    continue;

                field.setAccessible(true);
                Object value = field.get(entity);

                if(value instanceof LocalDateTime ldt){
                    ps.setTimestamp(index++, Timestamp.valueOf(ldt));
                }else{
                    ps.setObject(index++, value);
                }
            }

            ps.executeUpdate();
        }
    }

    public Optional<T> findById(int id) throws Exception{

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(SqlGenerator.findById(clazz))){

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return Optional.of(
                        ResultSetMapper.map(rs, clazz));

            return Optional.empty();
        }
    }

    public List<T> findAll() throws Exception{

        List<T> list = new ArrayList<>();

        try(Connection conn = ConnectionFactory.getConnection();
            Statement st = conn.createStatement()){

            ResultSet rs =
                    st.executeQuery(SqlGenerator.findAll(clazz));

            while(rs.next())
                list.add(ResultSetMapper.map(rs, clazz));
        }

        return list;
    }

    public void delete(int id) throws Exception{

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(SqlGenerator.delete(clazz))){

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void update(T entity) throws Exception{

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(SqlGenerator.update(clazz))){

            int index = 1;
            Object idValue = null;

            for(Field field : clazz.getDeclaredFields()){

                field.setAccessible(true);
                Object value = field.get(entity);

                if(field.isAnnotationPresent(Id.class)){
                    idValue = value;
                    continue;
                }

                if(value instanceof LocalDateTime ldt){
                    ps.setTimestamp(index++, Timestamp.valueOf(ldt));
                }else{
                    ps.setObject(index++, value);
                }
            }

            ps.setObject(index, idValue);

            ps.executeUpdate();
        }
    }
}