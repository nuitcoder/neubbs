package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.MessageDO;
import org.springframework.stereotype.Repository;

/**
 * 信息数据访问接口
 *      - 针对 forum_message
 *      - resources/mapping/MessageMapper.xml 配置 SQL
 *
 * @author Suvan
 */
@Repository
public interface IMessageDAO {

    /**
     * 保存消息
     *      - 消息对象需包含（消息来源，发送人 id，发送内容，回复人 id）
     *      - 新生成的消息，自增 id 会重新注入传入的 MessageDO 对象
     *
     * @param message 消息对象
     * @return int 插入行数
     */
    int saveMessage(MessageDO message);

    /**
     * （id）获取消息对象
     *
     * @param messageId 消息 id
     * @return MessageDO 消息对象
     */
    MessageDO getMessage(int messageId);

    /**
     * 更新消息接收时间（回复人接收）
     *      - MySQL 自动更新为当前时间
     *
     * @param messageId 消息 id
     * @return int 更新行数
     */
    int updateMessageReceiveTime(int messageId);
}
