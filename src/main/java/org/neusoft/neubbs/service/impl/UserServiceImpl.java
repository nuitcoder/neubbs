package org.neusoft.neubbs.service.impl;

import com.alibaba.fastjson.JSON;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.dao.ITopicDAO;
import org.neusoft.neubbs.dao.ITopicReplyDAO;
import org.neusoft.neubbs.dao.IUserActionDAO;
import org.neusoft.neubbs.dao.IUserDAO;
import org.neusoft.neubbs.entity.UserActionDO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.exception.TokenErrorException;
import org.neusoft.neubbs.exception.TopicErrorException;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.utils.JsonUtil;
import org.neusoft.neubbs.utils.MapFilterUtil;
import org.neusoft.neubbs.utils.PatternUtil;
import org.neusoft.neubbs.utils.SecretUtil;
import org.neusoft.neubbs.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  IUserServer 接口实现类
 *
 *  @author Suva
 */
@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {

    private final IUserDAO userDAO;
    private final IUserActionDAO userActionDAO;
    private final ITopicDAO topicDAO;
    private final ITopicReplyDAO replyDAO;

    /**
     * Constructor
     */
    @Autowired
    public UserServiceImpl(IUserDAO userDAO, IUserActionDAO userActionDAO,
                           ITopicDAO topicDAO, ITopicReplyDAO replyDAO) {
        this.userDAO = userDAO;
        this.userActionDAO = userActionDAO;
        this.topicDAO = topicDAO;
        this.replyDAO = replyDAO;
    }

    @Override
    public UserDO registerUser(String username, String password, String email) {
        //judge username, email is occupied
        this.confirmUserNotOccupiedByUsername(username);
        this.confirmUserNotOccupiedByEmail(email);

        //build UserDO, UserActionDO
        UserDO user = new UserDO();
            user.setName(username);
            user.setEmail(email);
            user.setPassword(SecretUtil.encryptUserPassword(password));

        UserActionDO userAction = new UserActionDO();
            userAction.setUserId(user.getId());

        //insert forum_user, forum_user_action
        if (userDAO.saveUser(user) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.US1);
        }

        if (userActionDAO.saveUserAction(userAction) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.US33);
        }

        //update forum_user 'fu_avator', set default avatar
        if (userDAO.updateUserAvatorByName(user.getName(), ParamConst.USER_DEFAULT_IMAGE) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.US2);
        }

        //re-query user information
        return this.getUserInfoById(user.getId());
    }

    @Override
    public UserDO loginVerification(String username, String password) {
        //check username, get user information
        UserDO user;
        try {
            user = this.getUserInfoByName(username);
        } catch (AccountErrorException ae) {
            //prevent user know real error message
            throw new AccountErrorException(ApiMessage.USERNAME_OR_PASSWORD_INCORRECT).log(ae.getLog());
        }

        String cipherText = SecretUtil.encryptUserPassword(password);
        if (!cipherText.equals(user.getPassword())) {
            throw new AccountErrorException(ApiMessage.USERNAME_OR_PASSWORD_INCORRECT).log(LogWarnEnum.US7);
        }

        return user;
    }

    @Override
    public void confirmUserActivatedByEmail(String email) {
        if (this.isUserActivatedByState(this.getUserInfoByEmail(email).getState())) {
            throw new AccountErrorException(ApiMessage.ACCOUNT_ACTIVATED).log(LogWarnEnum.US5);
        }
    }

    @Override
    public void confirmUserMatchCookieUser(String inputUsername, UserDO cookieUser) {
        if (cookieUser == null || !inputUsername.equals(cookieUser.getName())) {
            throw new AccountErrorException(ApiMessage.NO_PERMISSION).log(LogWarnEnum.US10);
        }
    }

    @Override
    public int countUserTotals() {
       return userDAO.countUser();
    }

    @Override
    public int countUserTopicTotals(int userId) {
        this.getUserDONotNull(userId);
        return topicDAO.countTopicByUserId(userId);
    }

    @Override
    public int countUserReplyTotals(int userId) {
        this.getUserDONotNull(userId);
        return replyDAO.countReplyByUserId(userId);
    }

    @Override
    public int countUserLikeTopicTotals(int userId) {
        return JsonUtil.getArrayLength(this.getUserActionDONotNull(userId).getLikeTopicIdJsonArray());
    }

    @Override
    public int countUserCollectTopicTotals(int userId) {
        return JsonUtil.getArrayLength(this.getUserActionDONotNull(userId).getCollectTopicIdJsonArray());
    }

    @Override
    public int countUserAttentionTopicTotals(int userId) {
        return JsonUtil.getArrayLength(this.getUserActionDONotNull(userId).getAttentionTopicIdJsonArray());
    }

    @Override
    public int countUserFollowingTotals(int userId) {
        return JsonUtil.getArrayLength(this.getUserActionDONotNull(userId).getFollowingUserIdJsonArray());
    }

    @Override
    public int countUserFollowedTotals(int userId) {
        return JsonUtil.getArrayLength(this.getUserActionDONotNull(userId).getFollowedUserIdJsonArray());
    }

    @Override
    public Map<String, Object> getUserInfoModelMap(String username, String email) {
        UserDO user = username != null
                ? this.isEmailType(username) ? userDAO.getUserByEmail(username) : userDAO.getUserByName(username)
                : userDAO.getUserByEmail(email);

       Map<String, Object> userInfoMap = this.getUserInfoMap(user);

            //append extra information
            userInfoMap.put(ParamConst.FOLLOWING, this.countUserFollowingTotals(user.getId()));
            userInfoMap.put(ParamConst.FOLLOWED, this.countUserFollowedTotals(user.getId()));
            userInfoMap.put(ParamConst.LIKE, this.countUserLikeTopicTotals(user.getId()));
            userInfoMap.put(ParamConst.COLLECT, this.countUserCollectTopicTotals(user.getId()));
            userInfoMap.put(ParamConst.ATTENTION, this.countUserAttentionTopicTotals(user.getId()));
            userInfoMap.put(ParamConst.TOPIC, this.countUserTopicTotals(user.getId()));
            userInfoMap.put(ParamConst.REPLY, this.countUserReplyTotals(user.getId()));

        return userInfoMap;
    }

    @Override
    public UserDO getUserInfoById(int userId) {
        UserDO user = userDAO.getUserById(userId);
        if (isEmptyUser(user)) {
            throwNoUserException();
        }

        return user;
    }

    @Override
    public UserDO getUserInfoByName(String username) {
        UserDO user = userDAO.getUserByName(username);
        if (this.isEmptyUser(user)) {
            throwNoUserException();
        }

        return user;
    }

    @Override
    public UserDO getUserInfoByEmail(String email) {
        UserDO user = userDAO.getUserByEmail(email);
        if (this.isEmptyUser(user)) {
            throwNoUserException();
        }

        return user;
    }

    @Override
    public Map<String, Object> getUserInfoModelMap(UserDO user) {
        if (user == null) {
            return new HashMap<>(0);
        }

        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(user);
        MapFilterUtil.filterUserInfo(userInfoMap);

        //alter state type: int -> boolean
        Integer stateInt = (Integer) userInfoMap.get(ParamConst.STATE);
        userInfoMap.put(ParamConst.STATE, this.isUserActivatedByState(stateInt));

        return userInfoMap;
    }

    @Override
    public List<Map<String, Object>> listAllFollowingUserInfoModelList(int userId) {
        return this.getUserInfoMapListByJsonArray(this.getUserActionDONotNull(userId).getFollowingUserIdJsonArray());
    }

    @Override
    public List<Map<String, Object>> listAllFollowedUserInfoModelList(int userId) {
        return this.getUserInfoMapListByJsonArray(this.getUserActionDONotNull(userId).getFollowedUserIdJsonArray());
    }

    @Override
    public boolean isUserExist(String username, String email) {
        return username != null
                ? this.isEmailType(username) ? this.isExistUserByEmail(username) : this.isExistUserByName(username)
                : this.isExistUserByEmail(email);
    }


    @Override
    public boolean isUserActivatedByName(String username) {
        UserDO user = this.isEmailType(username) ? this.getUserInfoByEmail(username) : this.getUserInfoByName(username);
        return this.isUserActivatedByState(user.getState());
    }

    @Override
    public boolean isUserActivatedByState(int userCurrentState) {
        return userCurrentState == SetConst.ACCOUNT_ACTIVATED_STATE;
    }

    @Override
    public boolean isUserLikeTopic(int userId, int topicId) {
        this.getUserDONotNull(userId);

        String userLikeTopicIdIntJsonArrayString = userActionDAO.getUserAction(userId).getLikeTopicIdJsonArray();
        return JsonUtil.isExistIntElement(userLikeTopicIdIntJsonArrayString, topicId);
    }

    @Override
    public boolean isFollowingUser(int currentUserId, int followingUserId) {
        this.getUserDONotNull(currentUserId);
        this.getUserDONotNull(followingUserId);

        return JsonUtil.isExistIntElement(
                this.getUserFollowingUserIdJsonArrayStringByUserId(currentUserId), followingUserId
        );
    }

    @Override
    public Map<String, Object> alterUserProfile(String username, int sex, String birthday,
                                                String position, String description) {
        this.getUserInfoByName(username);

        UserDO updateUser = new UserDO();
            updateUser.setName(username);
            updateUser.setSex(sex);
            updateUser.setBirthday(birthday);
            updateUser.setPosition(position);
            updateUser.setDescription(description);

        //update forum_user by username, if 4 fields not null, will be updated
        if (userDAO.updateUser(updateUser) == 0) {
            throw new AccountErrorException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.US2);
        }

        return this.getUserInfoModelMap(userDAO.getUserByName(username));
    }

    @Override
    public void alterUserPasswordByName(String username, String newPassword) {
        this.getUserInfoByName(username);
        this.updateUserPasswordByName(username, newPassword);
    }

    @Override
    public void alterUserPasswordByEmail(String email, String newPassword) {
        String username = this.getUserInfoByEmail(email).getName();
        this.updateUserPasswordByName(username, newPassword);
    }

    @Override
    public void alterUserEmail(String username, String newEmail) {
        this.getUserInfoByName(username);
        this.confirmUserNotOccupiedByEmail(newEmail);

        if (userDAO.updateUserEmailByName(username, newEmail) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.US2);
        }
    }

    @Override
    public void alterUserAvatar(String username, String avatarFileName) {
        if (userDAO.updateUserAvatorByName(username, avatarFileName) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.US2);
        }
    }

    @Override
    public UserDO alterUserActivateStateByEmailToken(String emailToken) {
        //parse token
        String plainText = SecretUtil.decryptBase64(emailToken);
        String[] array = plainText.split("-");
        if (array.length != SetConst.LENGTH_TWO) {
            throw new TokenErrorException(ApiMessage.INVALID_TOKEN).log(LogWarnEnum.US12);
        }

        String email = array[0];
        if (!PatternUtil.matchEmail(email)) {
            throw new TokenErrorException(ApiMessage.INVALID_TOKEN).log(LogWarnEnum.US12);
        }
        String expireTime = array[1];
        if (StringUtil.isExpire(expireTime)) {
            throw new TokenErrorException(ApiMessage.LINK_INVALID).log(LogWarnEnum.US4);
        }

        this.confirmUserActivatedByEmail(email);

        if (userDAO.updateUserStateToActivateByEmail(email) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.US2);
        }

        return userDAO.getUserByEmail(email);
    }

    @Override
    public void operateLikeTopic(int userId, int topicId, String command) {
        //input 'inc'
        int effectRow;
        if (command.equals(SetConst.COMMAND_INC)) {
            effectRow = userActionDAO.updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId);
        } else {
            //input `dec`
            String likeTopicIdArrayString = userActionDAO.getUserAction(userId).getLikeTopicIdJsonArray();
            int indexTopicId = JSON.parseArray(likeTopicIdArrayString).indexOf(topicId);
            if (indexTopicId == SetConst.INDEX_NO) {
                throw new TopicErrorException(ApiMessage.USER_NO_LIKE_THIS_TOPIC).log(LogWarnEnum.TS21);
            }

            effectRow = userActionDAO.updateLikeTopicIdJsonArrayByIndexToRemoveOneTopicId(userId, indexTopicId);
        }

        if (effectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.US3);
        }
    }

    @Override
    public List<Integer> operateFollowUser(int currentUserId, int followingUserId) {
        //isFollowingUser() already checked 'currentUserId' and 'followingUserId'

        //true -> do 'dec', false -> do 'inc'
        if (this.isFollowingUser(currentUserId, followingUserId)) {
            this.decUserFollowingToUpdateDatabase(currentUserId, followingUserId);
        } else {
            this.incUserFollowingToUpdateDatabase(currentUserId, followingUserId);
        }

        return JsonUtil.toListByJsonArrayString(this.getUserFollowingUserIdJsonArrayStringByUserId(currentUserId));
    }


    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

    /*
     * ***********************************************
     * database operate method (update forum_user_action)
     *      - (inc | dec) user attention user
     *      - update user password
     * ***********************************************
     */

    /**
     * (inc) 用户主动关注
     *
     * @param currentUserId 当前用户id
     * @param followingUserId 主动关注用户id
     */
    private void incUserFollowingToUpdateDatabase(int currentUserId, int followingUserId) {
        //add following user for current user
        if (userActionDAO.updateFollowingUserIdJsonArrayByOneUserIdToAppendEnd(currentUserId, followingUserId) == 0) {
            throwUserActionUpdateFailException(SetConst.FOLLOWING_INC);
        }

        //add followed user for following user
        if (userActionDAO.updateFollowedUserIdJsonArrayByOneUserIdToAppendEnd(followingUserId, currentUserId) == 0) {
            throwUserActionUpdateFailException(SetConst.FOLLOWED_INC);
        }
    }

    /**
     * (dec) 用户主动关注
     *
     * @param currentUserId 当前用户id
     * @param followingUserId 将取消的主动关注用户id
     */
    private void decUserFollowingToUpdateDatabase(int currentUserId, int followingUserId) {
        String followingUserJsonArrayString = this.getUserFollowingUserIdJsonArrayStringByUserId(currentUserId);
        int userIdIndex = JsonUtil.getIntElementIndex(followingUserJsonArrayString, followingUserId);
        if (userActionDAO.updateFollowingUserIdJsonArrayByIndexToRemoveOneUserId(currentUserId, userIdIndex) == 0) {
            throwUserActionUpdateFailException(SetConst.FOLLOWING_DEC);
        }

        String followedUserJsonArrayString = this.getUserFollowedUserIdJsonArrayStringByUserId(followingUserId);
        userIdIndex = JsonUtil.getIntElementIndex(followedUserJsonArrayString, currentUserId);
        if (userActionDAO.updateFollowedUserIdJsonArrayByIndexToRemoveOneUserId(followingUserId, userIdIndex) == 0) {
            throwUserActionUpdateFailException(SetConst.FOLLOWED_DEC);
        }
    }

    /**
     * 更新用户密码
     *
     * @param username 用户名
     * @param newPassword 新密码
     */
    private void updateUserPasswordByName(String username, String newPassword) {
        //update forum_user 'fu_password'
        if (userDAO.updateUserPasswordByName(username, SecretUtil.encryptUserPassword(newPassword)) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarnEnum.US2);
        }
    }


    /*
     * ***********************************************
     * filter information map (use util/MapFilter.java)
     * ***********************************************
     */

    /**
     * 获取用户基本信息 Map
     *
     * @param user 用户对象
     * @return Map 已处理的用户信息
     */
    private Map<String, Object> getUserInfoMap(UserDO user) {
        if (user == null) {
            return new LinkedHashMap<>(SetConst.SIZE_ONE);
        }

        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(user);
        MapFilterUtil.filterUserInfo(userInfoMap);

        return userInfoMap;
    }

    private List<Map<String, Object>> getUserInfoMapListByJsonArray(String userIdJsonArray) {
        List<Integer> userIdList = JsonUtil.toListByJsonArrayString(userIdJsonArray);

        List<Map<String, Object>> userInfoMapList = new ArrayList<>(userIdList.size());
        for (int userId: userIdList) {
            userInfoMapList.add(this.getUserInfoMap(this.getUserInfoById(userId)));
        }

        return userInfoMapList;
    }

     /*
     * ***********************************************
     * confirm method
     * ***********************************************
     */

    /**
     * （用户名）确认用户未被占用
     *
     * @param username 用户名
     */
    private void confirmUserNotOccupiedByUsername(String username) {
        if (userDAO.getUserByName(username) != null) {
            throw new AccountErrorException(ApiMessage.USERNAME_REGISTERED).log(LogWarnEnum.US11);
        }
    }

    /**
     * （邮箱）确认用户未被占用
     *
     * @param email 用户邮箱
     */
    private void confirmUserNotOccupiedByEmail(String email) {
        if (userDAO.getUserByEmail(email) != null) {
            throw new AccountErrorException(ApiMessage.EMAIL_REGISTERED).log(LogWarnEnum.US6);
        }
    }

    /*
     * ***********************************************
     * is method
     * ***********************************************
     */
    /**
     * 是否为邮件类型
     *
     * @param username 用户名
     * @return boolean 判断结果（true-是，false-否）
     */
    private boolean isEmailType(String username) {
        return PatternUtil.matchEmail(username);
    }

    /**
     * （用户名）是否存在用户
     *
     * @param username 用户名
     * @return boolean 存在结果（true-存在，false-不存在）
     */
    private boolean isExistUserByName(String username) {
        return userDAO.getUserByName(username) != null;
    }

    /**
     * （用户邮箱）是否存在用户
     *
     * @param email 用户邮箱
     * @return boolean 存在结果
     */
    private boolean isExistUserByEmail(String email) {
        return userDAO.getUserByEmail(email) != null;
    }

    /**
     * 判断用户是否为空
     *
     * @param user 用户对象
     * @return boolean 返回结果
     */
    private boolean isEmptyUser(UserDO user) {
        return user == null;
    }

    /*
     * ***********************************************
     * get method
     * ***********************************************
     */

    /**
     * 获取用户对象不能为空
     *
     * @param userId 用户id
     * @return UserDO 用户对象
     */
    private UserDO getUserDONotNull(int userId) {
        UserDO user = userDAO.getUserById(userId);
        if (user == null) {
            throwNoUserException();
        }

        return user;
    }

    /**
     * 获取用户行为对象不能为空
     *
     * @param userId 用户id
     * @return UserActionDO 用户行为对象
     */
    private UserActionDO getUserActionDONotNull(int userId) {
        UserActionDO userAction = userActionDAO.getUserAction(userId);
        if (userAction == null) {
            throwNoUserException();
        }

        return userAction;
    }

    /**
     * 获取用户主动关注用户 id JSON 数组字符串
     *      - 用户不能为空
     *
     * @param userId 用户id
     * @return String 主动关注用户idJSON数组字符串
     */
    private String getUserFollowingUserIdJsonArrayStringByUserId(int userId) {
        return userActionDAO.getUserActionFollowingUserIdJsonArray(userId).getFollowingUserIdJsonArray();
    }

    /**
     * 获取用户被关注用户 id JSON 数组字符串
     *      - 用户不能为空
     *
     * @param userId 用户id
     * @return String 被关注用户idJSON数组字符串
     */
    private String getUserFollowedUserIdJsonArrayStringByUserId(int userId) {
        return userActionDAO.getUserActionFollowedUserIdJsonArray(userId).getFollowedUserIdJsonArray();
    }

    /*
     * ***********************************************
     * throw exception method
     * ***********************************************
     */
    /**
     * 抛出不存用户异常
     */
    private void throwNoUserException() {
        throw new AccountErrorException(ApiMessage.NO_USER).log(LogWarnEnum.US16);
    }

    /**
     * 抛出用户行为更新失败异常
     *      - 更新 forum_user_action 失败（关注用户）
     *
     * @param userOperate 用户操作
     */
    private void throwUserActionUpdateFailException(String userOperate) {
        throw new AccountErrorException(ApiMessage.USER_OPERATE_FAIL).log(LogWarnEnum.US15);
    }
}
