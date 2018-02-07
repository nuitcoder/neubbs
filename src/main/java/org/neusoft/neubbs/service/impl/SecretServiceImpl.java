package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.service.ISecretService;
import org.neusoft.neubbs.utils.TokenUtil;
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
        return SecretUtil.encodeBase64(email + "-" + StringUtil.getTodayTwentyFourClockTimestamp());
    }

    @Override
    public String generateUserInfoAuthentication(UserDO user) {
        return TokenUtil.generateUserInfoToken(user);
    }

    @Override
    public UserDO getUserInfoByAuthentication(String authentication) {
        //input authentication and key
        UserDO user = TokenUtil.decryptUserInfoToken(authentication);
        if (user == null) {
            throw new AccountErrorException(ApiMessage.TOKEN_EXPIRED).log(LogWarnEnum.US4);
        }

        return user;
    }
}
