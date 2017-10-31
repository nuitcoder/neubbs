package org.neusoft.neubbs.controller.api;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.TopicInfo;
import org.neusoft.neubbs.constant.log.LogWarnInfo;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.RequestParamsCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.Topic;
import java.util.Map;

/**
 * Topic api
 *      发布话题
 *      发布回复
 *      删除话题
 *      删除回复
 *      修改话题内容
 *      修改回复内容
 *
 * @author Suvan
 */
@Controller
@RequestMapping("/api")
public class TopicController {

    private final ITopicService topicService;

    /**
     * Constructor
     */
    @Autowired
    public TopicController(ITopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * 发布话题
     *
     * 业务流程
     *      A.参数处理
     *      B.数据库操作
     *      C.返回成功状态 + 新增话题 id
     *
     * @param requestBodyParamsMap reuest-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输数据
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO saveTopic(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        Integer userId = (Integer) requestBodyParamsMap.get(TopicInfo.USERID);
        String category = (String) requestBodyParamsMap.get(TopicInfo.CATEGORY);
        String title = (String) requestBodyParamsMap.get(TopicInfo.TITLE);
        String content = (String) requestBodyParamsMap.get(TopicInfo.CONTENT);

        String errorInfo = RequestParamsCheckUtil
                                .putParamKeys(new String[]{TopicInfo.ID, TopicInfo.CATEGORY,
                                                                TopicInfo.TITLE, TopicInfo.CONTENT})
                                .putParamValues(new String[]{String.valueOf(userId), category, title, content})
                                .checkParamsNorm();
        if (errorInfo != null) {
            throw new ParamsErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        int topicId = topicService.saveTopic(userId, category, title, content);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, TopicInfo.TOPICID, topicId);
    }

    /**
     * 发布回复
     *
     * 业务流程
     *      A.参数处理
     *      B.数据库操作
     *      C.返回状态 + 新回复 id
     *
     * @param requetBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/reply", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO saveReply(@RequestBody Map<String, Object> requetBodyParamsMap) throws Exception {
        Integer userId = (Integer) requetBodyParamsMap.get(TopicInfo.USERID);
        Integer topicId = (Integer) requetBodyParamsMap.get(TopicInfo.TOPICID);
        String content = (String) requetBodyParamsMap.get(TopicInfo.CONTENT);

        String errorInfo = RequestParamsCheckUtil
                                .putParamKeys(new String[]{TopicInfo.USERID, TopicInfo.TOPICID, TopicInfo.CONTENT})
                                .putParamValues(new String[]{String.valueOf(userId), String.valueOf(topicId), content})
                                .checkParamsNorm();
        if (errorInfo != null) {
            throw new ParamsErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        int replyId = topicService.saveReply(userId, topicId, content);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, TopicInfo.REPLYID, replyId);
    }

    /**
     * 删除话题
     *
     * 业务流程
     *      A.参数处理
     *      B.数据库操作
     *      C.返回状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应Json传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation @AdminRank
    @RequestMapping(value = "/topic-remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO removeTopic(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        Integer topicId = (Integer) requestBodyParamsMap.get(TopicInfo.TOPICID);

        String errorInfo = RequestParamsCheckUtil.checkId(String.valueOf(topicId));
        if (errorInfo != null) {
            throw new ParamsErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        topicService.removeTopic(topicId);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 删除回复
     *
     * 业务流程
     *      A.参数处理
     *      B.数据库操作
     *      C.返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/reply-remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO removeReply(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        Integer replyId = (Integer) requestBodyParamsMap.get(TopicInfo.REPLYID);

        String errorInfo = RequestParamsCheckUtil.checkId(String.valueOf(replyId));
        if (errorInfo != null) {
            throw new ParamsErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        topicService.removeReply(replyId);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 修改主题内容
     *
     * 业务流程
     *      A.参数处理
     *      B.数据库操作
     *      C.返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/content-update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonDTO updateTopicContent(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        Integer topicId = (Integer) requestBodyParamsMap.get(TopicInfo.TOPICID);
        String category = (String) requestBodyParamsMap.get(TopicInfo.CATEGORY);
        String title = (String) requestBodyParamsMap.get(TopicInfo.TITLE);
        String content = (String) requestBodyParamsMap.get(TopicInfo.CATEGORY);

        String errorInfo = RequestParamsCheckUtil.putParamKeys(new String[] {TopicInfo.ID, TopicInfo.TITLE,
                                                                                TopicInfo.CATEGORY, TopicInfo.CONTENT})
                                                 .putParamValues(new String[] {String.valueOf(topicId), title,
                                                                                category, content})
                                                 .checkParamsNorm();
        if (errorInfo != null) {
            throw new ParamsErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
        }

        topicService.alterTopicContent(topicId, category, title, content);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
   }

    /**
     * 修改回复内容
     *
     * 业务内容
     *      A.参数处理
     *      B.数据库操作
     *      C.返回状态
     *
     * @param requestBodyParamsMap
     * @return
     * @throws Exception
     */
   @LoginAuthorization @AccountActivation
   @RequestMapping(value = "/topic/reply/content-update", method = RequestMethod.POST)
   @ResponseBody
   public ResponseJsonDTO updateReplyContent(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
       Integer replyId = (Integer) requestBodyParamsMap.get(TopicInfo.REPLYID);
       String content = (String) requestBodyParamsMap.get(TopicInfo.CONTENT);

       //这里需加入回复内容检查类型
       String errorInfo = RequestParamsCheckUtil.putParamKeys(new String[] {TopicInfo.ID, TopicInfo.REPLY})
                                                .putParamValues(new String[] {String.valueOf(replyId), content})
                                                .checkParamsNorm();
       if (errorInfo != null) {
            throw new TopicErrorException(TopicInfo.PARAM_ERROR).log(errorInfo);
       }

       //插入回复操作
       topicService.alterReplyContent(replyId, content);

       return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
   }
}
