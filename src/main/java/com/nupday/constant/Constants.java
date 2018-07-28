package com.nupday.constant;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class Constants {
    public static final Integer HASH_ITERATIONS = 2;

    public static final String ACCESS_CODE_PASSWORD = "59adb24ef3cdbe0297f05b395827453f";

    public static final List<String> PIC_SUFFIX = newArrayList("JPG","JPEG","PNG","GIF", "BMP", "SVG");

    public static final String OWNER = "OWNER";

    public static final String VISITOR = "VISITOR";

    public static final String ALBUM_KEY = "album/";

    public static final String ARTICLE_NOTIFICATION_TYPE_NEW = "发布";

    public static final String ARTICLE_NOTIFICATION_TYPE_UPDATE = "更新";

    public static final String ARTICLE_NOTIFICATION_TEMPLATE = "他（她）{0}了一篇文章《{1}》。快去看看吧：{2}";
}
