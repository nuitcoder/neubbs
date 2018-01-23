package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.service.ISecretService;
import org.neusoft.neubbs.utils.JwtTokenUtil;
import org.neusoft.neubbs.utils.SecretUtil;
import org.neusoft.neubbs.utils.StringUtil;
import org.springframework.stereotype.Service;

/**
 * ISecretService 实现类
 *
 * @author Suvan
 */
@Service("secretServiceImpl")
public class SecretServiceImpl implements ISecretService {

    @Override
    public String generateValidateEmailToken(String email) {
        return SecretUtil.encryptBase64(email + "-" + StringUtil.getTodayTwentyFourClockTimestamp());
    }

    @Override
    public String generateUserInfoAuthentication(UserDO user) {
        return JwtTokenUtil.createToken(user);
    }

    @Override
    public UserDO getUserInfoByAuthentication(String authentication) {
        //input authentication and key
        UserDO user = JwtTokenUtil.verifyToken(authentication, SetConst.JWT_TOKEN_SECRET_KEY);
        if (user == null) {
            throw new AccountErrorException(ApiMessage.TOKEN_EXPIRED).log(LogWarnEnum.US4);
        }

        return user;
    }
}
