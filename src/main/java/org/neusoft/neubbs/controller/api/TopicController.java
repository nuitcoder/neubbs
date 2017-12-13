package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ParamConst;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.constant.secret.SecretInfo;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.AdminRank;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.PageJsonDTO;
import org.neusoft.neubbs.dto.PageJsonListDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.service.IParamCheckService;
import org.neusoft.neubbs.service.ISecretService;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Topic api
 *      获取话题信息
 *      获取回复信息
 *      获取话题列表（分页，指定数量）
 *      获取话题总页数
 *      获取话题分类信息
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

    private final IParamCheckService paramCheckService;
    private final ITopicService topicService;
    private final IUserService userService;
    private final IHttpService httpService;
    private final ISecretService secretService;

    @Autowired
    public TopicController(IParamCheckService paramCheckService, ITopicService topicService,
                           IUserService userService, IHttpService httpService,
                           ISecretService secretService) {
        this.paramCheckService = paramCheckService;
        this.topicService = topicService;
        this.userService = userService;
        this.httpService = httpService;
        this.secretService = secretService;
    }


    /**
     * 获取话题信息
     *      - 能够获取当前用户是否点赞该文章信息（访客用户默认为 false）
     *
     * @param topicId 话题id
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    @ResponseBody
    public PageJsonDTO topic(@RequestParam(value = "topicid", required = false) String topicId,
                             HttpServletRequest request) {
        paramCheckService.check(ParamConst.ID, topicId);


        int topicIdInt = Integer.parseInt(topicId);
        Map<String, Object> topicContentPageModelMap = topicService.getTopicContentPageModelMap(topicIdInt);

        //judge current visit user whether to like this topic
        boolean isCurrentUserLikeThisTopic = false;
        String authentication = httpService.getCookieValue(request, ParamConst.AUTHENTICATION);
        if (authentication != null) {
            UserDO currentUser
                    = secretService.jwtVerifyTokenByTokenByKey(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
            isCurrentUserLikeThisTopic = userService.isUserLikeTopic(currentUser.getId(), topicIdInt);
        }

        topicContentPageModelMap.put(ParamConst.CURRENT_USERT_LIKE_TOPIC, isCurrentUserLikeThisTopic);
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, topicContentPageModelMap);
    }

    /**
     * 获取回复信息
     *
     * @param replyId 回复id
     * @return PageJsonDTO 页面JSON传输对象
     */
    @RequestMapping(value = "/topic/reply", method = RequestMethod.GET)
    @ResponseBody
    public PageJsonDTO topicReply(@RequestParam(value = "replyid", required = false) String replyId) {
        paramCheckService.check(ParamConst.ID, replyId);
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS,
                topicService.getReplyPageModelMap(Integer.parseInt(replyId)));
    }

    /**
     * 获取话题列表（分页，指定数量）
     *      - 未输入 limit 参数，使用 neubbs.properties 参数文件内指定的默认值
     *
     * @param limit 每页显示数量
     * @param page 跳转到指定页数
     * @return PageJsonListDTO 响应JSON传输列表对象
     */
    @RequestMapping(value = "/topics", method = RequestMethod.GET)
    @ResponseBody
    public PageJsonListDTO topics(@RequestParam(value = "limit", required = false) String limit,
                                  @RequestParam(value = "page", required = false) String page,
                                  @RequestParam(value = "category", required = false) String category,
                                  @RequestParam(value = "username", required = false) String username) {
        paramCheckService.check(ParamConst.NUMBER, page);

        //judge whether input limit param
        int inputLimit = SetConst.ZERO;
        if (limit != null) {
            paramCheckService.check(ParamConst.NUMBER, limit);
            inputLimit = Integer.parseInt(limit);
        }
        if (category != null) {
            paramCheckService.check(ParamConst.TOPIC_CATEGORY_NICK, category);
        }
        if (username != null) {
            paramCheckService.check(ParamConst.USERNAME, username);
        }

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
    @ResponseBody
    public PageJsonDTO topicsPages(@RequestParam(value = "limit", required = false) String limit,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "username", required = false) String username) {
        //judge whether input limit param
        int inputLimit = SetConst.ZERO;
        if (limit != null) {
            paramCheckService.check(ParamConst.NUMBER, limit);
            inputLimit = Integer.parseInt(limit);
        }
        if (category != null) {
            paramCheckService.check(ParamConst.TOPIC_CATEGORY_NICK, category);
        }
        if (username != null) {
            paramCheckService.check(ParamConst.USERNAME, username);
        }

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS,
                ParamConst.TOTAL_PAGES, topicService.countTopicTotalPages(inputLimit, category, username));
    }

    /**
     * 获取话题分类信息
     *
     * @return ResposneJsonDTO 页面JSON传输数据
     */
    @RequestMapping(value = "/topics/categorys", method = RequestMethod.GET)
    @ResponseBody
    public PageJsonListDTO topicCategorys() {
        return new PageJsonListDTO(AjaxRequestStatus.SUCCESS,  topicService.listAllTopicCategorys());
    }

    /**
     * 发布话题
     *
     * @param requestBodyParamsMap reuest-body内JSON数据
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输数据
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public PageJsonDTO saveTopic(@RequestBody Map<String, Object> requestBodyParamsMap, HttpServletRequest request) {
        String category = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String title = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String topicContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        paramCheckService.check(ParamConst.TOPIC_CATEGORY_NICK, category);
        paramCheckService.check(ParamConst.TOPIC_TITLE, title);
        paramCheckService.check(ParamConst.TOPIC_CONTENT, topicContent);

        //cookie get userid
        String authentication = httpService.getCookieValue(request, ParamConst.AUTHENTICATION);
        UserDO cookieUser
                = secretService.jwtVerifyTokenByTokenByKey(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);

        //database execute insert topic
        int topicId = topicService.saveTopic(cookieUser.getId(), category, title, topicContent);
        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.TOPIC_ID, topicId);
    }

    /**
     * 发布回复
     *
     * @param requetBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return PageJsonDTO 页面JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic/reply", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public PageJsonDTO saveReply(@RequestBody Map<String, Object> requetBodyParamsMap, HttpServletRequest request) {
        Integer topicId = (Integer) requetBodyParamsMap.get(ParamConst.TOPIC_ID);
        String replyContent = (String) requetBodyParamsMap.get(ParamConst.CONTENT);

        paramCheckService.check(ParamConst.ID, String.valueOf(topicId));
        paramCheckService.check(ParamConst.REPLY_CONTENT, replyContent);

        //cookie get userid
        String authentication = httpService.getCookieValue(request, ParamConst.AUTHENTICATION);
        UserDO cookieUser
                = secretService.jwtVerifyTokenByTokenByKey(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);

        //database execute insert topic
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
    @ResponseBody
    public PageJsonDTO topicRemove(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);

        paramCheckService.check(ParamConst.ID, String.valueOf(topicId));

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
    @ResponseBody
    public PageJsonDTO topicReplyRemove(@RequestBody Map<String, Object> requestBodyParamsMap) {
        int replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);

        paramCheckService.check(ParamConst.ID, String.valueOf(replyId));

        topicService.removeReply(replyId);

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS);
    }

    /**
     * 修改话题内容
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return PageJsonDTO 页面JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/topic-update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public PageJsonDTO topicContentUpdate(@RequestBody Map<String, Object> requestBodyParamsMap) {
        Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
        String newCategoryNick = (String) requestBodyParamsMap.get(ParamConst.CATEGORY);
        String newTitle = (String) requestBodyParamsMap.get(ParamConst.TITLE);
        String newTopicContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

        paramCheckService.check(ParamConst.ID, String.valueOf(topicId));
        paramCheckService.check(ParamConst.TOPIC_CATEGORY_NICK, newCategoryNick);
        paramCheckService.check(ParamConst.TOPIC_TITLE, newTitle);
        paramCheckService.check(ParamConst.TOPIC_CONTENT, newTopicContent);

        topicService.alterTopicContent(topicId, newCategoryNick, newTitle, newTopicContent);

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS);
   }

    /**
     * 修改回复内容
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @return PageJsonDTO 页面JSON传输对象
     */
   @LoginAuthorization @AccountActivation
   @RequestMapping(value = "/topic/reply-update", method = RequestMethod.POST, consumes = "application/json")
   @ResponseBody
   public PageJsonDTO topicReplyContentUpdate(@RequestBody Map<String, Object> requestBodyParamsMap) {
       Integer replyId = (Integer) requestBodyParamsMap.get(ParamConst.REPLY_ID);
       String newReplyContent = (String) requestBodyParamsMap.get(ParamConst.CONTENT);

       paramCheckService.check(ParamConst.ID, String.valueOf(replyId));
       paramCheckService.check(ParamConst.REPLY_CONTENT, newReplyContent);

       topicService.alterReplyContent(replyId, newReplyContent);

       return new PageJsonDTO(AjaxRequestStatus.SUCCESS);
   }

    /**
     * 修改话题点赞数量
     *
     * @param requestBodyParamsMap request-body内JSON数据
     * @param request http请求
     * @return PageJsonDTO 页面传输对象
     */
   @LoginAuthorization @AccountActivation
   @RequestMapping(value = "/topic/like", method = RequestMethod.POST, consumes = "application/json")
   @ResponseBody
   public PageJsonDTO topicLike(@RequestBody Map<String, Object> requestBodyParamsMap, HttpServletRequest request) {
       Integer topicId = (Integer) requestBodyParamsMap.get(ParamConst.TOPIC_ID);
       String command = (String) requestBodyParamsMap.get(ParamConst.COMMAND);

       paramCheckService.check(ParamConst.TOPIC_ID, String.valueOf(topicId));
       paramCheckService.checkInstructionOfSpecifyArray(command, SetConst.INC, SetConst.DEC);

       //get cookie user id
       String authentication = httpService.getCookieValue(request, ParamConst.AUTHENTICATION);
       UserDO cookieUser
               = secretService.jwtVerifyTokenByTokenByKey(authentication, SecretInfo.JWT_TOKEN_LOGIN_SECRET_KEY);
       int userId = cookieUser.getId();

       //alter topic like
       boolean isCurrentUserLikeTopic = userService.isUserLikeTopic(userId, topicId);
       int currentTopicLike = topicService.alterTopicLikeByInstruction(isCurrentUserLikeTopic, topicId, command);

       //alter user action
       userService.alterUserActionLikeTopicIdArray(userId, topicId, command);

       //return database latest topic like
       return new PageJsonDTO(AjaxRequestStatus.SUCCESS, ParamConst.LIKE, currentTopicLike);
   }
}
