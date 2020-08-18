-- 有趣的电影
-- 某城市开了一家新的电影院, 吸引了很多人过来看电影, 该电影院特别注意用户体验, 专门有个 LED 显示板作为用户推荐. 上面公布着影评和相关电影描述
-- 编写一个 sql 查询, 找出所有影片描述的非 boring 的并且 id 为奇数的影片, 结果请按等级 rating 排列
select 
    id, movie, description, rating 
from
    cinema
where
    description not in ('boring') and  id % 2 = 1
order by rating desc;