package com.s1mple.minischool.utils;

public class RedisKeyUtils {

    public static final String MAP_KEY_USER_PRAISE = "USER_PRAISE";

    public static final String MAP_KEY_USER_PRAISE_COUNT = "USER_PRAISE_COUNT";

    public static String getPraiseKey(Long user_id,Long trends_id){
        StringBuilder builder = new StringBuilder();
        builder.append(user_id).append("::").append(trends_id);
        return builder.toString();
    }

}
