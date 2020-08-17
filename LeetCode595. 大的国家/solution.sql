-- 有一张 world 表
-- 如果一个国家的面积超过 300 万平方公里, 或者人口超过 2500 万, 那么这个国家就是大国家
-- 编写一个 sql 查询, 输出表中所有大国家的名称, 人口和面积
select 
    name, population, area
from 
    World 
where 
    area > 3000000 or population > 25000000;