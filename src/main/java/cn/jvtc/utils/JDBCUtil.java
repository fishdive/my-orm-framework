package cn.jvtc.utils;

import java.sql.*;

/**
 * The type Jdbc util.
 *
 * @author 雷族
 */
public class JDBCUtil {

    private static final PropertiesUtil properties = new PropertiesUtil("db.properties");

    /**
     * JDBC驱动名，常量
     */
    private static final String JDBC_DRIVER = properties.getPropValue("jdbc.driver");

    /**
     * 数据库 URL，常量
     */
    private static final String DB_URL = properties.getPropValue("jdbc.url");


    // 数据库的用户名与密码，需要根据自己的设置
    /**
     * 数据库用户名，常量
     */
    private static final String USERNAME = properties.getPropValue("jdbc.username");

    /**
     * 数据库密码，常量
     */
    private static final String PASSWORD = properties.getPropValue("jdbc.password");

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     *
     * @return connection connection
     * @throws SQLException the sql exception
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    /**
     * Gets 数据表主键
     *
     * @param tableName  the 表名
     * @param connection the 数据库连接对象
     * @return the pk
     */
    public static String getPK(String tableName, Connection connection) throws SQLException {
        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet rs = dmd.getPrimaryKeys(null, null, tableName);
        String columnLabel = "column_name";
        if (rs.next()) {
            String PKName = rs.getString(columnLabel);
            rs.close();
            return PKName;
        } else {
            throw new SQLException("该表无主键。。。。");
        }
    }
}
