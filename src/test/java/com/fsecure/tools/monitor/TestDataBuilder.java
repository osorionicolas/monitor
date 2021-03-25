package com.fsecure.tools.monitor;

import com.fsecure.tools.monitor.model.Status;
import com.google.common.collect.Lists;

import java.util.List;

import static com.fsecure.tools.monitor.model.Status.ERROR;
import static com.fsecure.tools.monitor.model.Status.OK;
import static com.fsecure.tools.monitor.model.Status.WARNING;

public class TestDataBuilder {

   public static final String WIKI_ARG_URL = "https://en.wikipedia.org/wiki/Argentina";
   public static final String HTTP_404_URL = "https://google.com/404page";
   public static final String UNKNOWN_HOST_URL = "https://google.com.fake";
   public static final String UNKNOWN_HOST_ERROR_MSG = "I/O error on GET request for \"https://google.com.fake\": google.com.fake; nested exception is java.net.UnknownHostException: google.com.fake";

   public static final String MONITOR_HTTP_DEFAULTCHECKPERIOD = "60000";
   public static final String MONITOR_HTTP_FILE_FORMAT = ".json";

   public static final Integer URLS_TO_CHECK_SIZE = 5;
   public static final String URL_FILE_PATH = "${user.dir}/src/test/resources/urls.json";

   public static List<String> URLS = Lists.newArrayList("https://en.wikipedia.org/wiki/Argentina", "https://en.wikipedia.org/wiki/Finland", "https://en.wikipedia.org/wiki/F-Secure", "http://localhost:8080/fake_page", "https://google.com.fake");
   public static List<Status> URLS_STATUS = Lists.newArrayList(OK, OK, WARNING, ERROR, ERROR);

   public static final Integer SCHEDULED_SLEEP_TIME = 30000;

}