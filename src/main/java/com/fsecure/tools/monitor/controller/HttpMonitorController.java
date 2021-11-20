package com.fsecure.tools.monitor.controller;

import com.fsecure.tools.monitor.service.HttpMonitorService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping({"/", HTTP_MONITOR_PATH})
@RequiredArgsConstructor
public class HttpMonitorController {

    private final HttpMonitorService service;
    private static final Logger logger = getLogger(HttpMonitorController.class);

    @GetMapping
    public String status(Model model) {
        logger.debug("Getting URLs status");
        model.addAttribute("urlsStatus", service.getStatus());
        return HTTP_MONITOR_DASHBOARD;
    }

}