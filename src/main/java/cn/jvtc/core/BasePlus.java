package cn.jvtc.core;

import cn.jvtc.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 雷族
 */
public class BasePlus<T> extends Base<T> {
    public BasePlus(Class<T> clazz) {
        super(clazz);
    }

    public int executeUpdate(String sql, Object... args) {
        try (Connection connection = JDBCUtil.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                System.out.print("sql:" + sql);
                //赋参数值
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i + 1, args[i]);
                    System.out.print("       " + args[i]);
                }
                System.out.println();
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return 0;
        }
    }

    public List<T> executeQuery(String sql, Object... args) {
        List<T> list = new ArrayList<>();
        try (Connection connection = JDBCUtil.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                System.out.print("sql:" + sql);
                //赋参数值
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i + 1, args[i]);
                    System.out.print("       " + args[i]);
                }
                System.out.println();
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
}
