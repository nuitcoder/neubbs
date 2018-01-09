package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.PageJsonDTO;
import org.neusoft.neubbs.dto.PageJsonListDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.service.IValidationService;
import org.neusoft.neubbs.service.ISecretService;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 话题 api
 *      获取话题信息
 *      获取回复信息
 *      获取热议话题信息列表
 *      获取话题信息列表
 *      获取话题总页数
 *      获取话题分类信息列表
 *      发布话题
 *      发布回复
 *      删除话题
 *      删除回复
 *      编辑话题
 *      编辑回复
 *      点赞话题
 *      点赞话题（新接口）
 *      收藏话题
 *      关注话题
 *
 * @author Suvan
 */
@RestController
@RequestMapping("/api")
public class TopicController {

    private final IValidationService validationService;
    private final ITopicService topicService;
    private final IUserService userService;
    private final IHttpService httpService;
    private final ISecretService secretService;

    @Autowired
    public TopicController(IValidationService validationService, ITopicService topicService,
                           IUserService userService, IHttpService httpService,
                           ISecretService secretService) {
        this.validationService = validationService;
        this.topicService = topicService;
        this.userService = userService;
        this.httpService = httpService;
        this.secretService = secretService;
    }


    /**
     * 获取话题信息
     *      - 能够获取当前用户是否点赞该文章信息（访客用户默认为 false）
     *      - hadread 口令决定是否增加阅读数（0-不增加, 1-增加）
     *
     * @param topicId 话题id
     * @param hadRead 阅读数是否增加（0-不增加，1-增加）
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    public PageJsonDTO getTopicInfo(@RequestParam(value = "topicid", required = false) String topicId,
                             @RequestParam(value = "hadread", required = false) String hadRead,
                             HttpServletRequest request) {
        validationService.check(ParamConst.ID, topicId);

        //judge whether to increase read count
        boolean isAddTopicRead = false;
        if (hadRead != null) {
            validationService.checkInstructionOfSpecifyArray(hadRead, "0", "1");
            isAddTopicRead = SetConst.COMMAND_ONE.equals(hadRead);
        }

        int topicIdInt = Integer.parseInt(topicId);
        Map<String, Object> topicContentPageModelMap = topicService.getTopicContentPageModelMap(topicIdInt);

        //judge current user like topic state(visit user default value of false)
        boolean isCurrentUserLikeThisTopic = false;
        if (httpService.isLoggedInUser(request)) {
            UserDO currentUser = secretService.jwtVerifyTokenByTokenByKey(
                    httpService.getAuthenticationCookieValue(request), SetConst.JWT_TOKEN_SECRET_KEY
            );
            isCurrentUserLikeThisTopic = userService.isUserLikeTopic(currentUser.getId(), topicIdInt);
        }

        //read + 1 (default no add)
        if (isAddTopicRead) {
            topicService.alterTopicReadAddOne(topicIdInt);
            topicContentPageModelMap.put(ParamConst.READ,
                    (int) topicContentPageModelMap.get(ParamConst.READ) + SetConst.ONE);
        }

        topicContentPageModelMap.put(ParamConst.IS_LIKE_TOPIC, isCurrentUserLikeThisTopic);
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, topicContentPageModelMap);
    }

    /**
     * 获取回复信息
     *
     * @param replyId 回复id
     * @return PageJsonDTO 页面JSON传输对象
     */
    @RequestMapping(value = "/topic/reply", method = RequestMethod.GET)
    public PageJsonDTO getTopicReplyInfo(@RequestParam(value = "replyid", required = false) String replyId) {
        validationService.check(ParamConst.ID, replyId);
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, topicService.getReplyPageModelMap(Integer.parseInt(replyId)));
    }

    /**
     * 获取热议话题列表
     *
     * @return PageJsonListDTO 页面JSON列表传输对象
     */
    @RequestMapping(value = "/topics/hot", method = RequestMethod.GET)
    public PageJsonListDTO listTopicsInfo() {
        return new PageJsonListDTO(AjaxRequestStatus.SUCCESS, topicService.listHotTalkTopics());
    }

    /**
     * 获取话题信息列表
     *      - 分页，指定数量
     *      - 未输入 limit 参数，使用 neubbs.properties 参数文件内指定的默认值
     *
     * @param limit 每页显示数量
     * @param page 跳转到指定页数
     * @return PageJsonListDTO 响应JSON传输列表对象
     */
    @RequestMapping(value = "/topics", method = RequestMethod.GET)
    public PageJsonListDTO listTopics(@RequestParam(value = "limit", required = false) String limit,
                                      @RequestParam(value = "page", required = false) String page,
                                      @RequestParam(value = "category", required = false) String category,
                                      @RequestParam(value = "username", required = false) String username) {
        validationService.check(ParamConst.NUMBER, page);

        //judge input param(limit, category, username), if no input, user the default value
        validationService.checkNotNullParamsKeyValue(
                ParamConst.NUMBER, limit,
                ParamConst.TOPIC_CATEGORY_NICK, category,
                ParamConst.USERNAME, username
        );

        int inputLimit = limit != null ? Integer.parseInt(limit) : SetConst.ZERO;
        return new PageJsonListDTO(AjaxRequestStatus.SUCCESS,
                topicService.listTopics(inputLimit, Integer.parseInt(page), category, username));
    }


    /**
     * 获取话题总页数
     *
     * @param limit 每页显示条数
     * @return PageJsonDTO 页面JSON传输对象
     */
    @RequestMapping(value = "/topics/pages", method = RequestMethod.GET)
    public PageJsonDTO countTopicTotalPages(@RequestParam(value = "limit", required = false) String limit,
                                            @RequestParam(value = "category", required = false) String category,
                                            @RequestParam(value = "username", required = false) String username) {
        validationService.checkNotNullParamsKeyValue(
                ParamConst.NUMBER, limit,
                ParamConst.TOPIC_CATEGORY_NICK, category,
                ParamConst.USERNAME, username
        );

        int inputLimit = limit != null ? Integer.parseInt(limit) : SetConst.ZERO;
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS,
                ParamConst.TOTAL_PAGES, topicService.countTopicTotalPages(inputLimit, category, username));
    }

    /**
     * 获取话题分类信息列表
     *
     * @return PageJsonListDTO 页面JSON传输数据
     */
    @RequestMapping(value = "/topics/categorys", method = RequestMethod.GET)
    public PageJsonListDTO listTopicCategories() {
        return new PageJsonListDTO(AjaxRequestStatus.SUCCESS,  topicService.listAllTopicCategorys());
    }

    /**
     * 发布话题
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输数据
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic", method = RequestMethod.POST, consumes = "application/json")
    public PageJsonDTO releaseTopic(@RequestBody Map<String, Object> requestBodyParamsMap, HttpServletRequest request) {
        String category = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String title = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String topicContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        validationService.check(ParamConst.TOPIC_CATEGORY_NICK, category)
                         .check(ParamConst.TOPIC_TITLE, title)
                         .check(ParamConst.TOPIC_CONTENT, topicContent);

        UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
                httpService.getAuthenticationCookieValue(request), SetConst.JWT_TOKEN_SECRET_KEY
        );

        int topicId = topicService.saveTopic(cookieUser.getId(), category, title, topicContent);

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.TOPIC_ID, topicId);
    }

    /**
     * 发布回复
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/reply", method = RequestMethod.POST, consumes = "application/json")
    public PageJsonDTO releaseTopciReply(@RequestBody Map<String, Object> requestBodyParamsMap, HttpServletRequest request) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
        String replyContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        validationService.check(ParamConst.ID, String.valueOf(topicId)).check(ParamConst.REPLY_CONTENT, replyContent);

        UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
                httpService.getAuthenticationCookieValue(request), SetConst.JWT_TOKEN_SECRET_KEY
        );

        int replyId = topicService.saveReply(cookieUser.getId(), topicId, replyContent);
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.REPLY_ID, replyId);
    }

    /**
     * 删除话题
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return PageJsonDTO 页面Json传输对象
     */
    @LoginAuthorization @AccountActivation @AdminRank
    @RequestMapping(value = "/topic-remove", method = RequestMethod.POST, consumes = "application/json")
    public PageJsonDTO removeTopic(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        validationService.check(ParamConst.ID, String.valueOf(topicId));

        topicService.removeTopic(topicId);

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 删除回复
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return PageJsonDTO 页面JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/reply-remove", method = RequestMethod.POST, consumes = "application/json")
    public PageJsonDTO removeTopicReply(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);

        validationService.check(ParamConst.ID, String.valueOf(replyId));

        topicService.removeReply(replyId);

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 编辑话题
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return PageJsonDTO 页面JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic-update", method = RequestMethod.POST, consumes = "application/json")
    public PageJsonDTO updateTopic(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
        String newCategoryNick = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String newTitle = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String newTopicContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        validationService.check(ParamConst.ID, String.valueOf(topicId))
                         .check(ParamConst.TOPIC_CATEGORY_NICK, newCategoryNick)
                         .check(ParamConst.TOPIC_TITLE, newTitle)
                         .check(ParamConst.TOPIC_CONTENT, newTopicContent);

        topicService.alterTopicContent(topicId, newCategoryNick, newTitle, newTopicContent);

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS);
   }

    /**
     * 编辑回复
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return PageJsonDTO 页面JSON传输对象
     */
   @LoginAuthorization @AccountActivation
   @RequestMapping(value = "/topic/reply-update", method = RequestMethod.POST, consumes = "application/json")
   public PageJsonDTO updateTopicReply(@RequestBody Map<String, Object> requestBodyParamsMap) {
       Integer replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);
       String newReplyContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

       validationService.check(ParamConst.ID, String.valueOf(replyId)).check(ParamConst.REPLY_CONTENT, newReplyContent);

       topicService.alterReplyContent(replyId, newReplyContent);

       return new PageJsonDTO(AjaxRequestStatus.SUCCESS);
   }

    /**
     * 点赞话题
     *      - 需要 command 命令（用于点赞 or 取消）
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
   @LoginAuthorization @AccountActivation
   @RequestMapping(value = "/topic/like", method = RequestMethod.POST, consumes = "application/json")
   public PageJsonDTO likeTopic(@RequestBody Map<String, Object> requestBodyParamsMap, HttpServletRequest request) {
       Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
       String command = (String) requestBodyParamsMap.get(ParamConst.COMMAND);

       validationService.check(ParamConst.TOPIC_ID, String.valueOf(topicId));
       validationService.checkInstructionOfSpecifyArray(command, SetConst.COMMAND_INC, SetConst.COMMAND_DEC);

       UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
               httpService.getAuthenticationCookieValue(request), SetConst.JWT_TOKEN_SECRET_KEY
       );

       //judge cuurent user like topic, according the command('inc', 'dec'), alter like of topic
       boolean isCurrentUserLikeTopic = userService.isUserLikeTopic(cookieUser.getId(), topicId);
       int currentTopicLike = topicService.alterTopicLikeByInstruction(isCurrentUserLikeTopic, topicId, command);

       //record user like topic id array of user action
       userService.alterUserActionLikeTopicIdArray(cookieUser.getId(), topicId, command);

       return new PageJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.LIKE, currentTopicLike);
   }

    /**
     * 点赞话题（新接口）
     *      - 不需要 command 命令
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/newlike", method = RequestMethod.POST, consumes = "application/json")
    public PageJsonDTO newLikeTopic(@RequestBody Map<String, Object> requestBodyParamsMap, HttpServletRequest request) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        validationService.check(ParamConst.TOPIC_ID, String.valueOf(topicId));

        UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
                httpService.getAuthenticationCookieValue(request), SetConst.JWT_TOKEN_SECRET_KEY
        );

        Map<String, Object> resultMap = new LinkedHashMap<>(SetConst.SIZE_TWO);
            resultMap.put(ParamConst.USER_LIKE_TOPIC_ID, topicService.operateLikeTopic(cookieUser.getId(), topicId));
            resultMap.put(ParamConst.LIKE, topicService.countTopicContentLike(topicId));
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, resultMap);
    }

    /**
     * 收藏话题
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return PageJsonDTO　页面JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/collect", method = RequestMethod.POST, consumes = "application/json")
    public PageJsonDTO collectTopic(@RequestBody Map<String, Object> requestBodyParamsMap, HttpServletRequest request) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        validationService.check(ParamConst.TOPIC_ID, String.valueOf(topicId));

        UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
                httpService.getAuthenticationCookieValue(request), SetConst.JWT_TOKEN_SECRET_KEY
        );

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS,
                ParamConst.USER_COLLECT_TOPIC_ID, topicService.operateCollectTopic(cookieUser.getId(), topicId));
    }

    /**
     * 关注话题
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/attention", method = RequestMethod.POST, consumes = "application/json")
    public PageJsonDTO attentionTopic(@RequestBody Map<String, Object> requestBodyParamsMap, HttpServletRequest request) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        validationService.check(ParamConst.TOPIC_ID, String.valueOf(topicId));

        UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
                httpService.getAuthenticationCookieValue(request), SetConst.JWT_TOKEN_SECRET_KEY
        );

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS,
                ParamConst.USER_ATTENTION_TOPIC_ID, topicService.operateAttentionTopic(cookieUser.getId(), topicId));
    }
}
