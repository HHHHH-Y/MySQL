-- 从不订购的客户
-- 某网站包含两个表, Customers 表和 Orders 表, 编写一个 sql 查询, 找出所有从不订购任何东西的客户

-- 首先找出 Orders 表中的 id 
select CustomerId from Orders;

-- 再查询不在 Orders 表中的客户名称
select 
    name Customers
from 
    Customers
where 
    id not in (select CustomerId from Orders);