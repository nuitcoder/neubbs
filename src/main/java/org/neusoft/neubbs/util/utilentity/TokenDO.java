package org.neusoft.neubbs.util.utilentity;

/**
 * Token 实体类
 */
public class TokenDO {
    private String tokenname;
    private Long expireTime;

    private String token;

    /**
     * Getter
     */
    public String getTokenname(){
        return tokenname;
    }
    public Long getExpireTime(){
        return expireTime;
    }
    public String getToken(){
        return token;
    }

    /**
     * Setter
     */
    public void setTokenname(String tokenname){
        this.tokenname = tokenname;
    }
    public void setExpireTime(Long expireTime){
        this.expireTime = expireTime;
    }
    public void setToken(String token){
        this.token = token;
    }
}
