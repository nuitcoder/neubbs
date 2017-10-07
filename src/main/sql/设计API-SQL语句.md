# 设计接口

forum_user表
INSERT INTO forum_user(fu_name,fu_password,fu_email) VALUES ('suvan','12345','liushuwei0925@gmail');
DELETE FROM forum_user WHERE fu_id=1;
DELETE FROM forum_user WHERE fu_name='suvan';
SELECT fu_id,fu_name,fu_password,fu_sex,fu_birthday,fu_phone,fu_email,fu_address,fu_rank FROM forum_user WHERE fu_id=1;
UPDATE forum_user SET fu_password='222222' WHERE fu_id='1'; //修改密码
UPDATE forum_user SET fu_rank='admin' WHERE fu_name='suvan';//修改权限


forum_topic
INSERT INTO forum_topic(fu_id,ft_category,ft_title,ft_lastreplytime) VALUES (3,'Java','测试添加主题','2009.12.18 23:22:11');
DELETE FROM forum_topic WHERE ft_id = 1;
SELECT ft_id,fu_id,ft_category,ft_title,ft_comment,ft_lastreplytime,ft_createtime FROM forum_topic WHERE ft_id=2;
UPDATE forum_topic SET ft_comment = ft_comment + 1 WHERE ft_id = 2;//评论数+1
UPDATE forum_topic SET ft_lastreplytime = '2017.09.25 18:22:23' WHERE ft_id = 2; //修改最后回复时间


form_topic_content表
INSERT INTO forum_topic_content(ft_id,ftc_content) VALUES(2,'内容测试');
DELETE FROM forum_topic_content WHERE ftc_id=2;
SELECT ftc_id,ft_id,ftc_content,ftc_read FROM forum_topic_content WHERE ftc_id=1;
UPDATE forum_topic_content SET ftc_read = ftc_read + 1 WHERE ftc_id = 2;


forum_topic_reply表
INSERT INTO forum_topic_reply(fu_id,ft_id,ftr_content) VALUES (12,24,'这篇文章不错');
DELETE FROM forum_topic_reply WHERE ftr_id =1;
SELECT ftr_id,fu_id,ft_id,ftr_content,ftr_agree,ftr_oppose,ftr_createtime FROM forum_topic_reply WHERE ftr_id=2;
UPDATE forum_topic_reply SET ftr_agree = ftr_agree + 1 WHERE ftr_id = 2;  //点赞+1
UPDATE forum_topic_reply SET ftr_oppose = ftr_oppose + 1 WHERE ftr_id = 2;//反对+1
