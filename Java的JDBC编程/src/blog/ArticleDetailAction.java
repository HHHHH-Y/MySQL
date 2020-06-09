package blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: HHH.Y
 * Date: 2020-06-09
 */

// 查看文章指定内容
public class ArticleDetailAction implements Action {
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要查看文章的id > ");
        String id = scanner.nextLine();
        String authorID = null;
        String title = null;
        String content = null;
        String publishAt = null;
        int likeCount = 0;

        // 1. 查找文章信息
        try(Connection c = DBUTil.getConnection()) {
            String queryArticleSql = "select author_id, title, content, publish_at from article where id = ?";
            try(PreparedStatement s = c.prepareStatement(queryArticleSql)) {
                s.setString(1, id);
                try(ResultSet rs = s.executeQuery()) {
                    if(!rs.next()) {
                        System.out.println("没有这篇文章!!!");
                        return;
                    }
                    authorID = rs.getString("author_id");
                    title = rs.getString("title");
                    content = rs.getString("content");
                    publishAt = rs.getString("publish_at");
                }
            }

            // 2. 查询点赞数量
            String likeCountSql = "select count(*) from like_relation where article_id = ?";
            try(PreparedStatement s = c.prepareStatement(likeCountSql)) {
                s.setString(1, id);
                try(ResultSet rs = s.executeQuery()) {
                    rs.next(); // 至少有一行数据
                    likeCount = rs.getInt(1);
                }
            }

            // 3. 查询评论信息
            List<String[]> commentList = new ArrayList<>();
            String queryCommentSql = "select user_id, content, publish_at from comments where article_id = ? order by publish_at desc";
            try(PreparedStatement s = c.prepareStatement(queryCommentSql)) {
                s.setString(1, id);
                try(ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        String[] comment = new String[3];
                        comment[0] = rs.getString("user_id");
                        comment[1] = rs.getString("content");
                        comment[2] = rs.getString("publish_at");

                        commentList.add(comment);
                    }
                }
            }

            // 4. 根据用户 id , 查询用户昵称
            // 用户 id 来自 (作者id + 评论id)
            Set<String> userIdSet = new HashSet<>();
            userIdSet.add(authorID);
            for (String[] comment:commentList) {
                userIdSet.add(comment[0]);
            }
            StringBuilder queryNicknameSql = new StringBuilder("select id, nickname from user where id in (");
            for (int i = 1; i < userIdSet.size(); i++) {
                queryNicknameSql.append("?, ");
            }
            queryNicknameSql.append("?)");

            Map<String, String> userIdToNicknameMap = new HashMap<>();
            try(PreparedStatement s = c.prepareStatement(queryNicknameSql.toString())) {
                int i = 1;
                for (String userId: userIdSet) {
                    s.setString(i++, userId);
                }
                try(ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        userIdToNicknameMap.put(rs.getString("id"), rs.getString("nickname"));
                    }
                }
            }

            // 5. 根据所有的信息进行显示
            System.out.println(title);
            System.out.println(userIdToNicknameMap.get(authorID));
            System.out.println(publishAt);
            System.out.println("点赞人数: " + likeCount);
            System.out.println("==================================");
            System.out.println(content);
            System.out.println("==================================");
            for (String[] comment: commentList) {
                System.out.println(userIdToNicknameMap.get(comment[0]) + "|" + comment[1] + "|" + comment[2]);
            }
        } catch (SQLException e) {
            System.out.println("错误: " + e.getMessage());
        }
    }
}
