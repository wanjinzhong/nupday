package com.nupday.util;

import com.nupday.constant.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * UrlUtil
 * @author Neil Wan
 * @create 18-8-4
 */
public class UrlUtil {
    public static String getServerPath(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        String scheme = request.getScheme();
        url.append(scheme)
                .append("://")
                .append(request.getServerName());
        Integer port = request.getServerPort();
        Boolean needAppendPort = !((Constants.Http.SCHEMA_HTTP.equalsIgnoreCase(scheme) && Constants.Http.SCHEMA_HTTP_PORT.equals(port)) ||
                (Constants.Http.SCHEMA_HTTPS.equalsIgnoreCase(scheme) && Constants.Http.SCHEMA_HTTPS_PORT.equals(port)));
        if (needAppendPort) {
            url.append(":").append(port);
        }
        url.append(request.getContextPath());
        return url.toString();
    }
}
