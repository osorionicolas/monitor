package com.fsecure.tools.monitor.service;

import com.fsecure.tools.monitor.model.UrlStatus;
import com.fsecure.tools.monitor.monitors.HttpMonitor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@RequiredArgsConstructor
public class HttpMonitorServiceImpl implements HttpMonitorService {

    private static final Logger logger = getLogger(HttpMonitorServiceImpl.class);
    private final HttpMonitor monitor;

    @Override
    public List<UrlStatus> getStatus() {
        logger.debug("Getting status");
        return monitor.urlsStatus();
    }
}
