package com.fsecure.tools.monitor.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UrlStatus {

    private String name;
    private Status lastStatus;
    private String url;
    private Long lastResponseTime;
    private String lastCheckTime;
    private String description;

    public UrlStatus(String name, String url){
        this.name = name;
        this.url = url;
    }
}
