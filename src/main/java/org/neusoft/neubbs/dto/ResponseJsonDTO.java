package org.neusoft.neubbs.dto;

import org.neusoft.neubbs.constant.AjaxRequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * response JSON 数据传输对象
 */
public class ResponseJsonDTO {
    private Boolean status;
    private String message;
    private List<Map<String, Object>> model;

    /**
     * Constructor
     */
    public ResponseJsonDTO(){
        this.model = new ArrayList<Map<String, Object>>();
    }

    /**
     * 参数一次注入
     */
    public void put(Boolean status,String message){
        this.status = status;
        this.message = message;
    }


    public void putAjaxSuccess(String message){
        this.status = AjaxRequestStatus.SUCCESS;
        this.message = message;
    }
    public void putAjaxFail(String message){
        this.status = AjaxRequestStatus.FAIL;
        this.message = message;
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
