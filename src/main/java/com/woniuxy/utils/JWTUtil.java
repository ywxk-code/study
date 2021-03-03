package com.woniuxy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

public class JWTUtil {
    //设置要过期的时间
    private static final Long EXPIRE_TIME=6*60*60*1000L;
    //设置随机签名
    private static String SIGN="qwertyuiasdfghjk";
    //将创建的jwt和解析jwt的代码抽取出来，实现代码的复用
    //创建jwt
    public static String createToken(HashMap<String,String> map){
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        builder.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRE_TIME));
        String token = builder.sign(Algorithm.HMAC256(SIGN));
        return token;
    }
    //解析jwt
    public static DecodedJWT decodedToken(String token){
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
    //验证token
    public static boolean verify(String token,String username){
        return decodedToken(token).getClaim("username").asString().equals(username);
    }
}
