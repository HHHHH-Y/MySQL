package com.yuehu.MySQL;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Created with IntelliJ IDEA.
 * Description:
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

        parseArgument(args);

        // 1. 进行数据库连接
        Class.forName("com.mysql.jdbc.Driver");
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&characterEncoding=utf8",host, port, defaultDatabaseName);
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            printWelcome();
            Scanner scanner = new Scanner(System.in);

            System.out.println("mysql> ");
            while (true) {
                String cmd = scanner.nextLine();
                if(cmd.equalsIgnoreCase("quit")) {
                    break;
                }
                if(cmd.startsWith("select") || cmd.startsWith("show")) {
                    executeQuery(connection, cmd);
                } else {
                    executeUpdate(connection, cmd);
                }
                System.out.println("mysql> ");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeUpdate(Connection connection, String cmd) {

    }

    private static void executeQuery(Connection connection, String cmd) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(cmd);
        int columnCount = resultSet.getMetaData().getColumnCount(); // 一共多少列, 拿到所有的列数
        while (resultSet.next()) {
            for (int i = 0; i < columnCount; i++) {
                String val = resultSet.getString(i + 1);
                System.out.println(val + ",");
            }
            System.out.println();
        }
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
