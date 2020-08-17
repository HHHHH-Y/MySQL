-- 交换工资
-- 给定一个 salary 表, 有 m = 男性, f = 女性 的值, 交换所有的 f 和 m  值(例如: 将所有的 f 值更改为 m, 反之亦然)
-- 要求只使用一个 更新(update)语句, 并且没有中间的临时表
-- 注意: 你必只能写一个update语句, 请不要编写任何 select 语句

-- 本题涉及到条件判断: 如果 sex 为 m, 则将其设置为 f, 如果不为 m , 则将其设置为 m
-- 所以使用 mysql 中的 case when then else end

-- case                --如果
-- when xxx then aaa   --当 xxx 时, 返回 aaa
-- else(bbb)           --否则 bbb
-- end                 --结束
update 
    salary
set 
    sex = case sex
    when 'f' then 'm'
    else 'f'
end;

-- 或者直接使用 if(xxx, aaa, bbb)  表示的是 如果xxx, 就执行aaa, 否则执行bbb
update
    salary
set
    sex = if(sex = 'f', 'm', 'f');