import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * Description: 使用 Statement 进行 sql 执行时, 如果 sql 中有参数, 需要动态获取, Statement比较差
 * 使用 prepareStatement 进行代替
 *
 * 使用 Statement 有可能会产生 sql 注入 -- sql 漏洞的一种
 * SQL 注入:
 * 1. 由于我们在拼接 sql 时, 有漏洞/有 BUG
 * 2. 恶意用户使用一些特殊的输入导致执行一些不是我们预期之内的 sql 
 * prepareStatement 可以有效的防止 sql 注入
 * User: HHH.Y
 * Date: 2020-06-07
 */
public class Use_PrepareStatement {
    public static void main(String[] args) throws SQLException {
        // 初始化一次, 多次使用
        initDataSource();

        Scanner scanner = new Scanner(System.in);

        // 使用 statement 的写法
        try(Connection c = getConnection()) {
            // 查询以下 SQL: select * from exam_result where english > 某个值 and chinese > 某个值
            // 这个值需要用户输入
            System.out.print("请输入英语获奖分数> ");
            int ei = scanner.nextInt();
            System.out.println("请输入语文获奖分数> ");
            int ci = scanner.nextInt();
            try(Statement s = c.createStatement()) {
                // 自己使用 String 拼接 sql, 很容易出问题
                String sql = "select * from exam_result where english > " + ei + " and chinese > " + ci;
                System.out.println(sql);
                try(ResultSet resultSet = s.executeQuery(sql)) {
                    while (resultSet.next()) {
                        String id = resultSet.getString("id");
                        String name = resultSet.getString("name");
                        String english = resultSet.getString("english");
                        String chinese = resultSet.getString("chinese");
                        String math = resultSet.getString("math");
                        System.out.println(id + "," + name + " " + english + "," + chinese + "," + math);
                    }
                }
            }

        }

        // 使用 prepareStatement 的方式
        try(Connection c = getConnection()) {
            // 查询以下 SQL: select * from exam_result where english > 某个值 and chinese > 某个值
            // 这个值需要用户输入
            System.out.print("请输入英语获奖分数> ");
            int ei = scanner.nextInt();
            System.out.println("请输入语文获奖分数> ");
            int ci = scanner.nextInt();
            // 提前把 sql 写好, 使用 ? 作为占位符
            String sql = "select * from exam_result where english > ? and chinese > ?";
            try(PreparedStatement preparedStatement = c.prepareStatement(sql)) {
                // 使用具体的值替代占位符
                preparedStatement.setInt(1, ei);
                preparedStatement.setInt(2, ci);

                try(ResultSet resultSet = preparedStatement.getResultSet()) {
                    while (resultSet.next()) {
                        String id = resultSet.getString("id");
                        String name = resultSet.getString("name");
                        String english = resultSet.getString("english");
                        String chinese = resultSet.getString("chinese");
                        String math = resultSet.getString("math");
                        System.out.println(id + "," + name + " " + english + "," + chinese + "," + math);
                    }
                }
            }
        }

    }

    private static DataSource dataSource = null;

    private static void initDataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource(); // 实现了 DataSource 接口的类
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("huyuelover1017");
        mysqlDataSource.setDatabaseName("guyueyuebeautiful");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setCharacterEncoding("utf8");

        dataSource = mysqlDataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
