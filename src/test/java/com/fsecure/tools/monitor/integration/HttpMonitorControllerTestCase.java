package com.fsecure.tools.monitor.integration;

import com.fsecure.tools.monitor.model.Status;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.fsecure.tools.monitor.TestDataBuilder.HTTP_ERROR_DESCRIPTION;
import static com.fsecure.tools.monitor.TestDataBuilder.MODEL_ATTR;
import static com.fsecure.tools.monitor.TestDataBuilder.OK_DESCRIPTION;
import static com.fsecure.tools.monitor.TestDataBuilder.SCHEDULED_SLEEP_TIME;
import static com.fsecure.tools.monitor.TestDataBuilder.UNKHOST_ERROR_DESCRIPTION;
import static com.fsecure.tools.monitor.TestDataBuilder.URLS_TO_CHECK_SIZE;
import static com.fsecure.tools.monitor.TestDataBuilder.VIEW_NAME;
import static com.fsecure.tools.monitor.TestDataBuilder.WARNING_DESCRIPTION;
import static com.fsecure.tools.monitor.TestDataBuilder.getErrorUrl404;
import static com.fsecure.tools.monitor.TestDataBuilder.getErrorUrlUnkHost;
import static com.fsecure.tools.monitor.TestDataBuilder.getOkUrlWithRegexValidator;
import static com.fsecure.tools.monitor.TestDataBuilder.getOkUrlWithStringValidator;
import static com.fsecure.tools.monitor.TestDataBuilder.getWarningUrl;
import static com.fsecure.tools.monitor.model.Status.ERROR;
import static com.fsecure.tools.monitor.model.Status.OK;
import static com.fsecure.tools.monitor.model.Status.WARNING;
import static com.fsecure.tools.monitor.utils.Constants.HTTP_MONITOR_PATH;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class HttpMonitorControllerTestCase extends AbstractTestCases {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void status() throws Exception {
        sleep(SCHEDULED_SLEEP_TIME);
        mockMvc.perform(get(HTTP_MONITOR_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name(VIEW_NAME))
                .andExpect(model().attribute(MODEL_ATTR, hasSize(URLS_TO_CHECK_SIZE)))
                .andExpect(model().attribute(MODEL_ATTR, allOf(
                        validationStatements(getOkUrlWithStringValidator().getName(), OK, OK_DESCRIPTION),
                        validationStatements(getOkUrlWithRegexValidator().getName(), OK, OK_DESCRIPTION),
                        validationStatements(getWarningUrl().getName(), WARNING, WARNING_DESCRIPTION),
                        validationStatements(getErrorUrl404().getName(), ERROR, HTTP_ERROR_DESCRIPTION),
                        validationStatements(getErrorUrlUnkHost().getName(), ERROR, UNKHOST_ERROR_DESCRIPTION)
                )));

    }

    private Matcher<Iterable<? super Object>> validationStatements(String name, Status status, String description) {
        return hasItem(
                allOf(
                        hasProperty("name", is(name)),
                        hasProperty("lastStatus", is(status)),
                        hasProperty("description", startsWith(description))
                )
        );
    }
}
