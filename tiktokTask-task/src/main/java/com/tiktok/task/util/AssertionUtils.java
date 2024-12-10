package com.tiktok.task.util;

import com.tiktok.framework.web.domain.ResultCode;
import com.tiktok.framework.web.exception.CustomException;
import com.tiktok.framework.web.exception.ErrorCodeException;

public class AssertionUtils {
    public static void isTrue(boolean condition,String message) {
        if (!condition) {
            throw new ErrorCodeException(ResultCode.CUSTOMER_SERVICE_ERROR,message);

        }
    }
}
