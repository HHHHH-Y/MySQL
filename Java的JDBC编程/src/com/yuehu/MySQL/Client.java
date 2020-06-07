package com.yuehu.MySQL;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * 模拟一个 MySQL 命令行客户端
 * 1. JDBC 的使用
 * 2. 理解我们平时使用的 mysql 客户端是如何使用的, 例如: 为什么要加入参数? 为什么要用分号结尾? 不用分号结尾会怎么办?
 * 3. 理解 cmd 上启动 java 的各种知识点
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
                cmd = cmd.substring(0, cmd.length() - 1); // 截取到不带分号的 sql 语句
                System.out.println(cmd);

                // select 和 show 开头的 sql, 是会返回结果的
                // 所以使用带 resultSet 的查询方式
                // 否则使用 executeUpdate 的执行方式
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

    private static void executeUpdate(Connection connection, String sql) throws SQLException {
        long b = System.currentTimeMillis();
        Statement statement = connection.createStatement();
        int affectRows = statement.executeUpdate(sql);
        long e = System.currentTimeMillis();
        System.out.printf("Query Ok, %d row affected (%.2f sec)%n", affectRows, (e - b) / 1000.0);
        statement.close();
    }

    private static void executeQuery(Connection connection, String sql) throws SQLException {
        long b = System.currentTimeMillis();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // 利用结果集中的 "元数据 MetaData" 获取一些基本信息
        // 一共有多少列
        int columnCount = resultSet.getMetaData().getColumnCount(); // resultSet.getMetaData()  从结果集中拿到元信息

        // 打印表头 - 打印列的名称
        for (int i = 0; i < columnCount; i++) {
            String label = resultSet.getMetaData().getColumnLabel(i + 1); // 通过"元信息"获取到各列的名称, 列仍然是从1开始的
            if(i != columnCount - 1) {
                System.out.print(label + ", ");
            } else {
                System.out.println(label);
            }
        }

        // 打印内容
        // 依次去遍历每一行, 打印每一行的结果
        int rowCount = 0;
        while (resultSet.next()) { // 控制行
            rowCount++;
            for (int i = 0; i < columnCount; i++) { // 控制列
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

    // 解析参数
    private static void parseArgument(String[] args) {
        // 避免了数组越界异常
        if(args.length % 2 == 0) {
            printUsageAndExit("必须输入默认数据库名称");
        }
        defaultDatabaseName = args[args.length - 1];
        args = Arrays.copyOfRange(args, 0, args.length - 1);

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
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
