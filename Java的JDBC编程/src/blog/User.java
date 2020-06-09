package blog;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: HHH.Y
 * Date: 2020-06-08
 */

// 保存当前登录用户的信息
public class User {
    int id;
    String userName;
    String nickName;
    // 当前登录用户信息
    // 没有登录 user == null
    // 否则指向具体的用户对象

    // 若 currentUser == null, 说明当前用户没有登录
    private static User currentUser = null;

    // 登录
    public static void login(User user) {
        currentUser = user;
        System.out.println("刚刚登录的用户信息是: " + currentUser);
    }

    // 返回当前用户是谁
    public static  User getCurrentUser() {
        return currentUser;
    }

    // 判断用户是否已经登录
    public static boolean isLogined() {
        return currentUser != null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
