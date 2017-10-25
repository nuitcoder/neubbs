package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.TopicInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.RequestParamsCheckUtil;
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
 *      3.删除话题
 *      4.删除回复
 *      6.修改话题内容
 *      7.修改回复内容
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api/topic")
public class TopicController {

    private final ITopicService topicService;

    /**
     * Constructor
     */
    @Autowired
    public TopicController(ITopicService topicService){
        this.topicService = topicService;
    }

    /**
     * 1.发表话题
     *
     * @param requestBodyParamsMap reuest-body内JSON数据
     * @return ResponseJsonDTO 传输对象，api 显示
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation
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
        Integer userId = (Integer)requestBodyParamsMap.get(TopicInfo.USERID);
        String category = (String)requestBodyParamsMap.get(TopicInfo.CATEGORY);
        String title = (String)requestBodyParamsMap.get(TopicInfo.TITLE);
        String content = (String)requestBodyParamsMap.get(TopicInfo.CONTENT);

        String errorInfo = RequestParamsCheckUtil
                                .putParamKeys(new String[]{TopicInfo.USERID, TopicInfo.CATEGORY, TopicInfo.TITLE, TopicInfo.CONTENT})
                                .putParamValues(new String[]{String.valueOf(userId), category, title, content})
                                .checkParamsNorm();
        if (errorInfo != null) {
            throw new ParamsErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        topicService.saveTopic(userId, category, title, content);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, TopicInfo.SAVE_TOPIC_SUCCESS);
    }

    /**
     * 2.发表回复
     *
     * @param requetBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/save-reply", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO saveReply(@RequestBody Map<String, Object> requetBodyParamsMap) throws Exception{
        Integer userId = (Integer)requetBodyParamsMap.get(TopicInfo.USERID);
        Integer topicId = (Integer)requetBodyParamsMap.get(TopicInfo.TOPICID);
        String content = (String)requetBodyParamsMap.get(TopicInfo.CONTENT);

        String errorInfo = RequestParamsCheckUtil
                                .putParamKeys(new String[]{TopicInfo.USERID, TopicInfo.TOPICID, TopicInfo.CONTENT})
                                .putParamValues(new String[]{String.valueOf(userId), String.valueOf(topicId), content})
                                .checkParamsNorm();
        if (errorInfo != null) {
            throw new ParamsErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        topicService.saveReply(userId, topicId, content);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, TopicInfo.SAVE_REPLY_SUCCESS);
    }

    /**
     * 3.删除话题
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应Json传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation @AdminRank
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO removeTopic(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception{
        Integer topicId = (Integer)requestBodyParamsMap.get(TopicInfo.TOPICID);

        String errorInfo = RequestParamsCheckUtil.checkId(String.valueOf(topicId));
        if (errorInfo != null) {
            throw new ParamsErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        if (topicService.getTopic(topicId) == null) {
            throw new TopicErrorException(TopicInfo.NO_TOPIC).log(LogWarnInfo.NO_EXIST_TOPIC);
        }

        topicService.removeTopic(topicId);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, TopicInfo.REMOVE_TOPIC_SUCCESS);
    }

    /**
     * 4.删除回复
     *
     * @param requetsBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/remove-reply", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO removeReply(@RequestBody Map<String, Object> requetsBodyParamsMap) throws Exception{
        Integer replyId = (Integer)requetsBodyParamsMap.get(TopicInfo.REPLYID);

        String errorInfo = RequestParamsCheckUtil.checkId(String.valueOf(replyId));
        if (errorInfo != null) {
            throw new ParamsErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        if (topicService.getReply(replyId) == null) {
            throw new TopicErrorException(TopicInfo.NO_REPLY).log(LogWarnInfo.NO_EXIST_REPLY);
        }

        topicService.removeReply(replyId);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, TopicInfo.REMOVE_REPLY_SUCCESS);
    }
}
