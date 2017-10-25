package org.neusoft.neubbs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * response JSON 单个Map 数据对象（api 返回数据格式）
 * 【Data Transfer Object】
 *
 * @author Suvan
 */
public class ResponseJsonDTO {
    private Boolean success;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> model;

    /**
     * Constructor
     */
    public ResponseJsonDTO(Boolean success){  //请求成功，则返回 true，message = null
        this.success = success;
    }
    public ResponseJsonDTO(Boolean success, String message){ //失败，发回提示信息
        this.success = success;
        this.message = message;
    }
    public ResponseJsonDTO(Boolean success, Map<String, Object> map){ //成功获取到数据不需要提示信息
        this.success = success;
        this.model = map;

        filterUserInfo(map);
    }
    public ResponseJsonDTO(Boolean success, String key, Object obj){
        this.success = success;

        this.model = new HashMap<>();
        this.model.put(key, obj);
    }

    /**
     *Getter
     */
    public Boolean getSuccess(){
        return  success;
    }
    public String getMessage(){
        return message;
    }
    public Map<String, Object> getModel(){
        return model;
    }

    /**
     * Setter
     */
    public void setSuccess(Boolean success){
        this.success = success;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setModel(Map<String, Object> model){
        this.model = model;
    }

    /**
     * 过滤用户信息
     *
     * @param map UserDO 对象的 map
     */
    private void filterUserInfo(Map map){
        map.remove("id");
        map.remove("password");
        map.remove("image");
        map.remove("rank");
        map.remove("state");
        map.remove("createtime");
    }

    @Override
    public String toString() {
        return "ResponseJsonDTO{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", model=" + model +
                '}';
    }
}
