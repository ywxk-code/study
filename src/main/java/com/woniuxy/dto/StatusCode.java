package com.woniuxy.dto;

public class StatusCode {
    public static final int OK = 20000;//成功
    public static final int ERROR = 20001;//失败
    public static final int UNKNOWNACCOUNT = 20002;//用户未注册
    public static final int INCORRECTCREDENTIALS = 20003;//用户名或密码错误
    public static final int ACCOUNTEXISTS = 20004;//用户未注册,转换大小写快捷键ctrl+shift+u
    public static final int NULLPOINTEXCEPTION= 20005;//空指针异常
    public static final int EXCEPTION = 20006;//服务器异常
}
