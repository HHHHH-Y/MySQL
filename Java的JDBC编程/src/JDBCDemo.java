import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * JDBC 代码的基本套路:
 * 1. 注册驱动
 * 2. 获取连接
 *    循环:
 *         查询操作 或者 非查询操作
 * -1. 关闭连接
 *
 * 查询操作:
 * 1. 获取 Statement 对象
 * 2. 执行 executeQuery 并得到 resultSet 对象
 * 3. 控制行 + 列 获取需要的结果
 * -2. 关闭 resultSet 对象
 * -1. 关闭 Statement 对象
 *
 * 非查询操作:
 * 1. 获取 Statement 对象
 * 2. 执行 executeUpdate 并得到影响行数
 * -1. 关闭 Statement 对象
 *
 *
 * User: HHH.Y
 * Date: 2020-06-06
 */
public class JDBCDemo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // 1. 注册驱动 -- (挑选 mysql.Driver 作为乙方)
        Class.forName("com.mysql.jdbc.Driver");

        /**
         * 完整的目标是执行 select * from student_0605;
         */

        // 2. 建立数据库连接

        // 写明 MySQL 服务端所在地
        // 以后写项目, 只需要修改默认数据库名称即可
        String defaultDatabaseName = "guyueyuebeautiful"; // 指定默认库, 相当于是cmd中的 use, 设定默认库
        // 填写你自己的 MySQl 密码
        String password = "huyuelover1017";

        // 下面这些, 基本上不变
        String user = "root";
        // MySQL数据连接的URL参数格式如下:
        // jdbc:mysql://服务器地址:端口/数据库名?参数名=参数值
        String url = "jdbc:mysql://127.0.0.1:3306/" + defaultDatabaseName + "?useSSL=false&characterEncoding=utf8";

        // 创建数据库连接
        Connection connection = DriverManager.getConnection(url, user, password);
        // 打印 connection 对象, 验证是否连接成功
        System.out.println(connection);

        // 要真正的执行 sql 语言, 并且获取数据库返回的结果
        // 1. 关于有结果返回的 sql (show databases / show tables / select ...)
        // queryDemo(connection);

        // 2. 关于没有结果返回的 sql (create database / create table / insert / update / delete)
        updateDemo(connection);

        // -1. 关闭刚才的连接
        connection.close();
    }

    private static void updateDemo(Connection connection) throws SQLException {
        // 1. 获取 Statement 对象
        Statement statement = connection.createStatement();
        String sql = "insert into student_0605 (sn, name, sex) values ('20200605', '小陈', '女')";
        int affectRows = statement.executeUpdate(sql); // 适用于在不带有结果的场景下
        System.out.printf("Query Ok, %d row affected%n", affectRows);
        statement.close();
    }

    private static void queryDemo(Connection connection) throws SQLException {
        // (1). 创建 Statement 对象 (创建sql 语句对象)
        // Statement 代表的是 "语句" 的抽象对象
        Statement statement = connection.createStatement();

        String sql = "select * from student_0605"; // sql语句没有要求必须分号结尾
        // (2). 执行 sql 语句并将执行的结果放入结果集 resultSet 中
        //      1. statement.executeQuery(...) 适用于在带有结果的场景下
        //      2. ResultSet 代表的是结果集的抽象对象
        ResultSet resultSet = statement.executeQuery(sql);

        int count = 0;
        System.out.println("+----+----------+--------+-----+");
        System.out.println("| id | sn       | name   | sex |");
        System.out.println("+----+----------+--------+-----+");

        /**
         * 1. 如何在结果集中, 控制行: 调用 resultSet.next();
         *    1). 必须调用一次 resultSet.next() 才是第一行
         *    2). resultSet.next() 返回 true, 代表还有行可以读取, 返回 false, 代表结果已经全部取完
         * 2. 如何在结果集中, 控制列: 调用 resultSet.getXXX() 系列
         *    1). 首先, resultSet 必须还有行可以被读取
         *    2). 需要不同类型, 可以调用不同的 getXXX 系列
         *    3). 列的下标(columnIndex) 从 1 开始
         */
        // resultSet.next 表示的是结果集中是否还有下一个有效行
        while (resultSet.next()) {
            // 根据想要的类型, 有很多种选择, 如: getString(...), getInt(...). getLong(...)
            // columnIndex 是从 1 开头的, 表示的是列数
            String id = resultSet.getString(1);
            String sn = resultSet.getString(2);
            String name = resultSet.getString(3);
            String sex = resultSet.getString(4);

            System.out.format("| %2s | %4s | %9s | %3s | %n", id, sn, name, sex);
            count++;
        }
        System.out.println("+----+----------+--------+-----+");
        System.out.format("%d rows int set", count);


        // -3. 关闭 resultSet 对象
        resultSet.close();

        // -2. 关闭 statement 对象
        statement.close();
    }
}
