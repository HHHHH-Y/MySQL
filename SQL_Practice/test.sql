-- 一个 person 表, 存着 id 和 email, 查询重复的 email
select distinct email from person group by email having count(*) > 1;