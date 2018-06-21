package com.yida.framework.blog.utils.h2;

import com.yida.framework.blog.utils.common.GeneralUtil;

import java.sql.*;
import java.util.*;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-20 00:51
 * @Description H2数据库操作工具类
 */
public class H2DBUtil {
    //jdbc:h2:[<path>]<databaseName>
    //ALTER USER SA SET PASSWORD 'rioyxlgt'

    // 数据库连接地址
    public static String URL;
    // 用户名
    public static String USERNAME;
    // 密码
    public static String PASSWORD;
    // mysql的驱动类
    public static String DRIVER;

    //数据库数据存储目录
    public static String DB_PATH;

    private static ResourceBundle rb = ResourceBundle.getBundle("db-config");

    // 使用静态块加载驱动程序
    static {
        URL = rb.getString("jdbc.url");
        USERNAME = rb.getString("jdbc.username");
        PASSWORD = rb.getString("jdbc.password");
        DRIVER = rb.getString("jdbc.driver");
        DB_PATH = rb.getString("jdbc.dbpath");
        URL = URL.replace("{db_path}", DB_PATH);
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private H2DBUtil() {
    }

    // 定义一个获取数据库连接的方法
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("获取连接失败");
        }
        return conn;
    }

    /**
     * 执行查询
     *
     * @param sql 要执行的sql语句
     * @return List<Map<String, Object>>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static List<Map<String, Object>> executeQuery(Connection connection, String sql) throws SQLException {
        return executeQuery(connection, sql, null);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return List<Map<String, Object>>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static List<Map<String, Object>> executeQuery(Connection connection, String sql,
                                                         Object[] bindArgs) throws SQLException {
        return executeQuery(connection, sql, bindArgs, true);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return List<Map<String, Object>>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static List<Map<String, Object>> executeQuery(Connection connection, String sql, Object[] bindArgs,
                                                         boolean autoClose) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> datas = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null && bindArgs.length > 0) {
                /**设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            System.out.println(getExecSQL(sql, bindArgs));
            /**执行sql语句，获取结果集**/
            resultSet = preparedStatement.executeQuery();
            datas = getDatas(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
            if (autoClose) {
                closeConnection(connection);
            }
        }
        return datas;
    }

    /**
     * 执行查询
     *
     * @param sql 要执行的sql语句
     * @return List<Map<String, Object>>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        return executeQuery(sql, null);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return List<Map<String, Object>>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object[] bindArgs) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> datas = null;
        try {
            /**获取数据库连接池中的连接**/
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null && bindArgs.length > 0) {
                /**设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            System.out.println(getExecSQL(sql, bindArgs));
            /**执行sql语句，获取结果集**/
            resultSet = preparedStatement.executeQuery();
            datas = getDatas(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            colseResource(connection, preparedStatement, resultSet);
        }
        return datas;
    }

    /**
     * 执行查询
     *
     * @param sql 要执行的sql语句
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static <T> T queryColumn(Connection connection, String sql,
                                    String columnName, Class<T> clazz) throws SQLException {
        return queryColumn(connection, sql, null, columnName, clazz);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static <T> T queryColumn(Connection connection, String sql,
                                    Object[] bindArgs, String columnName, Class<T> clazz) throws SQLException {
        return queryColumn(connection, sql, bindArgs, columnName, clazz, true);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static <T> T queryColumn(Connection connection, String sql,
                                    Object[] bindArgs, String columnName, Class<T> clazz,
                                    boolean autoClose) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        T t = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null && bindArgs.length > 0) {
                /**设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            System.out.println(getExecSQL(sql, bindArgs));
            /**执行sql语句，获取结果集**/
            resultSet = preparedStatement.executeQuery();
            t = getData(resultSet, columnName, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            colseResource(autoClose, connection, preparedStatement, resultSet);
        }
        return t;
    }

    /**
     * 执行查询
     *
     * @param sql 要执行的sql语句
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static <T> T queryColumn(String sql, String columnName,
                                    Class<T> clazz) throws SQLException {
        return queryColumn(sql, null, columnName, clazz);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static <T> T queryColumn(String sql, Object[] bindArgs, String columnName,
                                    Class<T> clazz) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        T t = null;
        try {
            /**获取数据库连接池中的连接**/
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null && bindArgs.length > 0) {
                /**设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            System.out.println(getExecSQL(sql, bindArgs));
            /**执行sql语句，获取结果集**/
            resultSet = preparedStatement.executeQuery();
            t = getData(resultSet, columnName, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            colseResource(connection, preparedStatement, resultSet);
        }
        return t;
    }

    /**
     * 执行查询
     *
     * @param sql 要执行的sql语句
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static Map<String, Object> queryOne(Connection connection, String sql) throws SQLException {
        return queryOne(connection, sql, null, true);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static Map<String, Object> queryOne(Connection connection, String sql,
                                               Object[] bindArgs) throws SQLException {
        return queryOne(connection, sql, bindArgs, true);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static Map<String, Object> queryOne(Connection connection, String sql,
                                               Object[] bindArgs, boolean autoClose) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map<String, Object> data = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null && bindArgs.length > 0) {
                /**设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            System.out.println(getExecSQL(sql, bindArgs));
            /**执行sql语句，获取结果集**/
            resultSet = preparedStatement.executeQuery();
            data = getData(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            colseResource(autoClose, connection, preparedStatement, resultSet);
        }
        return data;
    }

    /**
     * 执行查询
     *
     * @param sql 要执行的sql语句
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static Map<String, Object> queryOne(String sql) throws SQLException {
        return queryOne(sql, null);
    }

    /**
     * 执行查询
     *
     * @param sql      要执行的sql语句
     * @param bindArgs 绑定的参数
     * @return Map<String, Object>结果集对象
     * @throws SQLException SQL执行异常
     */
    public static Map<String, Object> queryOne(String sql, Object[] bindArgs) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map<String, Object> data = null;
        try {
            /**获取数据库连接池中的连接**/
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null && bindArgs.length > 0) {
                /**设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            System.out.println(getExecSQL(sql, bindArgs));
            /**执行sql语句，获取结果集**/
            resultSet = preparedStatement.executeQuery();
            data = getData(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            colseResource(connection, preparedStatement, resultSet);
        }
        return data;
    }

    /**
     * 可以执行新增，修改，删除
     *
     * @param sql sql语句
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int executeUpdate(Connection connection, String sql) throws SQLException {
        return executeUpdate(connection, sql, null, true);
    }

    /**
     * 可以执行新增，修改，删除
     *
     * @param sql      sql语句
     * @param bindArgs 绑定参数
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int executeUpdate(Connection connection, String sql, Object[] bindArgs) throws SQLException {
        return executeUpdate(connection, sql, bindArgs, true);
    }

    /**
     * 可以执行新增，修改，删除
     *
     * @param sql      sql语句
     * @param bindArgs 绑定参数
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int executeUpdate(Connection connection, String sql, Object[] bindArgs, boolean autoClose) throws SQLException {
        /**影响的行数**/
        int affectRowCount = -1;
        PreparedStatement preparedStatement = null;
        try {
            /**执行SQL预编译**/
            preparedStatement = connection.prepareStatement(sql.toString());
            /**设置不自动提交，以便于在出现异常的时候数据库回滚**/
            connection.setAutoCommit(false);
            System.out.println(getExecSQL(sql, bindArgs));
            if (bindArgs != null && bindArgs.length > 0) {
                /**绑定参数设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            /**执行sql**/
            affectRowCount = preparedStatement.executeUpdate();
            connection.commit();
            String operate;
            if (sql.toUpperCase().indexOf("DELETE FROM") != -1) {
                operate = "删除";
            } else if (sql.toUpperCase().indexOf("INSERT INTO") != -1) {
                operate = "新增";
            } else {
                operate = "修改";
            }
            System.out.println("成功" + operate + "了" + affectRowCount + "行");
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            closeStatement(preparedStatement);
            if (autoClose) {
                closeConnection(connection);
            }
        }
        return affectRowCount;
    }

    /**
     * 可以执行新增，修改，删除
     *
     * @param sql sql语句
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int executeUpdate(String sql) throws SQLException {
        return executeUpdate(sql, null);
    }

    /**
     * 可以执行新增，修改，删除
     *
     * @param sql      sql语句
     * @param bindArgs 绑定参数
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int executeUpdate(String sql, Object[] bindArgs) throws SQLException {
        /**影响的行数**/
        int affectRowCount = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            /**从数据库连接池中获取数据库连接**/
            connection = getConnection();
            /**执行SQL预编译**/
            preparedStatement = connection.prepareStatement(sql.toString());
            /**设置不自动提交，以便于在出现异常的时候数据库回滚**/
            connection.setAutoCommit(false);
            System.out.println(getExecSQL(sql, bindArgs));
            if (bindArgs != null && bindArgs.length > 0) {
                /**绑定参数设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            /**执行sql**/
            affectRowCount = preparedStatement.executeUpdate();
            connection.commit();
            String operate;
            if (sql.toUpperCase().indexOf("DELETE FROM") != -1) {
                operate = "删除";
            } else if (sql.toUpperCase().indexOf("INSERT INTO") != -1) {
                operate = "新增";
            } else {
                operate = "修改";
            }
            System.out.println("成功" + operate + "了" + affectRowCount + "行");
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            closeStatement(preparedStatement);
            closeConnection(connection);
        }
        return affectRowCount;
    }

    /**
     * 释放资源
     *
     * @param conn
     * @param st
     * @param rs
     */
    public static void colseResource(Connection conn, Statement st, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(st);
        closeConnection(conn);
    }

    /**
     * 释放资源
     *
     * @param conn
     * @param st
     * @param rs
     */
    public static void colseResource(boolean autoClose, Connection conn, Statement st, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(st);
        if (autoClose) {
            closeConnection(conn);
        }
    }

    /**
     * 释放资源
     *
     * @param conn
     * @param st
     */
    public static void colseResource(boolean autoClose, Connection conn, Statement st) {
        closeStatement(st);
        if (autoClose) {
            closeConnection(conn);
        }
    }

    /**
     * 释放连接 Connection
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //等待垃圾回收
        conn = null;
    }

    /**
     * 释放语句执行者 Statement
     *
     * @param st
     */
    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //等待垃圾回收
        st = null;
    }

    /**
     * 释放结果集 ResultSet
     *
     * @param rs
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //等待垃圾回收
        rs = null;
    }

    /**
     * 将结果集对象封装成List<Map<String, Object>> 对象
     *
     * @param resultSet 结果多想
     * @return 结果的封装
     * @throws SQLException
     */
    private static List<Map<String, Object>> getDatas(ResultSet resultSet) throws SQLException {
        if (null == resultSet) {
            System.out.println("没有查到任何数据");
            return null;
        }
        List<Map<String, Object>> datas = new ArrayList<>();
        /**获取结果集的数据结构对象**/
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                rowMap.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            datas.add(rowMap);
        }
        System.out.println("成功查询到了" + datas.size() + "行数据");
        for (int i = 0; i < datas.size(); i++) {
            Map<String, Object> map = datas.get(i);
            System.out.println("第" + (i + 1) + "行：" + map);
        }
        return datas;
    }

    /**
     * 将结果集对象封装成Map<String, Object>对象
     *
     * @param resultSet 结果多想
     * @return 结果的封装
     * @throws SQLException
     */
    private static Map<String, Object> getData(ResultSet resultSet) throws SQLException {
        if (null == resultSet) {
            System.out.println("没有查到任何数据");
            return null;
        }
        Map<String, Object> rowMap = null;
        /**获取结果集的数据结构对象**/
        ResultSetMetaData metaData = resultSet.getMetaData();
        if (null == metaData || metaData.getColumnCount() <= 0) {
            System.out.println("没有查到任何数据");
            return null;
        }
        if (resultSet.next()) {
            rowMap = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                rowMap.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            System.out.println("成功查询到了1行数据");
            System.out.println("查询到该行数据为：" + rowMap);
        }
        return rowMap;
    }

    /**
     * 将结果集对象封装成T
     *
     * @param resultSet 结果多想
     * @return 结果的封装
     * @throws SQLException
     */
    private static <T> T getData(ResultSet resultSet, String columnName, Class<T> clazz) throws SQLException {
        if (null == resultSet) {
            System.out.println("没有查到任何数据");
            return null;
        }
        T t = null;
        /**获取结果集的数据结构对象**/
        ResultSetMetaData metaData = resultSet.getMetaData();
        if (null == metaData || metaData.getColumnCount() <= 0) {
            System.out.println("没有查到任何数据");
            return null;
        }
        if (resultSet.next()) {
            String columnLabel = null;
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                columnLabel = metaData.getColumnLabel(i);
                if (columnName.equals(columnLabel)) {
                    t = GeneralUtil.cast(resultSet.getObject(i), clazz);
                    break;
                }
            }
            System.out.println("成功查询到了1行数据");
            System.out.println("查询到该行数据为：" + t);
        }
        return t;
    }

    /**
     * 生成实际执行的SQL语句
     *
     * @param sql      SQL statement
     * @param bindArgs Binding parameters
     * @return Replace? SQL statement executed after the
     */
    private static String getExecSQL(String sql, Object[] bindArgs) {
        StringBuilder sb = new StringBuilder(sql);
        if (bindArgs != null && bindArgs.length > 0) {
            int index = 0;
            for (int i = 0; i < bindArgs.length; i++) {
                index = sb.indexOf("?", index);
                sb.replace(index, index + 1, String.valueOf(bindArgs[i]));
            }
        }
        return sb.toString();
    }
}
