package com.yuehu.MySQL;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Created with IntelliJ IDEA.
 * Description: 模拟一个 MySQL 的客户端
 * User: HHH.Y
 * Date: 2020-06-06
 */
public class Client {
    /**
     * 以下配置可以通过 parseArguments 进行替换
     */
    private static  String host = "127.0.0.1";
    private static int port = 3306;
    private static String user = null;
    private static String password = "";
    private static String defaultDatabaseName = "";

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(Arrays.toString(args));
        // args 代表的就是用户输入的参数
        if(args.length == 0) {
            // 说明用户什么参数都没有输入
            printUsageAndExit(); // 打印使用说明并直接退出
        }

        // 解析参数, 获取连接数据库的信息
        parseArgument(args);

        // 1. 进行数据库连接
        Class.forName("com.mysql.jdbc.Driver");
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&characterEncoding=utf8",host, port, defaultDatabaseName);
        try {
            // 一次连接
            Connection connection = DriverManager.getConnection(url, user, password);
            printWelcome();
            Scanner scanner = new Scanner(System.in);

            // 进入之前所谓的 mysql 界面
            System.out.print("mysql> ");
            // 一次连接, 多次 sql 执行
            while (true) {
                String cmd = scanner.nextLine();
                // 模拟, 使用 quit 可以退出
                // quit 不需要输入 分号
                if(cmd.equalsIgnoreCase("quit")) {
                    break;
                }

                // 模拟真实的 mysql 中, 不输入 分号, 一行命令就不结束的现象
                while (!cmd.endsWith(";")) {
                    System.out.print("   -> ");
                    cmd += scanner.nextLine();
                }

                // 分号只是 mysql 程序的要求, 不是 sql 语言的要求
                cmd = cmd.substring(0, cmd.length() - 1);
                System.out.println(cmd);

                // 多次执行 sql
                if(cmd.startsWith("select") || cmd.startsWith("show")) {
                    executeQuery(connection, cmd);
                } else {
                    executeUpdate(connection, cmd);
                }
                System.out.print("mysql> ");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeUpdate(Connection connection, String cmd) throws SQLException {
        Statement statement = connection.createStatement();
        int affectRows = statement.executeUpdate(cmd);
        System.out.printf("Query Ok, %d row affected%n", affectRows);
        statement.close();
    }

    private static void executeQuery(Connection connection, String cmd) throws SQLException {
        long b = System.currentTimeMillis();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(cmd);

        int columnCount = resultSet.getMetaData().getColumnCount(); // 一共多少列, 拿到所有的列数
        // 打印表头 - 打印列的名称
        for (int i = 0; i < columnCount; i++) {
            String label = resultSet.getMetaData().getColumnLabel(i + 1);
            if(i != columnCount - 1) {
                System.out.print(label + ", ");
            } else {
                System.out.println(label);
            }
        }
        // 依次去遍历每一行, 打印每一行的结果
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
            for (int i = 0; i < columnCount; i++) {
                String val = resultSet.getString(i + 1);
                if(i != columnCount - 1) {
                    System.out.print(val + ", ");
                } else {
                    System.out.println(val);
                }
            }
        }
        long e =  System.currentTimeMillis();
        System.out.printf("%d rows int set (%.2f sec)%n", rowCount, (e - b) / 1000.0);
        resultSet.close();
        statement.close();
    }

    private static void printWelcome() {
        System.out.println("欢迎使用mysqlClient");
        System.out.println();
    }

    private static void parseArgument(String[] args) {
        defaultDatabaseName = args[args.length - 1];
        args = Arrays.copyOfRange(args, 0, args.length - 1);

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--help":
                    printUsageAndExit();
                case "-h":
                case "--host":
                    host = args[++i];
                    break;
                case "-P":
                case "--port":
                    port = Integer.parseInt(args[++i]);
                    break;
                case "-u":
                case "--user":
                    user = args[++i];
                    break;
                case "-p":
                case "--password":
                    password = args[++i];
                    break;
                default:
                    printUsageAndExit("不认识的选项: " + arg);
            }
        }
    }

    private static void printUsageAndExit(String ... messages) {
        System.out.println("使用帮助 : mysql [选项] 默认数据库");
        System.out.println(" --help 打印帮助信息");
        System.out.println(" -h, --host 连接主机, 默认是 127.0.0.1");
        System.out.println(" -P, --port 连接端口, 默认是 3306");
        System.out.println(" -u, --user mysql用户名, 必须设置");
        System.out.println(" -p, --password mysql密码");

        // 1. stream 的操作
        // 2. 方法引用
        // 等同于下面代码的简单版本
        Arrays.stream(messages).peek(System.out::println);

        /*for (String s:messages) {
            System.out.println(s);
        }*/
        System.exit(1);
    }
}
