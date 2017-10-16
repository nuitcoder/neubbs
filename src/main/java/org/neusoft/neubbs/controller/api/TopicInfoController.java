package org.neusoft.neubbs.controller.api;

import org.apache.log4j.Logger;
import org.neusoft.neubbs.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Topic api
 */
@Controller
@RequestMapping("/api/topic")
public class TopicInfoController {

    @Autowired
    ITopicService topicService;

    private static Logger logger = Logger.getLogger(TopicInfoController.class);
}
