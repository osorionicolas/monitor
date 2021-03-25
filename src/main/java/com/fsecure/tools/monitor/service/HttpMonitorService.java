package com.fsecure.tools.monitor.service;

import com.fsecure.tools.monitor.model.UrlStatus;

import java.util.List;

public interface HttpMonitorService {

    List<UrlStatus> getStatus();
}
