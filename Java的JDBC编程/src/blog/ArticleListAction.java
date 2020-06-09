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

// 查看文章列表, 按照发表时间倒序展示
public class ArticleListAction implements Action {
    @Override
    public void run() {
        List<String[]> articleList = new ArrayList<>();
        try(Connection c = DBUTil.getConnection()) {
            String sql = "select id, title, author_id, publish_at from article order by publish_at desc";
            try(PreparedStatement s = c.prepareStatement(sql)) {
                try(ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        String[] article = new String[4];
                        String id = rs.getString("id");
                        String title = rs.getString("title");
                        String authorID = rs.getString("author_id");
                        String publishAt = rs.getString("publish_at");
                        article[0] = id;
                        article[1] = authorID;
                        article[2] = title;
                        article[3] = publishAt;

                        articleList.add(article);
                    }
                }
            }

            // TODO: 打印作者的昵称而不是id信息
            // 需要根据作者 id, 再次去 user 表中查询出 用户的昵称信息

            // 提取出所有需要查询的作者的id
            // 利用 Set 中不会存在重复元素的特性
            Set<String> authorIDSet = new HashSet<>();
            for (String[] article:articleList) {
                String id = article[1];
                authorIDSet.add(id);
            }

            // 使用提取出的作者id, 拼接出一个根据 id, 查询作者昵称的 SQL
            // select id, nickname from user where id in (...)
            StringBuilder querySql = new StringBuilder("select id, nickname from user where id in (");
            for (int i = 1; i < authorIDSet.size(); i++) {
                querySql.append("?, ");
            }
            querySql.append("?)");

            Map<String, String> userIdToNickNameMap = new HashMap<>();
            try(PreparedStatement s = c.prepareStatement(querySql.toString())) {
                // 替换占位符?
               int i = 1;
                for (String id:authorIDSet) {
                    s.setString(i++, id);
                }

                try(ResultSet r = s.executeQuery()) {
                    while (r.next()) {
                        String id = r.getString("id");
                        String nickname = r.getString("nickname");
                        userIdToNickNameMap.put(id, nickname);
                    }
                }
            }

            System.out.printf("ID |   标题                       |    作者     |   发表时间%n");
            for (String[] article:articleList) {
                String id = article[0];
                String authorId = article[1];
                String nickname = userIdToNickNameMap.get(authorId);
                String title = article[2];
                String publishAt = article[3];
                System.out.printf("%-4s | %-20s | %-10s | %s%n", id, title, nickname, publishAt);
            }
        } catch (SQLException e) {
            System.out.println("错误: " + e.getMessage());
        }
    }
}
