-- 查询每门课大于 60 分的学生姓名

-- 思路: 每门课都要 > 60 分, 说明最低分 > 60 就可以
select name from student group by name having min(score) > 60; 