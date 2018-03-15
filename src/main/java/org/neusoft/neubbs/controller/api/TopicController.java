package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ApiJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.service.ISecretService;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.service.IUserService;
import org.neusoft.neubbs.service.IValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 话题 api
 *      获取话题信息
 *      获取回复信息
 *      获取热议话题信息列表
 *      获取首页话题信息列表
 *      获取首页话题总页数
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
     *      - hadread 参数决定是否增加阅读数（0 - 不增加, 1 - 增加）
     *
     * @param topicId 话题 id
     * @param hadRead 阅读数是否增加（0 - 不增加，1 - 增加）
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    public ApiJsonDTO getTopicInfo(@RequestParam(value = "topicid", required = false) String topicId,
                                   @RequestParam(value = "hadread", required = false) String hadRead) {
        validationService.check(ParamConst.ID, topicId);

        //judge whether to increase read count
        boolean isAddTopicRead = false;
        if (hadRead != null) {
            validationService.checkCommand(hadRead, "0", "1");
            isAddTopicRead = SetConst.COMMAND_ONE.equals(hadRead);
        }

        int topicIdInt = Integer.parseInt(topicId);
        Map<String, Object> topicContentPageModelMap = topicService.getTopicContentModelMap(topicIdInt);

        //judge current user is like this topic (visit user default value of 'false')
        boolean isLikeTopicForCurrentUser = false;
        if (httpService.isUserLoginState()) {
            UserDO currentUser = secretService.getUserInfoByAuthentication(httpService.getAuthenticationCookieValue());
            isLikeTopicForCurrentUser = userService.isUserLikeTopic(currentUser.getId(), topicIdInt);
        }
        topicContentPageModelMap.put(ParamConst.IS_LIKE_TOPIC, isLikeTopicForCurrentUser);

        //read + 1 (default no add)
        if (isAddTopicRead) {
            topicService.increaseTopicRead(topicIdInt);
            topicContentPageModelMap.put(ParamConst.READ, (int) topicContentPageModelMap.get(ParamConst.READ) + 1);
        }

        return new ApiJsonDTO().success().model(topicContentPageModelMap);
    }

    /**
     * 获取回复信息
     *
     * @param replyId 回复 id
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @RequestMapping(value = "/topic/reply", method = RequestMethod.GET)
    public ApiJsonDTO getTopicReplyInfo(@RequestParam(value = "replyid", required = false) String replyId) {
        validationService.check(ParamConst.ID, replyId);
        return new ApiJsonDTO().success().model(topicService.getTopicReplyModelMap(Integer.parseInt(replyId)));
    }

    /**
     * 获取热议话题列表
     *
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @RequestMapping(value = "/topics/hot", method = RequestMethod.GET)
    public ApiJsonDTO listTopicsInfo() {
        return new ApiJsonDTO().success().model(topicService.listHotTopics());
    }

    /**
     * 获取首页话题信息列表
     *      - 首页话题基本信息，最新发布，最新回复自动置顶
     *      - 可设置分页，每页显示数量
     *      - limit 参数具备默认值（neubbs.properties 配置文件中可设置）
     *      - 可设置筛选条件（category 和 username）
     *
     * @param limit 每页显示数量
     * @param page 跳转到指定页数
     * @param category 话题分类 id（英文昵称）
     * @param username 用户名
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @RequestMapping(value = "/topics", method = RequestMethod.GET)
    public ApiJsonDTO listHomeTopics(@RequestParam(value = "limit", required = false) String limit,
                                     @RequestParam(value = "page", required = false) String page,
                                     @RequestParam(value = "category", required = false) String category,
                                     @RequestParam(value = "username", required = false) String username) {
        validationService.check(ParamConst.NUMBER, page);
        validationService.checkOnlyNotNullParam(
                ParamConst.NUMBER, limit,
                ParamConst.TOPIC_CATEGORY_NICK, category,
                ParamConst.USERNAME, username
        );

        int limitParam = limit == null ? 0 : Integer.parseInt(limit);
        return new ApiJsonDTO().success()
                .model(topicService.listTopics(limitParam, Integer.parseInt(page), category, username));
    }


    /**
     * 获取话题总页数
     *      - 首页显示，各分类
     *
     * @param limit 每页显示条数
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @RequestMapping(value = "/topics/pages", method = RequestMethod.GET)
    public ApiJsonDTO countTopicTotalPages(@RequestParam(value = "limit", required = false) String limit,
                                           @RequestParam(value = "category", required = false) String category,
                                           @RequestParam(value = "username", required = false) String username) {
        validationService.checkOnlyNotNullParam(
                ParamConst.NUMBER, limit,
                ParamConst.TOPIC_CATEGORY_NICK, category,
                ParamConst.USERNAME, username
        );

        int limitParam = limit == null ? 0 : Integer.parseInt(limit);
        return new ApiJsonDTO().success().model(topicService.countTopicTotalPages(limitParam, category, username));
    }

    /**
     * 获取话题分类信息列表
     *      - 所有分类信息
     *
     * @return ApiJsonDTO 接口 JSON 传输数据
     */
    @RequestMapping(value = "/topics/categorys", method = RequestMethod.GET)
    public ApiJsonDTO listTopicCategories() {
        return new ApiJsonDTO().success().model(topicService.listAllTopicCategories());
    }

    /**
     * 发布话题
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO 接口 JSON 传输数据
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO releaseTopic(@RequestBody Map<String, Object> requestBodyParamsMap) {
        String category = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String title = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String topicContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        validationService.check(ParamConst.TOPIC_CATEGORY_NICK, category)
                         .check(ParamConst.TOPIC_TITLE, title)
                         .check(ParamConst.TOPIC_CONTENT, topicContent);

        UserDO cookieUser = secretService.getUserInfoByAuthentication(httpService.getAuthenticationCookieValue());

        return new ApiJsonDTO().success()
                .model(topicService.saveTopic(cookieUser.getId(), category, title, topicContent));
    }

    /**
     * 发布回复
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/reply", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO releaseTopicReply(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
        String replyContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        validationService.check(ParamConst.ID, String.valueOf(topicId))
                         .check(ParamConst.REPLY_CONTENT, replyContent);

        UserDO cookieUser = secretService.getUserInfoByAuthentication(httpService.getAuthenticationCookieValue());

        return new ApiJsonDTO().success().model(topicService.saveReply(cookieUser.getId(), topicId, replyContent));
    }

    /**
     * 删除话题
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO 接口 Json 传输对象
     */
    @LoginAuthorization @AccountActivation @AdminRank
    @RequestMapping(value = "/topic-remove", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO removeTopic(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        validationService.check(ParamConst.ID, String.valueOf(topicId));

        topicService.removeTopic(topicId);

        return new ApiJsonDTO().success();
    }

    /**
     * 删除回复
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @LoginAuthorization @AccountActivation @AdminRank
    @RequestMapping(value = "/topic/reply-remove", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO removeTopicReply(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);

        validationService.check(ParamConst.ID, String.valueOf(replyId));

        topicService.removeReply(replyId);

        return new ApiJsonDTO().success();
    }

    /**
     * 编辑话题
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic-update", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO updateTopic(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
        String newCategoryNick = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String newTitle = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String newTopicContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        validationService.check(ParamConst.ID, String.valueOf(topicId))
                         .check(ParamConst.TOPIC_CATEGORY_NICK, newCategoryNick)
                         .check(ParamConst.TOPIC_TITLE, newTitle)
                         .check(ParamConst.TOPIC_CONTENT, newTopicContent);

        topicService.alterTopicContent(topicId, newCategoryNick, newTitle, newTopicContent);

        return new ApiJsonDTO().success();
   }

    /**
     * 编辑回复
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
   @LoginAuthorization @AccountActivation
   @RequestMapping(value = "/topic/reply-update", method = RequestMethod.POST, consumes = "application/json")
   public ApiJsonDTO updateTopicReply(@RequestBody Map<String, Object> requestBodyParamsMap) {
       Integer replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);
       String newReplyContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

       validationService.check(ParamConst.ID, String.valueOf(replyId))
                        .check(ParamConst.REPLY_CONTENT, newReplyContent);

       topicService.alterReplyContent(replyId, newReplyContent);

       return new ApiJsonDTO().success();
   }

    /**
     * 点赞话题
     *      - 需要 command 命令（inc - 点赞 | dec - 取消）
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
   @LoginAuthorization @AccountActivation
   @RequestMapping(value = "/topic/like", method = RequestMethod.POST, consumes = "application/json")
   public ApiJsonDTO likeTopic(@RequestBody Map<String, Object> requestBodyParamsMap) {
       Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
       String command = (String) requestBodyParamsMap.get(ParamConst.COMMAND);

       validationService.check(ParamConst.TOPIC_ID, String.valueOf(topicId));
       validationService.checkCommand(command, SetConst.COMMAND_INC, SetConst.COMMAND_DEC);

       UserDO cookieUser = secretService.getUserInfoByAuthentication(httpService.getAuthenticationCookieValue());

       //judge current user like topic, according the command('inc', 'dec'), alter like of topic
       boolean isCurrentUserLikeTopic = userService.isUserLikeTopic(cookieUser.getId(), topicId);

       //record user like topic id array of user action
       userService.operateLikeTopic(cookieUser.getId(), topicId, command);

       int latestTopicLike
               = topicService.alterTopicLikeByCommand(isCurrentUserLikeTopic, topicId, cookieUser.getId(), command);

       return new ApiJsonDTO().success().buildMap(ParamConst.LIKE, latestTopicLike);
   }

    /**
     * 点赞话题（新接口）
     *      - 自动处理用户喜欢话题状态，取反（已喜欢 -> 未喜欢， 未喜欢 -> 已喜欢）
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/newlike", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO newLikeTopic(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        validationService.check(ParamConst.TOPIC_ID, String.valueOf(topicId));

        UserDO cookieUser = secretService.getUserInfoByAuthentication(httpService.getAuthenticationCookieValue());

        Map<String, Object> resultMap = new LinkedHashMap<>(SetConst.SIZE_TWO);
            resultMap.put(ParamConst.USER_LIKE_TOPIC_ID, topicService.operateLikeTopic(cookieUser.getId(), topicId));
            resultMap.put(ParamConst.LIKE, topicService.countTopicContentLike(topicId));
        return new ApiJsonDTO().success().model(resultMap);
    }

    /**
     * 收藏话题
     *      - 自动处理用户喜欢话题状态，取反（已收藏 -> 未收藏， 未收藏 -> 已收藏）
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO　接口 JSON 传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/collect", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO collectTopic(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        validationService.check(ParamConst.TOPIC_ID, String.valueOf(topicId));

        UserDO cookieUser = secretService.getUserInfoByAuthentication(httpService.getAuthenticationCookieValue());

        return new ApiJsonDTO().success().model(topicService.operateCollectTopic(cookieUser.getId(), topicId));
    }

    /**
     * 关注话题
     *
     * @param requestBodyParamsMap request-body 内 JSON 数据
     * @return ApiJsonDTO 接口 JSON 传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/attention", method = RequestMethod.POST, consumes = "application/json")
    public ApiJsonDTO attentionTopic(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        validationService.check(ParamConst.TOPIC_ID, String.valueOf(topicId));

        UserDO cookieUser = secretService.getUserInfoByAuthentication(httpService.getAuthenticationCookieValue());

        return new ApiJsonDTO().success().model(topicService.operateAttentionTopic(cookieUser.getId(), topicId));
    }
}
