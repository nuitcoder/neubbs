package org.neusoft.neubbs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.neusoft.neubbs.constant.account.AccountInfo;
import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * response JSON 数据传输对对象（api 返回数据格式）
 * 【Data Transfer Object】
 */
public class ResponseJsonDTO {
    private Boolean success;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, Object>> model;

    /**
     * Constructor
     */
    public ResponseJsonDTO(){
        this.model = new ArrayList<Map<String, Object>>();
    }
    public ResponseJsonDTO(Boolean success, String message){
        this.success = success;
        this.message = message;
    }
    public ResponseJsonDTO(Boolean success, String message,String key, Object obj){
        this.success = success;
        this.message = message;
        this.model = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
            map.put(key, obj);

        this.model.add(map);
    }
    public ResponseJsonDTO(Boolean success, String message, Map<String, Object> map){
        this.success = success;
        this.message = message;
        this.model = new ArrayList<Map<String, Object>>();

        //密码，权限，激活状态，不显示在页面
        map.remove(AccountInfo.PASSWORD);
        map.remove(AccountInfo.RANK);
        map.remove(AccountInfo.STATE);

        this.model.add(map);
    }

    /**
     * 输出错误信息
     * @param errorInfo
     */
    public ResponseJsonDTO putAjaxFail(String errorInfo){
        this.success = AjaxRequestStatus.FAIL;
        this.message = errorInfo;

        return this;
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
    public List<Map<String, Object>> getModel(){
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
    public void setModel(List<Map<String, Object>> model){
        this.model = model;
    }
}
