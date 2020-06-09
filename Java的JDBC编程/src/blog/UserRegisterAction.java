package blog;

import java.sql.*;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * Description: 用户注册
 * User: HHH.Y
 * Date: 2020-06-08
 */

// 完整实现用户注册功能
public class UserRegisterAction implements Action{

    @Override
    public void run() {
        // 1. 提示用户输入需要的的信息, 并且使用 JDBC 执行 MySQL
        System.out.println("开始用户注册...");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名称> ");
        String userName = scanner.nextLine();
        System.out.print("请输入用户昵称> ");
        String nickName = scanner.nextLine();
        System.out.print("请输入用户密码> ");
        String password = scanner.nextLine();
        String sql = "insert into user (userName, nickName, password) values (?, ?, ?)";
        try(Connection connection = DBUTil.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // RETURN_GENERATED_KEYS 表示返回自动生成的 Keys
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, nickName);
                preparedStatement.setString(3, password);

                preparedStatement.executeUpdate();

                int id;
                try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    resultSet.next();
                    id = resultSet.getInt(1);
                }

                System.out.println("注册成功, 欢迎您的加入: " + nickName);
                // 是否应该让刚刚注册的这个用户自动登录成功呢? 还是让该用户重新登录呢?
                // 两种方法都可以接受, 我们选择让其自动登录成功
                User user = new User();
                user.id = id;
                user.nickName = nickName;
                user.userName = userName;
                User.login(user);

            }
        } catch (SQLException e) {
            System.out.println("错误: " + e.getMessage());
        }
    }
}
