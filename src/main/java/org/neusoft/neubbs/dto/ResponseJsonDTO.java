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
    private boolean success;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> model;

    /**
     * Constructor
     *      1.仅返回成功状态（message = null）
     *      2.返回成功状态 + 提示信息
     *      3.返回成功状态（message = null） + 响应数据
     *      4.返回成功状态（message =null） + 响应数据（适用单条 key-value 情况）
     */
    public ResponseJsonDTO(Boolean success) {
        this.success = success;
    }
    public ResponseJsonDTO(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public ResponseJsonDTO(Boolean success, Map<String, Object> map) {
        this.success = success;
        this.model = map;
    }
    public ResponseJsonDTO(Boolean success, String key, Object obj) {
        this.success = success;

        this.model = new HashMap<>();
        this.model.put(key, obj);
    }

    /**
     *Getter
     */
    public Boolean getSuccess() {
        return  success;
    }
    public String getMessage() {
        return message;
    }
    public Map<String, Object> getModel() {
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
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "ResponseJsonDTO{"
                + "success=" + success
                + ", message='" + message + '\''
                + ", model=" + model
                + '}';
    }
}
