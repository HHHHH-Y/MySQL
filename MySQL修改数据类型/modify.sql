-- 修改数据类型

-- student 表中列 sname 类型是 char(20), 现在要修改为 varchar(20), 请写出 SQL 语句
alter table student modify column sname varchar(20);

-- char 存不下了, 怎么修改成 text 类型
alter table 表名 modify column char类型的字段 text;