package com.tiktok.common.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;


public class ParseTitle {
    public static String[] parseText(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String chinese = jsonObject.optString("Chinese", null);
            String english = jsonObject.optString("English", null);

            if (chinese != null && english != null) {
                return new String[]{chinese, english};
            } else {
                return null;
            }
        } catch (JSONException e) {
            return null;
        }
    }



    public static Object iterateObjectFields(Object obj,String Language) {
        Class<?> clazz = obj.getClass();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // 设置字段可访问

            String fieldName = field.getName();
            Object value;

            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                System.out.println("Error accessing field " + fieldName);
                continue;
            }

            if (value != null) {
                String[] strings = parseText(value.toString());
                if (strings != null && strings.length >= 2 && strings[0] != null && strings[1] != null) {
                    System.out.println("Field: " + fieldName + ", Value: " + value);
                    try {

                        if(Language.equals("Chinese")){
                            field.set(obj, strings[0]);
                        }else if(Language.equals("English")){

                            field.set(obj, strings[1]);
                        }                    } catch (IllegalAccessException e) {
                        System.out.println("Error setting value for field " + fieldName);
                    }
                }
            }
        }

        return obj;
    }


}
