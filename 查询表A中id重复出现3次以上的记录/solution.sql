-- 查询表 A 中 id 出现三次以上的记录

-- 首先查询出现三次以上的 id
select id from A group by id having count(id) > 3

-- 查询这些 id 对应的信息
select * from A where id in (select id from A group by id having count(id) > 3); 