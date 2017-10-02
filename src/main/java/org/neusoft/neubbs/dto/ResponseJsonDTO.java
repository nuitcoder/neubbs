package org.neusoft.neubbs.dto;

import java.util.Map;

/**
 * response JSON 数据传输对象
 */
public class ResponseJsonDTO {
    private Boolean status;

    private String message;
    private Map<String,String> model;

    /**
     * 参数一次注入
     *
     * @param status
     * @param message
     */
    public void put(Boolean status,String message){
        this.status = status;
        this.message = message;
    }
    public void put(Boolean ajaxStatus,String message,Map<String,String> model){
        this.status = ajaxStatus;
        this.message = message;
        this.model = model;
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
    public Map<String,String> getModel(){
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
    public void setModel(Map<String,String> model){
        this.model = model;
    }
}
