package org.neusoft.neubbs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.neusoft.neubbs.constant.account.AccountInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * response JSON 数据传输对对象（api 返回数据格式）
 * 【Data Transfer Object】
 */
public class ResponseJsonDTO {
    private Boolean status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, Object>> model;

    /**
     * Constructor
     */
    public ResponseJsonDTO(){
        this.model = new ArrayList<Map<String, Object>>();
    }
    public ResponseJsonDTO(Boolean status, String message){
        this.status = status;
        this.message = message;
    }
    public ResponseJsonDTO(Boolean status, String message,String key, Object obj){
        this.status = status;
        this.message = message;
        this.model = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
            map.put(key, obj);

        this.model.add(map);
    }
    public ResponseJsonDTO(Boolean status, String message, Map<String, Object> map){
        this.status = status;
        this.message = message;
        this.model = new ArrayList<Map<String, Object>>();

        //密码，权限，激活状态，不显示在页面
        map.remove(AccountInfo.PASSWORD);
        map.remove(AccountInfo.RANK);
        map.remove(AccountInfo.STATE);

        this.model.add(map);
    }

    /**
     *Getter
     */
    public Boolean getStatus(){
        return  status;
    }
    public String getMessage(){
        return message;
    }
    public List<Map<String, Object>> getModel(){
        return model;
    }

    /**
     * Setter
     */
    public void setStatus(Boolean status){
        this.status = status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setModel(List<Map<String, Object>> model){
        this.model = model;
    }
}
