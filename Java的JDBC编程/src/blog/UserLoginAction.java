package blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: HHH.Y
 * Date: 2020-06-08
 */

// 负责用户登录的功能
public class UserLoginAction implements Action {
    @Override
    public void run() {
        System.out.println("开始用户登录...");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名称> " );
        String username = scanner.nextLine();
        System.out.print("请输入用户密码> " );
        String password = scanner.nextLine();

        try(Connection c = DBUTil.getConnection()) {
            String sql = "select id, nickname from user where username = ? and password = ?";
            try(PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try(ResultSet r = ps.executeQuery()) {
                    // 因为 username 是 unique 的
                    // 所以查询的结果, 要么是返回 1 行数据, 要么是返回 0 行数据
                    // 不能找到多个
                    if(r.next()) {
                        User user = new User();
                        int id = r.getInt(1);
                        String nickname = r.getString(2);
                        user.id = id;
                        user.nickName = nickname;
                        user.userName = username;
                        User.login(user);
                    } else {
                        System.out.println("用户名或密码出错, 请重新输入!!!");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("错误: " + e.getMessage());
        }
    }
}
