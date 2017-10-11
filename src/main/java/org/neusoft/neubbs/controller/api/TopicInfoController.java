package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.AjaxRequestStatus;
import org.neusoft.neubbs.constant.TopicInfo;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.ResponseJsonDTO;
import org.neusoft.neubbs.entity.TopicDO;
import org.neusoft.neubbs.service.ITopicService;
import org.neusoft.neubbs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Topic 控制器
 */
@Controller
@RequestMapping("/api")
public class TopicInfoController {

    @Autowired
    ITopicService topicService;

    /**
     * 输入 userid,category,title 保存主题
     * @param userid
     * @param title
     * @param category
     * @return
     * @throws Exception
     */
    @LoginAuthorization
    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDTO topic(@RequestParam(value = "userid")Integer userid,
                                 @RequestParam(value = "category")String category,
                                 @RequestParam(value = "title")String title) throws Exception{
       if (userid == null) {
           return new ResponseJsonDTO(AjaxRequestStatus.FAIL, TopicInfo.SAVE_TOPIC_ID_NONULL);
       }
       if (category == null || category.length() == 0){
           return new ResponseJsonDTO(AjaxRequestStatus.FAIL, TopicInfo.SAVE_TOPIC_CATEGORY_NONULL);
       }
       if (title == null || title.length() == 0) {
           return new ResponseJsonDTO(AjaxRequestStatus.FAIL, TopicInfo.SAVE_TOPIC_TITLE_NONULL);
       }

       TopicDO topic = new TopicDO();
            topic.setUserid(userid);
            topic.setTitle(title);
            topic.setCategory(category);

        //保存主题
        topicService.saveTopic(topic);
        if(topic.getId() == null){
            return new ResponseJsonDTO(AjaxRequestStatus.FAIL, TopicInfo.SAVE_TOPIC_FAIL);
        }

        //重新新id查询
        int newId = topic.getId();
        topic = topicService.getTopicById(newId);
        Map<String, Object> topicMap = JsonUtils.getMapByObject(topic);

        return new ResponseJsonDTO(AjaxRequestStatus.FAIL, TopicInfo.SAVE_TOPIC_SUCCESS, topicMap);
    }
}
