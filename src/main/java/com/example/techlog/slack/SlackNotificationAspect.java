package com.example.techlog.slack;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Aspect
@Component
public class SlackNotificationAspect {

    private final SlackApi slackApi;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final Environment environment;

    public SlackNotificationAspect(SlackApi slackApi, ThreadPoolTaskExecutor threadPoolTaskExecutor, Environment environment) {
        this.slackApi = slackApi;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.environment = environment;
    }

    @Around(value = "@annotation(com.example.techlog.slack.SlackNotification) && args(ex, request)", argNames = "proceedingJoinPoint,ex,request")
    public Object slackNotification(ProceedingJoinPoint proceedingJoinPoint, Exception ex, WebRequest request) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        HttpServletRequest httpServletRequest =
                ((ServletWebRequest)request).getRequest();

        RequestInfo requestInfo = new RequestInfo(
                httpServletRequest.getRequestURL().toString(),
                httpServletRequest.getMethod(),
                httpServletRequest.getRemoteAddr()
        );

        threadPoolTaskExecutor.execute(() -> sendSlackMessage(requestInfo, ex));

        return result;
    }

    public void sendSlackMessage(RequestInfo requestInfo, Exception e) {
        SlackMessage slackMessage = constructSlackMessage(requestInfo, e);
        slackApi.call(slackMessage);
    }

    private SlackMessage constructSlackMessage(RequestInfo requestInfo, Exception e) {
        SlackAttachment slackAttachment = constructSlackAttachment(requestInfo, e);
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setAttachments(Collections.singletonList(slackAttachment));
        LocalDateTime now = LocalDateTime.now();
        int time = now.getHour();
        int minute = now.getMinute();
        slackMessage.setText(String.format(
                        """
                        후-후- 당직사관이 전파한다. 현재시각 %d시 %d분 중대한 예외가 발생했다.
                        각 분대별 1명씩 상황 파악해서 보고할 수 있도록.
                       
                        다시한번 전파한다.
                       
                        현재시각 %d시 %d분 중대한 예외가 발생했다.
                        각 분대별 1명씩 상황 파악해서 보고할 수 있도록.
                       """,
                time, minute, time, minute));
        slackMessage.setUsername("당직사관");
        return slackMessage;
    }

    private SlackAttachment constructSlackAttachment(RequestInfo requestInfo, Exception e) {
        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.setFallback("Error");
        slackAttachment.setColor("danger");
        slackAttachment.setFields(Arrays.asList(
                constructSlackField("Exception class", e.getClass().getCanonicalName()),
                constructSlackField("예외 메시지", e.getMessage() != null ? e.getMessage() : ""),
                constructSlackField("Request URL", requestInfo.requestUrl()),
                constructSlackField("Request Method", requestInfo.method()),
                constructSlackField("요청 시간", currentTime()),
                constructSlackField("Request IP", requestInfo.remoteAddr()),
                constructSlackField("Profile 정보", Arrays.toString(environment.getActiveProfiles()))
        ));
        return slackAttachment;
    }

    private SlackField constructSlackField(String title, String value) {
        SlackField slackField = new SlackField();
        slackField.setTitle(title);
        slackField.setValue(value);
        return slackField;
    }

    private String currentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

