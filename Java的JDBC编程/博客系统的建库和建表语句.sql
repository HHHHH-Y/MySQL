-- 建库
create database guyueyue_blog charset utf8mb4;
use guyueyue_blog;

-- 建表
-- 用户表
create table user (
    id int primary key auto_increment comment "自增 id",
    username varchar(200) not null unique comment "唯一的用户名",
    nickname varchar(200) not null comment "显示昵称",
    password varchar(200) not null comment "登录密码"
);

-- 文章表
create table article(
      id int primary key auto_increment comment "自增 id",
      author_id int not null comment "作者id",
      title varchar(200) not null comment "文章标题",
      publish_at datetime not null comment "文章发表时间",
      content text not null comment "文章正文"
);

-- 评论表
create table comments(
    id int primary key auto_increment comment "自增 id",
    user_id int not null comment "评论者id",
    article_id int not null comment "文章id",
    publish_at datetime not null comment "评论时间",
    content varchar(200) not null comment "评论内容"
);

-- 点赞关系表
-- 使用成复合主键的形式
create table like_relation(
    user_id int not null comment "评论者id",
    article_id int not null comment "文章id",
    primary key(user_id, article_id)
);