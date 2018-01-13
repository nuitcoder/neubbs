package org.neusoft.neubbs.dto;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.SetConst;

import java.util.HashMap;
import java.util.Map;

/**
 *  Api Result 传输对象
 *
 *  @author Suvan
 */
public class ApiJsonDTO {

    private Boolean success;
    private String message;

    private Object model;

    /**
     * Constructor
     *      - 未知（输入：Ajax 请求状态）
     *      - 失败（输入：错误信息）
     *      - 成功（输入：模型参数）
     *      - 未知（输入：Ajax 请求状态，错误信息，模型参数）
     */
    public ApiJsonDTO() {
       this.success = false;
       this.message = "";
       this.model = new Object();
    }

    public ApiJsonDTO success() {
        this.setSuccess(AjaxRequestStatus.SUCCESS);
        return this;
    }
    public ApiJsonDTO error() {
        this.setSuccess(AjaxRequestStatus.FAIL);
        return this;
    }
    public ApiJsonDTO message(String message) {
        this.setMessage(message);
        return this;
    }
    public ApiJsonDTO map(Object map) {
        this.setModel(map);
        return this;
    }
    public ApiJsonDTO buildMap(String key, Object value) {
        Map<String, Object> tmpMap = new HashMap<>(SetConst.SIZE_ONE);
            tmpMap.put(key, value);
        this.setModel(tmpMap);
        return this;
    }
    public ApiJsonDTO list(Object list) {
        this.model = list;
        return this;
    }


    /**
     * Getter
     */
    public Boolean getSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public Object getModel() {
        return model;
    }

    /**
     * Setter
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setModel(Object model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "PageJsonListDTO{"
                + "success=" + success
                + ", message='" + message + '\''
                + ", model=" + model
                + '}';
    }
}
