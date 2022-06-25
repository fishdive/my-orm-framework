package cn.jvtc.core;

import cn.jvtc.utils.JDBCUtil;
import cn.jvtc.utils.PojoUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 雷族
 */
public class Base<T> {
    private Class<T> clazz;

    public Base(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> selectAll(Object object) {
        try {
            //获取生成的查询语句
            AnnotationParser<Object> annotationParser = new AnnotationParser<>(object);
            String sql = annotationParser.getSelectSql();
            return this.executeQuery(sql);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | IllegalAccessException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    public T selectOne(Object object) {
        try {
            //获取生成的查询语句
            AnnotationParser<Object> annotationParser = new AnnotationParser<>(object);
            String sql = annotationParser.getSelectOneSql();
            return this.executeQuery(sql).get(0);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | IllegalAccessException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    public int delete(Object object) {
        //获取删除语句
        AnnotationParser<Object> annotationParser = null;
        try {
            annotationParser = new AnnotationParser<>(object);
            String sql = annotationParser.getDeleteSql();
            return executeUpdate(sql);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int update(Object object) {
        //获取修改语句
        AnnotationParser<Object> annotationParser = null;
        try {
            annotationParser = new AnnotationParser<>(object);
            String sql = annotationParser.getUpdateSql();
            return executeUpdate(sql);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int insert(Object object) {
        //获取添加语句
        AnnotationParser<Object> annotationParser = null;
        try {
            annotationParser = new AnnotationParser<>(object);
            String sql = annotationParser.getInsertSql();
            return executeUpdate(sql);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int executeUpdate(String sql) {
        try (Connection connection = JDBCUtil.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return 0;
        }
    }

    public List<T> executeQuery(String sql) {
        List<T> list = new ArrayList<>();
        try (Connection connection = JDBCUtil.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        T t = this.getPojo(resultSet);
                        list.add(t);
                    }
                    return list;
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    public T getPojo(ResultSet resultSet) {
        T t = null;
        try {
            t = this.clazz.getConstructor().newInstance();
            //获取该类自己声明的字段,不包含继承自父类的字段
            for (Field field : clazz.getDeclaredFields()) {
                //通过列名获取结果
                Object value = resultSet.getString(PojoUtil.getColumnName(field));
                //开启操作权限
                field.setAccessible(true);
                //赋值
                if (field.getType() == Integer.class) {
                    field.set(t, Integer.parseInt(value.toString()));
                } else {
                    field.set(t, value);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
        }
        return t;
    }
}
