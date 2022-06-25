package cn.jvtc.core;

import cn.jvtc.utils.JDBCUtil;
import cn.jvtc.utils.PojoUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Annotation core.
 *
 * @author 雷族
 */
public class AnnotationParser<T> {

    /**
     * 实体类
     */
    private T obj;
    /**
     * 表名
     */
    private String tableName;

    /**
     * 表字段
     */
    private Map<String, Object> columnMap = new HashMap<String, Object>();

    /**
     * 主键名
     */
    private String pk;

    public AnnotationParser(T t) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, SQLException {
        this.obj = t;
        //获取表名
        this.tableName = PojoUtil.getTableName(t);
        //查询数据库主键字段名称
        try (Connection connection = JDBCUtil.getConnection()) {
            this.pk = JDBCUtil.getPK(this.tableName, connection);
        }
        //获取实体类映射所有字段名和字段值
        for (Field field : t.getClass().getDeclaredFields()) {
            //获取表字段名
            String columnName = PojoUtil.getColumnName(field);
            //获取属性get方法返回值
            Object columnValue = PojoUtil.getMethodValue(obj, field);
            if (columnValue != null) {
                //判断是否为字符串
                if (columnValue instanceof String) {
                    this.columnMap.put(columnName, "'" + columnValue + "'");
                } else {
                    this.columnMap.put(columnName, columnValue);
                }
            }
        }
    }

    public String getSelectOneSql() throws SQLException {
        StringBuffer sql = new StringBuffer();
        Object pkValue = this.columnMap.get(this.pk);
        if (pkValue == null) {
            throw new SQLException("实体类没有与数据库表主键字段相映射属性！");
        } else {
            sql.append("select * from `" + tableName + "` where `" + this.pk + "` = " + pkValue);
        }
        System.out.println("sql:" + sql);
        return sql.toString();
    }

    public String getSelectOneSql(Object keyValue) throws SQLException {
        StringBuffer sql = new StringBuffer();
        if (keyValue == null) {
            throw new SQLException("主键值不能为空！");
        }
        if (keyValue instanceof String) {
            sql.append("select * from `" + tableName + "` where `" + this.pk + "` = '" + keyValue + "'");
        } else {
            sql.append("select * from `" + tableName + "` where `" + this.pk + "` = " + keyValue);
        }
        System.out.println("sql:" + sql);
        return sql.toString();
    }

    /**
     * 通过注解来组装查询条件，生成查询语句
     *
     * @return select sql
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException     the no such method exception
     * @throws IllegalAccessException    the illegal access exception
     */
    public String getSelectSql() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        StringBuffer sql = new StringBuffer();
        boolean flag = false;
        sql.append("select * from `" + this.tableName + "` where ");
        //判断有属性值才加入查询条件
        for (String key : this.columnMap.keySet()) {
            //主键不加入条件
            if (!pk.equals(key)) {
                Object value = this.columnMap.get(key);
                if (value != null) {
                    flag = true;
                    sql.append(" and `" + key + "` = " + value);
                }
            }

        }
        if (flag) {
            //有查询条件，处理字符，去掉第一个and
            sql.delete(sql.indexOf("and"), sql.indexOf("and") + "and".length());
        } else {
            //没有条件去掉where，查询全部
            sql.delete(sql.indexOf("where"), sql.indexOf("where") + "where".length());

        }
        System.out.println("sql:" + sql);

        return sql.toString();

    }

    /**
     * 通过注解来组装删除条件，生成删除语句
     *
     * @return the delete sql
     * @throws SQLException              the sql exception
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException     the no such method exception
     * @throws IllegalAccessException    the illegal access exception
     */
    public String getDeleteSql() throws SQLException {
        StringBuilder sql = new StringBuilder();
        Object pkValue = this.columnMap.get(this.pk);
        if (pkValue == null) {
            throw new SQLException("实体类没有与数据库表主键字段相映射属性！");
        } else {
            sql.append("delete  from `" + tableName + "` where `" + this.pk + "` = " + pkValue);
        }
        System.out.println("sql:" + sql);

        return sql.toString();
    }

    public String getDeleteSql(Object keyValue) throws SQLException {
        StringBuilder sql = new StringBuilder();
        if (keyValue == null) {
            throw new SQLException("主键值不能为空！");
        }
        if (keyValue instanceof String) {
            sql.append("delete from `" + tableName + "` where `" + this.pk + "` = '" + keyValue + "'");
        } else {
            sql.append("delete  from `" + tableName + "` where `" + this.pk + "` = " + keyValue);
        }
        System.out.println("sql:" + sql);

        return sql.toString();
    }

    /**
     * 通过注解来组装修改条件，生成修改语句
     *
     * @return the update sql
     * @throws SQLException              the sql exception
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException     the no such method exception
     * @throws IllegalAccessException    the illegal access exception
     */
    public String getUpdateSql() throws SQLException {
        StringBuffer sql = new StringBuffer();
        String where = null;

        sql.append("update `" + this.tableName + "` set");
        Object pkValue = this.columnMap.get(this.pk);
        if (pkValue == null) {
            throw new SQLException("实体类没有与数据库表主键字段相映射属性！");
        } else {
            where = " where `" + this.pk + "` = " + pkValue;
        }
        for (String key : this.columnMap.keySet()) {
            if (!key.equals(this.pk)) {
                Object value = this.columnMap.get(key);
                if (value != null) {
                    sql.append(" `" + key + "` =" + value + ",");
                }

            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(where);
        System.out.println("sql:" + sql);

        return sql.toString();
    }

    /**
     * 通过注解来组装增加条件，生成增加语句
     *
     * @return the insert sql
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException     the no such method exception
     * @throws IllegalAccessException    the illegal access exception
     */
    public String getInsertSql() {
        StringBuffer sql = new StringBuffer();
        StringBuffer values = new StringBuffer("VALUES (");
        sql.append("INSERT INTO `" + this.tableName + "` (");
        for (String key : this.columnMap.keySet()) {
            Object value = this.columnMap.get(key);
            //判断有值才加入sql
            if (value != null) {
                sql.append(" `" + key + "` ,");
                values.append(value + ",");
            }
        }
        //删除column中多余,
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        //删除values中多余,
        values.deleteCharAt(values.length() - 1);
        values.append(")");
        sql.append(values);
        System.out.println("sql:" + sql);

        return sql.toString();
    }
}
