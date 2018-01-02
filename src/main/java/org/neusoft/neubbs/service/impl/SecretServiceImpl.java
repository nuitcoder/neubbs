package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.exception.AccountErrorException;
import org.neusoft.neubbs.entity.UserDO;
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
    public String getEmailActivateToken(String email) {
        return SecretUtil.encryptBase64(email + "-" + StringUtil.getTodayTwentyFourClockTimestamp());
    }

    @Override
    public String jwtCreateTokenByUser(UserDO user) {
        return JwtTokenUtil.createToken(user);
    }

    @Override
    public UserDO jwtVerifyTokenByTokenByKey(String token, String key) {
        UserDO user = JwtTokenUtil.verifyToken(token, key);
        if (user == null) {
            throw new AccountErrorException(ApiMessage.TOKEN_EXPIRED).log(LogWarn.USER_05);
        }
        return user;
    }
}
