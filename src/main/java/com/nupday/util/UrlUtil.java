package com.nupday.util;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {
    public static String getServerPath(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        String scheme = request.getScheme();
        url.append(scheme)
                .append("://")
                .append(request.getServerName());
        Integer port = request.getServerPort();
        if (!(("HTTP".equalsIgnoreCase(scheme) && port == 80) ||
                ("HTTPS".equalsIgnoreCase(scheme) && port == 443))) {
            url.append(":").append(port);
        }
        url.append(request.getContextPath());
        return url.toString();
    }
}
