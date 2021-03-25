package com.fsecure.tools.monitor.controller;

import com.fsecure.tools.monitor.service.HttpMonitorService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.fsecure.tools.monitor.utils.Constants.HTTP_MONITOR_DASHBOARD;
import static com.fsecure.tools.monitor.utils.Constants.HTTP_MONITOR_PATH;
import static org.slf4j.LoggerFactory.getLogger;


@Controller
@RequestMapping(HTTP_MONITOR_PATH)
public class HttpMonitorController {

    private static final Logger logger = getLogger(HttpMonitorController.class);

    @Autowired
    private HttpMonitorService service;

    @GetMapping
    public String status(Model model) {
        logger.debug("Getting URLs status");
        model.addAttribute("urlsStatus", service.getStatus());
        return HTTP_MONITOR_DASHBOARD;
    }

}