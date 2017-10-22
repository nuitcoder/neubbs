package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.TopicInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.util.RequestParamsCheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Topic api
 *      1.发表话题
 *      2.发表回复
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/topic")
public class TopicInfoController {

    @Autowired
    private ITopicService topicService;

    /**
     * 1.发表话题
     *
     * @param requestBodyParamsMap rquest请求的body参数
     * @return ResponseJsonDTO 传输对象，api 显示
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO saveTopic(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception{
        /*
         * 发表流程
         *      1. requestBody 获取参数
         *      2. userid,category,title,content 参数合法性检测
         *      3. 持久化到数据库
          *     4. 返回成功提示信息
         */
        Integer userid = (Integer)requestBodyParamsMap.get(TopicInfo.USERID);
        String category = (String)requestBodyParamsMap.get(TopicInfo.CATEGORY);
        String title = (String)requestBodyParamsMap.get(TopicInfo.TITLE);
        String content = (String)requestBodyParamsMap.get(TopicInfo.CONTENT);

        String errorInfo = RequestParamsCheckUtils
                                .putParamKeys(new String[]{TopicInfo.USERID, TopicInfo.CATEGORY, TopicInfo.TITLE, TopicInfo.CONTENT})
                                .putParamValues(new String[]{String.valueOf(userid), category, title, content})
                                .checkParamsNorm();
        if (errorInfo != null) {
            throw new TopicErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        topicService.saveTopic(userid, category, title, content);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, TopicInfo.PUBLISH_TOPIC_SUCCESS);
    }

    /**
     * 2.发表回复
     *
     * @param requetBodyParamsMap request请求body参数
     * @return ResponseJsonDTO 传输对象，api 显示
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/save-reply", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO saveReply(@RequestBody Map<String, Object> requetBodyParamsMap) throws Exception{
        Integer userId = (Integer)requetBodyParamsMap.get(TopicInfo.USERID);
        Integer topicId = (Integer)requetBodyParamsMap.get(TopicInfo.TOPICID);
        String content = (String)requetBodyParamsMap.get(TopicInfo.CONTENT);

        String errorInfo = RequestParamsCheckUtils
                                .putParamKeys(new String[]{TopicInfo.USERID, TopicInfo.TOPICID, TopicInfo.CONTENT})
                                .putParamValues(new String[]{String.valueOf(userId), String.valueOf(topicId), content})
                                .checkParamsNorm();
        if (errorInfo != null) {
            throw new TopicErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        topicService.saveReply(userId, topicId, content);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, TopicInfo.PUBLISH_REPLY_SUCCESS);
    }
}
