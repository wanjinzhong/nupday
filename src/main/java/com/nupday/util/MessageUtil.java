package com.nupday.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * MessageUtil
 * @author Neil Wan
 * @create 18-8-4
 */
public class MessageUtil {
    public static String getMessage(String template, String ...params) {
        List<String> paramList = newArrayList(params);
        if (StringUtils.isBlank(template) || CollectionUtils.isEmpty(paramList)) {
            return template;
        }
        String res = template;
        for (int i = 0; i < paramList.size(); i ++) {
            res = res.replace("{" + i + "}", paramList.get(i));
        }
        return res;
    }
}
