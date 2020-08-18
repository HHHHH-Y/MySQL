-- 删除重复的电子邮箱
-- 编写一个 sql 查询, 来删除 Person 表中所有重复的电子邮箱, 重复的邮箱里只保留 id 最小的那一个


-- 使用自联的方式找出所有邮箱相等的情况
select p1.* from Preson p1, Person p2 where p1.Email = p2.Email;
-- 进而找出具有相同邮箱地址中更大的 id
select p1.* from Person p1, Person p2 where p1.Email = p2.Email and p1.Id > p2.Id;
-- 将这些更大 id 的邮箱删除掉
delete p1 from Person p1, Person p2 where p1.Email = p2. Email and p1.Id > p2.Id;