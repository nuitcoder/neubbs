package org.neusoft.neubbs.entity.json;

import java.util.Map;

/**
 * LoginJsonDO
 */
public class LoginJsonDO {
    private Boolean status;

    private String message;
    private Map<String,String> model;

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
