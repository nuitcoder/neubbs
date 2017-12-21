package org.neusoft.neubbs.dao;

import org.neusoft.neubbs.entity.UserDynamicDO;
import org.springframework.stereotype.Repository;

/**
 * 用户动态数据访问接口
 *      - 针对 forum_user_dynamic
 *      - resources/mapping/UserDynamicMapper.xml 配置 SQL
 *
 * @author Suvan
 */
@Repository
public interface IUserDynamicDAO {

    /**
     * 保存用户动态
     *      - 用于注册用户时初始化
     *      - UserDynamicDO 对象需注入 userId
     *
     * @param userDynamic 用户动态对象
     * @return int 插入行数
     */
    int saveUserDynamic(UserDynamicDO userDynamic);

    /**
     * 获取用户动态
     *      - 可获取用户所有发布动态
     *
     * @param userId 用户id
     * @return UserDynamicDO 用户动态对象
     */
    UserDynamicDO getUserDynamic(int userId);

    /**
     * 更新发布动态信息 JSON 数组（末尾追加新的动态信息）
     *      - 用户动态信息约定格式
     *          - {
     *              "id": ,               //int，id 从 1 开始，i（id - 1）可作为删除索引
     *              "createTime":  ,      //long, 时间戳格式
     *              "content": "",        //消息内容
     *              "visibilityType", ""  //可见性类型
     *             }
     *
     * @param userId 用户id
     * @param newUserDynamicJsonInfo 新用户动态JSON信息
     * @return int 更新行数
     */
    int updatePublicInfoJsonArrayByOneDynamicInfoToAppendEnd(int userId, String newUserDynamicJsonInfo);

    /**
     * 更新发布动态信息
     *      - 索引位置从 0 开始，可以是 $.[index].id -1（信息内 id 从 1 开始）
     *
     * @param userId 用户id
     * @param indexOfRemoveDynamicInfo 要删除动态信息的索引位置
     * @return int 更新行数
     */
    int updatePublicInfoJsonArrayByIndexToRemoveOneDynamicInfo(int userId, int indexOfRemoveDynamicInfo);
}
