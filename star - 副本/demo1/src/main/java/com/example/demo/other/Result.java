package com.example.demo.other;

import lombok.Data;

@Data
public class Result {
    private int code;//返回编码200成功，400失败
    private String msg;//成功/失败
    private long total;//记录数
    private Object data;//数据

    private static Result result(int code, String msg, long total, Object data){
        Result res = new Result();
        res.setCode(code);
        res.setMsg(msg);
        res.setTotal(total);
        res.setData(data);
        return res;
    }

  public static Result fail() {
        return result(400,"失败",0L,null);
    }
    public static Result fail(Object data) {
        return result(400,"失败",0L,data);
    }
    public static Result fail(Object data,String msg) {
        return result(400,msg,0L,data);
    }

    public static Result success(Object data) {
        return result(200,"成功",0L,data);
    }
    public static Result success(Object data, long total) {
        return result(200,"成功",total,data);
    }
    public static Result success() {
        return result(200,"成功",0L,null);
    }

}
