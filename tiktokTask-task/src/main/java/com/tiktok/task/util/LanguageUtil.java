package com.tiktok.task.util;

import com.tiktok.common.core.redis.RedisCache;

import java.util.ArrayList;
import java.util.List;

import static com.tiktok.common.utils.ParseTitle.iterateObjectFields;

public class LanguageUtil {
    public static Object processObjectWithLanguageSetting(Long uid, Object obj, RedisCache redisCache) {
        String language = "English";

        if (uid != null) {
            // 从Redis中获取用户的语言设置
            String l = redisCache.getCacheObject("user:language:" + uid);
            if (l != null) {
                language = l;
            }
        }

        return iterateObjectFields(obj, language);
    }

    public static  <T> List<T> processListWithLanguageSettingList(Long uid, List<T> objectList, RedisCache redisCache) {
        List<T> processedList = new ArrayList<>();

        for (T obj : objectList) {
            T processedObj = (T) processObjectWithLanguageSetting(uid, obj, redisCache);
            processedList.add(processedObj);
        }

        return processedList;
    }


}
