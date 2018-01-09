package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.log.LogWarn;
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
    public void checkInstructionOfSpecifyArray(String instructionParam, String... instructionArray) {
        for (String allowInstruction: instructionArray) {
            if (allowInstruction.equals(instructionParam)) {
                return;
            }
        }

        throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(LogWarn.PARAM_CHECK_02);
    }

    @Override
    public void checkNotNullParamsKeyValue(String... params) {
        int len = params.length;
        if (len == SetConst.ZERO || len % SetConst.TWO != 0) {
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(LogWarn.USER_05);
        }

        int i = SetConst.ONE;
        while (i < len) {
            String type = params[i - SetConst.ONE];
            String value = params[i];

            if (value != null) {
                this.check(type, value);
            }

            i += SetConst.TWO;
        }
    }

    @Override
    public void paramsNotNull(String... params) {
        int count = 0;
        for (String param: params) {
            if (param == null) {
              count++;
            }
        }

        if (count == params.length) {
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log(LogWarn.PARAM_CHECK_01);
        }
    }

    @Override
    public String getUsernameParamType(String username) {
        return PatternUtil.matchEmail(username) ? ParamConst.EMAIL : ParamConst.USERNAME;
    }
}
