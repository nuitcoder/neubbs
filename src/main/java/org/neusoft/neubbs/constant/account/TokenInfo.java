package org.neusoft.neubbs.constant.account;

/**
 *  Token 信息
 */
public interface TokenInfo {
    String JWT = "JWT";
    String HS256 = "HS256";
    String RS256 = "RS256";

    String HEADER = "Header";       //头部
    String HEADER_TYP = "typ";      //类型
    String HEADER_ALG = "alg";      //加密算法

    String PAYLOAD = "Payload";     //载荷
    String PLAYLOAD_ISS = "iss";    //签发者（JWT签发）
    String PLAYLOAD_SUB = "sub";    //面向用户（JWT所面向）
    String PLAYLOAD_AUD = "aud";    //接收者（接收JWT）
    String PLAYLOAD_IAT = "iat";    //签发时间
    String PLAYLOAD_EXP = "exp";    //过期时间

    String SIGNATURE = "Signature"; //签名

    String SET_ISSUER = "neubbs";
    String SET_SUBJECT =  "www.neubbs.com";
    String SET_AUDIENCE = "count@neubbs.com";
    String SET_ISSUEDAT = "";
    String SET_EXPIRESAT = "";

    //Claim参数
    String CLAIM_NAME = "name";
    String CLAIM_ID = "id";
    String CLAIN_RANK = "rank";

    Long EXPIRETIME_SERVEN_DAY = 604800000L; //时间：1000 * 60 * 60 * * 24 * 7 ms

    String AUTHENTICATION = "Authentication"; //Cookie 名和 response 的 Header
    String ADMIN_RANK = "admin";

    //token 密钥
    String SECRET_KEY = "this neubbs is best";

    //过期提示信息
    String TOKEN_ALREAD_EXPIRE = "客户端 token，已经过期，请重新的登录";
}