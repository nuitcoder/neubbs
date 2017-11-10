package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.controller.exception.AccountErrorException;
import org.neusoft.neubbs.controller.exception.DatabaseOperationFailException;
import org.neusoft.neubbs.controller.exception.ParamsErrorException;
import org.neusoft.neubbs.controller.exception.TopicErrorException;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.dto.ResponseJsonListDTO;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.utils.RequestParamCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Topic api
 *      获取话题信息
 *      获取回复信息
 *      获取话题列表（分页，指定数量）
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
     * 获取话题信息
     *
     * @param topicId 话题id
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws TopicErrorException 话题错误异常
     */
    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getTopic(@RequestParam(value = "topicid", required = false) Integer topicId)
            throws ParamsErrorException, TopicErrorException {

        RequestParamCheckUtil.check(ParamConst.ID, String.valueOf(topicId));

        Map<String, Object> topicInfoMap =  topicService.getTopic(topicId);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, topicInfoMap);
    }

    /**
     * 获取回复信息
     *
     * @param replyId 回复id
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     */
    @RequestMapping(value = "/topic/reply", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO getReply(@RequestParam(value = "replyid", required = false) Integer replyId)
            throws ParamsErrorException {

        RequestParamCheckUtil.check(ParamConst.ID, String.valueOf(replyId));

        Map<String, Object> replyInfoMap = topicService.getReply(replyId);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, replyInfoMap);
    }

    /**
     * 获取话题列表（分页，指定数量）
     *
     * @param page 页数
     * @param count 每页显示话题数量
     * @return ResponseJsonListDTO 响应JSON传输列表对象昂
     * @throws ParamsErrorException 参数错误异常
     * @throws TopicErrorException 话题错误异常
     */
    @RequestMapping(value = "/topics", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonListDTO listTopics(@RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "count", required = false) Integer count)
            throws ParamsErrorException, TopicErrorException {

        Map<String, String> paramsMap = new HashMap<>(SetConst.SIZE_FOUR);
            paramsMap.put(ParamConst.NUMBER, String.valueOf(page));
            paramsMap.put(ParamConst.NUMBER, String.valueOf(count));
        RequestParamCheckUtil.check(paramsMap);

        List<Map<String, Object>> topics = topicService.listTopics(page, count);

        return new ResponseJsonListDTO(AjaxRequestStatus.SUCCESS, topics);
    }

    /**
     * 发布话题
     *
     * 业务流程
     *      - 参数处理
     *      - 数据库操作
     *      - 返回成功状态 + 新增话题 id
     *
     * @param requestBodyParamsMap reuest-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输数据
     * @throws ParamsErrorException 参数错误异常
     * @throws AccountErrorException 账户错误异常
     * @throws DatabaseOperationFailException 数据库操作失败失败异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO saveTopic(@RequestBody Map<String, Object> requestBodyParamsMap)
            throws ParamsErrorException, AccountErrorException, DatabaseOperationFailException {

        Integer userId = (Integer) requestBodyParamsMap.get(ParamConst.USER_ID);
        String category = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String title = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String topicContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        Map<String, String> paramsMap = new HashMap<>(SetConst.SIZE_FOUR);
            paramsMap.put(ParamConst.ID, String.valueOf(userId));
            paramsMap.put(ParamConst.CATEGORY, category);
            paramsMap.put(ParamConst.TITLE, title);
            paramsMap.put(ParamConst.TOPIC_CONTENT, topicContent);
        RequestParamCheckUtil.check(paramsMap);

        int topicId = topicService.saveTopic(userId, category, title, topicContent);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.TOPIC_ID, topicId);
    }

    /**
     * 发布回复
     *
     * 业务流程
     *      - 参数处理
     *      - 数据库操作
     *      - 返回状态 + 新回复 id
     *
     * @param requetBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws TopicErrorException 话题错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     * @throws AccountErrorException 账户错误异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/reply", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO saveReply(@RequestBody Map<String, Object> requetBodyParamsMap)
            throws ParamsErrorException, TopicErrorException, DatabaseOperationFailException, AccountErrorException {

        Integer userId = (Integer) requetBodyParamsMap.get(ParamConst.USER_ID);
        Integer topicId = (Integer) requetBodyParamsMap.get(ParamConst.TOPIC_ID);
        String replyContent = (String) requetBodyParamsMap.get(ParamConst.CONTENT);

        Map<String, String> paramsMap = new HashMap<>(SetConst.SIZE_THREE);
            paramsMap.put(ParamConst.ID, String.valueOf(userId));
            paramsMap.put(ParamConst.ID, String.valueOf(topicId));
            paramsMap.put(ParamConst.REPLY_CONTENT, replyContent);
        RequestParamCheckUtil.check(paramsMap);

        int replyId = topicService.saveReply(userId, topicId, replyContent);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.REPLY_ID, replyId);
    }

    /**
     * 删除话题
     *
     * 业务流程
     *      - 参数处理
     *      - 数据库操作
     *      - 返回状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应Json传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws DatabaseOperationFailException 数据库操作异常
     * @throws TopicErrorException 话题错误异常
     */
    @LoginAuthorization @AccountActivation @AdminRank
    @RequestMapping(value = "/topic-remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO removeTopic(@RequestBody Map<String, Object> requestBodyParamsMap)
            throws ParamsErrorException, DatabaseOperationFailException, TopicErrorException {

        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        RequestParamCheckUtil.check(ParamConst.ID, String.valueOf(topicId));

        topicService.removeTopic(topicId);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 删除回复
     *
     * 业务流程
     *      - 参数处理
     *      - 数据库操作
     *      - 返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     * @throws TopicErrorException 话题错误异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/reply-remove", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO removeReply(@RequestBody Map<String, Object> requestBodyParamsMap)
            throws ParamsErrorException, DatabaseOperationFailException, TopicErrorException {

        int replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);

        RequestParamCheckUtil.check(ParamConst.ID, String.valueOf(replyId));

        topicService.removeReply(replyId);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 修改话题内容
     *
     * 业务流程
     *      - 参数处理
     *      - 数据库操作
     *      - 返回成功状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws DatabaseOperationFailException 数据库操作失败异常
     * @throws TopicErrorException 话题错误异常
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/content-update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseJsonDTO updateTopicContent(@RequestBody Map<String, Object> requestBodyParamsMap)
            throws ParamsErrorException, DatabaseOperationFailException, TopicErrorException {

        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
        String newCategory = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String newTitle = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String newTopicContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        Map<String, String> paramsMap = new HashMap<>(SetConst.SIZE_FOUR);
            paramsMap.put(ParamConst.ID, String.valueOf(topicId));
            paramsMap.put(ParamConst.CATEGORY, newCategory);
            paramsMap.put(ParamConst.TITLE, newTitle);
            paramsMap.put(ParamConst.TOPIC_CONTENT, newTopicContent);
        RequestParamCheckUtil.check(paramsMap);

        topicService.alterTopicContent(topicId, newCategory, newTitle, newTopicContent);

        return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
   }

    /**
     * 修改回复内容
     *
     * 业务内容
     *      - 参数处理
     *      - 数据库操作
     *      - 返回状态
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return ResponseJsonDTO 响应JSON传输对象
     * @throws ParamsErrorException 参数错误异常
     * @throws  DatabaseOperationFailException 数据库操作失败异常
     * @throws TopicErrorException 话题错误异常
     */
   @LoginAuthorization @AccountActivation
   @RequestMapping(value = "/topic/reply/content-update", method = RequestMethod.POST, consumes = "application/json")
   @ResponseBody
   public ResponseJsonDTO updateReplyContent(@RequestBody Map<String, Object> requestBodyParamsMap)
           throws ParamsErrorException, DatabaseOperationFailException, TopicErrorException {

       Integer replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);
       String newReplyCntent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

       Map<String, String> paramsMap = new HashMap<>(SetConst.SIZE_TWO);
            paramsMap.put(ParamConst.ID, String.valueOf(replyId));
            paramsMap.put(ParamConst.REPLY_CONTENT, newReplyCntent);
       RequestParamCheckUtil.check(paramsMap);

       topicService.alterReplyContent(replyId, newReplyCntent);

       return new ResponseJsonDTO(AjaxRequestStatus.SUCCESS);
   }
}
