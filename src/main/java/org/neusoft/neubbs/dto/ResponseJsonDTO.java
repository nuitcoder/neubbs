package org.neusoft.neubbs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
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
    public ResponseJsonDTO(Boolean status, String message, Map<String, Object> map){
        this.status = status;
        this.message = message;
        this.model = new ArrayList<Map<String, Object>>();

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
