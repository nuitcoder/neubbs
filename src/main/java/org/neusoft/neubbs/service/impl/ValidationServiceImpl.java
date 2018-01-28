package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarnEnum;
import org.neusoft.neubbs.exception.ParamsErrorException;
import org.neusoft.neubbs.service.IValidationService;
import org.neusoft.neubbs.utils.PatternUtil;
import org.neusoft.neubbs.utils.RequestParamCheckUtil;
import org.springframework.stereotype.Service;

/**
 * IValidationService 实现类
 *
 * @author Suvan
 */
@Service("paramCheckServiceImpl")
public class ValidationServiceImpl implements IValidationService {

    @Override
    public IValidationService check(String paramType, String paramValue) {
        RequestParamCheckUtil.check(paramType, paramValue);
        return this;
    }

    @Override
    public IValidationService checkUsername(String username) {
        String paramType = PatternUtil.matchEmail(username) ? ParamConst.EMAIL : ParamConst.USERNAME;
        RequestParamCheckUtil.check(paramType, username);
        return this;
    }

    @Override
    public void checkCommand(String command, String... commandArray) {
        for (String commandElement: commandArray) {
            if (commandElement.equals(command)) {
                return;
            }
        }

        throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(LogWarnEnum.VS2);
    }

    @Override
    public void checkOnlyNotNullParam(String... params) {
        int len = params.length;
        if (len == 0 || len % SetConst.LENGTH_TWO != 0) {
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(LogWarnEnum.US4);
        }

        for (int i = 0; i < len; i += SetConst.LENGTH_TWO) {
             String type = params[i];
             String value = params[i + 1];

            //if value != null, check param
            if (value != null) {
                this.check(type, value);
            }
        }
    }

    @Override
    public void checkParamsNotAllNull(String... params) {
        for (String param: params) {
            if (param != null) {
                return;
            }
        }

        throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(LogWarnEnum.VS1);
    }
}
