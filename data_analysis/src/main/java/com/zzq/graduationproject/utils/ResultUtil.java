package com.zzq.graduationproject.utils;

import com.zzq.graduationproject.model.Result;
import com.zzq.graduationproject.model.ResultCode;

/**
 * @author zzq
 */
public class ResultUtil {

    public static Result success() {
        return success(null);
    }

    public static Result success(String msg) {
        return success(msg, null);
    }

    public static Result success(String msg, Object data) {
        return new Result(ResultCode.SUCCESS.getCode(), msg, data);
    }


    public static Result error(int code) {
        return error(code, null);
    }

    public static Result error(int code, String msg) {
        return error(code, msg, null);
    }

    public static Result error(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result errorUserNotLogin() {
        return error(ResultCode.USER_NOT_LOGIN.getCode(), ResultCode.USER_NOT_LOGIN.getDescription());
    }
}
