package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.service.IParamCheckService;
import org.neusoft.neubbs.utils.PatternUtil;
import org.neusoft.neubbs.utils.RequestParamCheckUtil;
import org.springframework.stereotype.Service;

/**
 * IParamCheckService 实现类
 *
 * @author Suvan
 */
@Service("paramCheckServiceImpl")
public class ParamCheckServiceImpl implements IParamCheckService {

    @Override
    public void check(String paramType, String paramValue) {
        RequestParamCheckUtil.check(paramType, paramValue);
    }

    @Override
    public void checkTwoParamNotNulThreeCondition(String paramType1, String paramValue1,
                                                  String paramType2, String paramValue2) {
        if (paramValue1 != null && paramValue2 != null) {
            this.check(paramType1, paramValue1);
            this.check(paramType2, paramValue2);
        } else if (paramValue1 != null) {
            this.check(paramType1, paramValue1);
        } else if (paramValue2 != null) {
            this.check(paramType2, paramValue2);
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
            throw new ParamsErrorException(ApiMessage.PARAM_ERROR).log("需按规定输入相应参数，不能输入空参数");
        }
    }

    @Override
    public String getUsernameParamType(String username) {
        return PatternUtil.matchEmail(username) ? ParamConst.EMAIL : ParamConst.USERNAME;
    }
}
