package com.nupday.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class MessageUtil {
    public static String getMessage(String template, List params) {
        if (StringUtils.isBlank(template) || CollectionUtils.isEmpty(params)) {
            return template;
        }
        String res = template;
        for (int i = 0; i < params.size(); i ++) {
            res = res.replace("{" + i + "}", params.get(i).toString());
        }
        return res;
    }
}
