package blog;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * 项目构想:
 *      1. 打印操作菜单
 *          1. 用户注册
 *          2. 用户登录
 *          3. 查看文章列表 (只给出 id, 按照发表时间倒序)
 *          4. 发表文章 (要求用户先登录)
 *          5. 查看指定文章的内容
 *          6. 对指定文章发表评论 (要求用户先登录)
 *          7. 对指定文章点赞
 *          0. 退出
 *      2. 读取用户的选择
 *      3. 根据用户的不同选择, 进行不同的操作
 * User: HHH.Y
 * Date: 2020-06-07
 */
public class Main {
    // 当前登录用户信息
    // 没有登录 user == null
    // 否则指向具体的用户对象
    //private static User user = null;
    private static List<String> featureList = new ArrayList<>(); // 使用集合 featureList, 用于存放菜单中的信息
    private static List<Action> actionsList = new ArrayList<>(); // 使用集合 actionsList, 用于存放执行动作信息

    private static void initFeatureList() {
        featureList.add("用户注册");
        featureList.add("用户登录");
        featureList.add("查看文章列表(按照发表时间倒序)");
        featureList.add("发表文章 (要求用户先登录)");
        featureList.add("查看指定文章的内容");
        featureList.add("对指定文章发表评论 (要求用户先登录)");
        featureList.add("对指定文章点赞");
    }



    private static void initActionList() {
        actionsList.add(new UserRegisterAction());
    }

    private static void showMenu() {
        System.out.println("欢迎使用博客系统, 支持以下功能: ");
        for (int i = 0; i < featureList.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, featureList.get(i));
        }
        System.out.println("  0. 退出");
    }

    private static void showPrompt() {
        System.out.print("请输入功能的序号> ");
    }


    public static void main(String[] args) {
        // 初始化菜单
        initFeatureList();
        initActionList();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // 1. 打印菜单
            showMenu();
            // 给出提示
            showPrompt();
            // 2. 获取用户的输入
            int select = scanner.nextInt();
            // 3. 根据具体的用户输入, 执行相应的语句动作
            doAction(select);
        }
    }

    private static void doAction(int select) {
        if(select == 0) {
            System.out.println("欢迎下次再来");
            System.exit(0);
        }
        System.out.println("您的选择是: " + featureList.get(select - 1));
        if(select - 1 < actionsList.size()) {
            Action action = actionsList.get(select - 1);
            action.run();
        } else {
            System.out.println("该功能尚未支持, 敬请期待");
        }
    }

    // 用户注册的实现类
    static class UserRegisterAction implements Action {
        @Override
        public void run() {
            userRegister();
        }
    }
    private static void userRegister() {
        System.out.println("开始用户注册");
    }
}
