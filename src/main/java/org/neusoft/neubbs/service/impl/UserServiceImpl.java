package org.neusoft.neubbs.service.impl;

import com.alibaba.fastjson.JSON;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
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
import org.neusoft.neubbs.utils.RequestParamCheckUtil;
import org.neusoft.neubbs.utils.SecretUtil;
import org.neusoft.neubbs.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
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

    /**
     * Constuctor
     */
    @Autowired
    public UserServiceImpl(IUserDAO userDAO, IUserActionDAO userActionDAO) {
        this.userDAO = userDAO;
        this.userActionDAO = userActionDAO;
    }

    @Override
    public UserDO registerUser(String username, String password, String email) {
        //judge username and email params, whether occupied
        this.confirmUserNotOccupiedByUsername(username);
        this.confirmUserNotOccupiedByEmail(email);

        //build user
        UserDO user = new UserDO();
            user.setName(username);
            user.setEmail(email);
            user.setPassword(SecretUtil.encryptUserPassword(password));

        //register user to database
        if (userDAO.saveUser(user) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_01);
        }

        //insert default user action record
        UserActionDO userAction = new UserActionDO();
            userAction.setUserid(user.getId());
        if (userActionDAO.saveUserAction(userAction) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_04);
        }

        //update data user image filed, set default avator image
        if (userDAO.updateUserAvatorByName(user.getName(), ParamConst.USER_DEFAULT_IMAGE) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
        }

        //again select user information by new register user id
        return this.getUserInfoById(user.getId());
    }

    @Override
    public UserDO loginAuthenticate(String username, String password) {
        //check username, get user information
        UserDO user;
        try {
            user = this.getUserInfoByName(username);
        } catch (AccountErrorException ae) {
            throw new AccountErrorException(ApiMessage.USERNAME_OR_PASSWORD_INCORRECT).log(ae.getLogMessage());
        }

        //judge user passowrd whether correct
        String cipherText = SecretUtil.encryptUserPassword(password);
        if (!cipherText.equals(user.getPassword())) {
            throw new AccountErrorException(ApiMessage.USERNAME_OR_PASSWORD_INCORRECT)
                    .log(username + LogWarn.ACCOUNT_09);
        }

        return user;
    }

    @Override
    public void confirmUserActivatedByEmail(String email) {
        UserDO user = this.getUserInfoByEmail(email);

        //judge user activate state
        if (this.isUserActivatedByState(user.getState())) {
            throw new AccountErrorException(ApiMessage.ACCOUNT_ACTIVATED).log(email + LogWarn.ACCOUNT_07);
        }
    }


    @Override
    public void confirmUserMatchCookieUser(String inputUsername, UserDO cookieUser) {
        if (cookieUser == null || !inputUsername.equals(cookieUser.getName())) {
            throw new AccountErrorException(ApiMessage.NO_PERMISSION).log(LogWarn.ACCOUNT_12);
        }
    }

    @Override
    public Map<String, Object> getUserInfoToPageModelMap(String username, String email) {
        //get user information
        UserDO user = username != null
                ? this.isEmailType(username) ? userDAO.getUserByEmail(username) : userDAO.getUserByName(username)
                : userDAO.getUserByEmail(email);

        return this.getUserInfoMapByUser(user);
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
        if (isEmptyUser(user)) {
            throwNoUserException();
        }

        return user;
    }

    @Override
    public UserDO getUserInfoByEmail(String email) {
        UserDO user = userDAO.getUserByEmail(email);
        if (isEmptyUser(user)) {
            throwNoUserException();
        }

        return user;
    }

    @Override
    public Map<String, Object> getUserInfoMapByUser(UserDO user) {
        if (user == null) {
            return new LinkedHashMap<>(SetConst.SIZE_ONE);
        }

        Map<String, Object> userInfoMap = JsonUtil.toMapByObject(user);
        MapFilterUtil.filterUserInfo(userInfoMap);

        //alter state type: int -> boolean
        Integer stateInt = (Integer) userInfoMap.get(ParamConst.STATE);
        userInfoMap.put(ParamConst.STATE, this.isUserActivatedByState(stateInt));

        return userInfoMap;
    }

    @Override
    public boolean isUserExist(String username, String email) {
        return username != null
                ? this.isEmailType(username) ? this.isExistUserByEmail(username) : this.isExistUserByName(username)
                : this.isExistUserByEmail(email);
    }


    @Override
    public boolean isUserActivatedByName(String username) {
        //input username param get UserDO instance
        UserDO user = PatternUtil.matchEmail(username)
                ? this.getUserInfoByEmail(username) : this.getUserInfoByName(username);

        return this.isUserActivatedByState(user.getState());
    }

    @Override
    public boolean isUserActivatedByState(int state) {
        return state == SetConst.ACCOUNT_STATE_TRUE;
    }

    @Override
    public boolean isUserLikeTopic(int userId, int topicId) {
        this.getUserDONotNull(userId);

        String userLikeTopicIdIntJsonArrayString = userActionDAO.getUserAction(userId).getLikeTopicidJsonArray();

        return JsonUtil.isJsonArrayStringExistIntElement(userLikeTopicIdIntJsonArrayString, topicId);
    }

    @Override
    public void alterUserPasswordByName(String username, String newPassword) {
        Map<String, String> paramsMap = new LinkedHashMap<>(SetConst.SIZE_TWO);
        paramsMap.put(ParamConst.USERNAME, username);
        paramsMap.put(ParamConst.PASSWORD, newPassword);
        RequestParamCheckUtil.check(paramsMap);

        //secret new password, update user passowrd
        if (userDAO.updateUserPasswordByName(username, SecretUtil.encryptUserPassword(newPassword)) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
        }
    }

    @Override
    public void alterUserPasswordByEmail(String email, String newPassword) {
        //get username by email
        String username = this.getUserInfoByEmail(email).getName();

        //secret new password, update user passowrd
        if (userDAO.updateUserPasswordByName(username, SecretUtil.encryptUserPassword(newPassword)) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
        }
    }

    @Override
    public void alterUserEmail(String username, String newEmail) {
        //judge new email, whether occupied
        this.getUserInfoByName(username);
        this.confirmUserNotOccupiedByEmail(newEmail);

        if (userDAO.updateUserEmailByName(username, newEmail) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
        }
    }

    @Override
    public void alterUserAvatorImage(String username, String newImageName) {
        if (userDAO.updateUserAvatorByName(username, newImageName) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
        }
    }

    @Override
    public void alterUserActivateStateByToken(String token) {
        //解析 token
        String plainText = SecretUtil.decryptBase64(token);
        String[] array = plainText.split("-");
        if (array.length != SetConst.LENGTH_TWO) {
            throw new TokenErrorException(ApiMessage.IVALID_TOKEN).log(token + LogWarn.ACCOUNT_15);
        }

        String email = array[0];
        if (!PatternUtil.matchEmail(email)) {
            throw new TokenErrorException(ApiMessage.IVALID_TOKEN).log(token + LogWarn.ACCOUNT_15);
        }
        String expireTime = array[1];
        if (StringUtil.isExpire(expireTime)) {
            throw new TokenErrorException(ApiMessage.LINK_INVALID).log(token + LogWarn.ACCOUNT_05);
        }

        this.confirmUserActivatedByEmail(email);

        if (userDAO.updateUserStateToActivateByEmail(email) == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_02);
        }
    }

    @Override
    public void alterUserActionLikeTopicIdArray(int userId, int topicId, String instruction) {
        //input 'inc'
        int effectRow = 0;
        if (instruction.equals(SetConst.INC)) {
            effectRow = userActionDAO.updateLikeTopicIdJsonArrayByOneTopicIdToAppendEnd(userId, topicId);
        } else {
            //input `dec`
            String likeTopicIdArrayString = userActionDAO.getUserAction(userId).getLikeTopicidJsonArray();
            int indexTopicId = JSON.parseArray(likeTopicIdArrayString).indexOf(topicId);
            if (indexTopicId == SetConst.NEGATIVE_ONE) {
                throw new TopicErrorException(ApiMessage.USER_NO_LIKE_THIS_TOPIC).log(LogWarn.TOPIC_22);
            }

            effectRow = userActionDAO.updateLikeTopicIdJsonArrayRemoveOneTopicIdByIndex(userId, indexTopicId);
        }

        if (effectRow == 0) {
            throw new DatabaseOperationFailException(ApiMessage.DATABASE_EXCEPTION).log(LogWarn.SERVICE_04);
        }
    }


    /*
     * ***********************************************
     * private method
     * ***********************************************
     */

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
            throw new AccountErrorException(ApiMessage.USERNAME_REGISTERED).log(username + LogWarn.ACCOUNT_14);
        }
    }

    /**
     * （邮箱）确认用户未被占用
     *
     * @param email 用户邮箱
     */
    private void confirmUserNotOccupiedByEmail(String email) {
        if (userDAO.getUserByEmail(email) != null) {
            throw new AccountErrorException(ApiMessage.EMAIL_REGISTERED).log(email + LogWarn.ACCOUNT_08);
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

    /*
     * ***********************************************
     * throw exception method
     * ***********************************************
     */
    /**
     * 抛出不存用户异常
     */
    private void throwNoUserException() {
        throw new AccountErrorException(ApiMessage.NO_USER).log(LogWarn.ACCOUNT_01);
    }
}
