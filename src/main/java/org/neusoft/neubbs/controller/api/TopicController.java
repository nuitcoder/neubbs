package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.RequestParamCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
    @RequestMapping(value = "/topic", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO saveTopic(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        Integer userId = (Integer) requestBodyParamsMap.get(ParamConst.USER_ID);
        String category = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String title = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String content = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        Map<String, String> typeParamMap = new HashMap<>(SetConst.SIZE_FOUR);
            typeParamMap.put(ParamConst.ID, String.valueOf(userId));
            typeParamMap.put(ParamConst.CATEGORY, category);
            typeParamMap.put(ParamConst.TITLE, title);
            typeParamMap.put(ParamConst.TOPIC_CONTENT, content);
        RequestParamCheckUtil.check(typeParamMap);

        int topicId = topicService.saveTopic(userId, category, title, content);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.TOPIC_ID, topicId);
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
    @RequestMapping(value = "/topic/reply", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO saveReply(@RequestBody Map<String, Object> requetBodyParamsMap) throws Exception {
        Integer userId = (Integer) requetBodyParamsMap.get(ParamConst.USER_ID);
        Integer topicId = (Integer) requetBodyParamsMap.get(ParamConst.TOPIC_ID);
        String content = (String) requetBodyParamsMap.get(ParamConst.CONTENT);

        Map<String, String> typeParmaMap = new HashMap<>(SetConst.SIZE_THREE);
            typeParmaMap.put(ParamConst.ID, String.valueOf(userId));
            typeParmaMap.put(ParamConst.ID, String.valueOf(topicId));
            typeParmaMap.put(ParamConst.REPLY_CONTENT, content);

        int replyId = topicService.saveReply(userId, topicId, content);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.REPLY_ID, replyId);
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
    @RequestMapping(value = "/topic-remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO removeTopic(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        RequestParamCheckUtil.check(ParamConst.ID, String.valueOf(topicId));

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
    @RequestMapping(value = "/topic/reply-remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO removeReply(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        int replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);

        RequestParamCheckUtil.check(ParamConst.ID, String.valueOf(replyId));

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
    @RequestMapping(value = "/topic/content-update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO updateTopicContent(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
        String category = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String title = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String content = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);

        Map<String, String> typeParamMap = new HashMap<>(SetConst.SIZE_FOUR);
            typeParamMap.put(ParamConst.ID, String.valueOf(topicId));
            typeParamMap.put(ParamConst.CATEGORY, category);
            typeParamMap.put(ParamConst.TITLE, title);
            typeParamMap.put(ParamConst.TOPIC_CONTENT, content);
        RequestParamCheckUtil.check(typeParamMap);

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
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws Exception 所有异常
     */
   @LoginAuthorization @AccountActivation
   @RequestMapping(value = "/topic/reply/content-update", method = RequestMethod.POST, consumes = "application/json")
   @ResponseBody
   public ResponseJsonDTO updateReplyContent(@RequestBody Map<String, Object> requestBodyParamsMap) throws Exception {
       Integer replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);
       String content = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

       Map<String, String> typeParamMap = new HashMap<>(SetConst.SIZE_TWO);
            typeParamMap.put(ParamConst.ID, String.valueOf(typeParamMap));
            typeParamMap.put(ParamConst.REPLY_CONTENT, content);
       RequestParamCheckUtil.check(typeParamMap);

       topicService.alterReplyContent(replyId, content);

       return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
   }
}
