package blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: HHH.Y
 * Date: 2020-06-09
 */

// 用户发表文章
public class ArticlePublishAction implements Action {
    @Override
    public void run() {
        // 1. 判断用户是否已登录
        if(!User.isLogined()) {
            System.out.println("需要先登录, 才可以进行该操作!");
            return;
        }

        // 和注册用户基本一致
        // 获取用户输入的信息(标题, 正文)
        // 根据当前登录的用户, 拿到作者的id
        // 通过调用API, 获取当前时间
        System.out.println("发表文章中...");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入文章标题 > ");
        String title = scanner.nextLine();
        System.out.print("请输入文章内容 > ");
        String content = scanner.nextLine();
        int authorID = User.getCurrentUser().id; // 作者 id
        Date publishAt = new Date(); // new 完的对象, 本来就是当前时间

        // publishAt 现在是 Date 对象, 我们把 Date 对象 format 成 String 格式
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String publishAtStr = format.format(publishAt);

        try(Connection c = DBUTil.getConnection()) {
            String sql = "insert into article (author_id, title, publish_at, content) values (?, ?, ?, ?)";
            try(PreparedStatement s = c.prepareStatement(sql)) {
                s.setInt(1, authorID);
                s.setString(2, title);
                s.setString(3, publishAtStr);
                s.setString(4, content);

                s.executeUpdate();

                System.out.println("《" + title + "》， 文章发表成功！");
            }
        } catch (SQLException e) {
            System.out.println("错误: " + e.getMessage());
        }
    }
}
