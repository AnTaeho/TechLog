package com.example.techlog.slack;

public record RequestInfo(
        String requestUrl,
        String method,
        String remoteAddr
) {
}
