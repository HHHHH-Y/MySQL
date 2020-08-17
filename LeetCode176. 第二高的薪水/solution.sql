-- 第二高的薪水
-- 编写一个 sql 查询, 获取 Empolyee 表中的第二高薪水 (Salary). 如果不存在第二高薪水, 那么查询应该返回 null

-- 只能在第二高工资存在的情况下使用, 无法表示不存在的情况
-- 可以将其作为一张临时表, 然后再进行查询
-- select distinct Salary SecondHighestSalary from Employee order by Salary desc limit 1 offset 1; 


select 
    (select distinct 
        Salary 
    from 
        Employee 
    order by Salary desc 
    limit 1 offset 1) SecondHighestSalary;

-- 或者使用 ifnull 函数, 如果要查找的信息为 null, 就返回 null. 否则就返回应该返回的信息
select 
    ifnull (
        (select distinct 
            Salary 
        from 
            Employee 
        order by Salary desc 
        limit 1 offset 1),
    null) SecondHighestSalary;