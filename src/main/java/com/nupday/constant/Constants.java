package com.nupday.constant;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

/**
 * Constants
 * @author Neil Wan
 * @create 18-8-4
 */
public class Constants {
    public static final Integer HASH_ITERATIONS = 2;

    public static final String ACCESS_CODE_PASSWORD = "59adb24ef3cdbe0297f05b395827453f";

    public static final List<String> PIC_SUFFIX = newArrayList("JPG", "JPEG", "PNG", "GIF", "BMP", "SVG");

    public static final String OWNER = "OWNER";

    public static final String VISITOR = "VISITOR";

    public static final String ALBUM_KEY = "album/";

    public static final String ARTICLE_NOTIFICATION_TYPE_NEW = "发布";

    public static final String ARTICLE_NOTIFICATION_TYPE_UPDATE = "更新";

    public static final String ARTICLE_NOTIFICATION_TEMPLATE = "他（她）{0}了一篇文章《{1}》。快去看看吧：{2}";

    public static final String CHANGE_PASSWORD_EMAIL_SUBJECT = "修改密码";

    public static final String CHANGE_PASSWORD_EMAIL_CONTENT = "您正在请求修改密码，验证码是{0}， 30分钟内有效。";



    public static final Integer VALIDATION_CODE_TRY_TIMES = 5;

    public static final Integer VALIDATION_CODE_EXPIRE_TIME = 30;

    public static final String OS_NAME = "os.name";

    public static final String WINDOWS = "Windows";

    public static class Article {
        public static final Integer PREVIEW_MAX_LENGTH = 300;
    }

    public static class File {
        public static final String FILE_SPLICER = ".";
    }

    public static class Http {
        public static final String UNKNOWN = "unknown";

        public static final String SCHEMA_HTTP = "HTTP";

        public static final Integer SCHEMA_HTTP_PORT = 80;

        public static final String SCHEMA_HTTPS = "HTTPS";

        public static final Integer SCHEMA_HTTPS_PORT = 443;
    }

    public static class User {
        public static final Integer PASSWORD_MIN_LENGTH = 6;

        public static final Integer ACCESS_CODE_MIN_LENGTH = 6;

        public static final Integer NAME_MAX_LENGTH = 4;
    }
}
