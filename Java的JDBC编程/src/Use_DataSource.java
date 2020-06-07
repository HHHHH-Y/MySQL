import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description: JDBC 标准在升级的过程中, 引入了 DataSource 方式, 创建 connection 对象
 * User: HHH.Y
 * Date: 2020-06-07
 */
public class Use_DataSource {
    public static void getConnectionUseDriverManger() throws ClassNotFoundException, SQLException {
        // 1. 注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 2. 获取连接
        String url = "jdbc:mysql://127.0.0.1:3306/guyueyuebeautiful?useSSL=false&characterEncoding=utf8";
        String user = "root";
        String password = "huyuelover1017";
        try(Connection connection = DriverManager.getConnection(url, user, password)) {
            // 目的就是获得 connection 对象
        }
    }

    // 这个是新版 JDBC 标准提供的写法
    // 写法比 url 的方式更加明确, 不容易出现拼写错误
    // 支持连接池的方式, 所以可能效率更高
    public static void getConnectionUseDataSource() throws SQLException {
        DataSource dataSource; // 接口

        MysqlDataSource mysqlDataSource = new MysqlDataSource(); // 实现了 DataSource 接口的类
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("huyuelover1017");
        mysqlDataSource.setDatabaseName("guyueyuebeautiful");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setCharacterEncoding("utf8");

        // DataSource 也支持 url 的方式制定连接参数
        //mysqlDataSource.setURL("jdbc:mysql://127.0.0.1:3306/guyueyuebeautiful?useSSL=false&characterEncoding=utf8");

        dataSource = mysqlDataSource; // 向上转型

        try(Connection connection = dataSource.getConnection())  {
            // 目的就是获得 connection 对象
        }
    }
}
