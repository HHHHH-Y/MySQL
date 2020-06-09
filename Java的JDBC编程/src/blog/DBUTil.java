package blog;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description: 通过 DBUTil 获取 Connection 连接
 * User: HHH.Y
 * Date: 2020-06-08
 */

// Utilize 工具
public class DBUTil {
    // 静态属性, 整个代码运行过程中, 只有一份(整个进程运行过程中)
    private static DataSource dataSource = null;
    static {
        initDataSource();
    }
    public static void initDataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("huyuelover1017");
        mysqlDataSource.setDatabaseName("guyueyue_blog");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setCharacterEncoding("utf8");

        dataSource = mysqlDataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
