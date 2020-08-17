-- 超过 5 名学生的课
-- 有一个 courses 表, 有 student(学生) 和 class(课程), 请列出所有超过或等于 5 名学生的课

-- 使用 dinstinct 对学生进行去重操作, 防止同一门课中学生被重复计算
select class from courses group by class having count(distinct student) >= 5;