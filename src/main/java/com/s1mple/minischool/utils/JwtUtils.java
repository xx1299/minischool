package com.s1mple.minischool.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.s1mple.minischool.domain.po.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {


    private  static final int keepTime = 1000 * 60 * 60;
    private static final String Secret = "6820915642642218681L";
    private static final String userId = "userId";
    private static final String sessionKey = "sessionKey";
    private static final String openid = "openid";
    private static final String state = "state";
//    @Value("${jwt.keepTime}")
//    private int keepTime;
//    @Value("${jwt.Secret}")
//    private String Secret;
//    @Value("${jwt.userId}")
//    private String userId;
//    @Value("${jwt.sessionKey}")
//    private String sessionKey;
//    @Value("${jwt.openid}")
//    private String openid;

    /**
     * 创建token
     * @param user 用户对象
     * @return token凭证
     */
    public static String createToken(User user){
        long now=System.currentTimeMillis();
        Date date = new Date(now);
        long exp=now+keepTime;
        Date expdate = new Date(exp);
        Map<String,Object> headerMap = new HashMap<String, Object>();
        headerMap.put("typ","jwt");
        headerMap.put("alg","HS256");
        String token = JWT.create().withHeader(headerMap)
                .withClaim(userId,user.getUser_id())
                .withClaim(sessionKey,user.getSession_key())
                .withClaim(openid,user.getOpenid())
                .withClaim(state,user.getState())
                .withIssuedAt(date)
                .withExpiresAt(expdate)
                .sign(Algorithm.HMAC256(Secret));
        return token;
    }

    public static DecodedJWT getClaims(String token){
        DecodedJWT decode = JWT.decode(token);
        return decode;
    }

    /**
     * 验证token
     * @param token 凭证
     * @return token是否过期 false为过期，true为未过期
     */
    public static boolean verifyToken(String token){
        DecodedJWT claims = getClaims(token);
        Claim claim = claims.getClaim(userId);
        Date exp = claims.getExpiresAt();
        if (exp.after(new Date())){
            return true;
        }
        return false;
    }

}
