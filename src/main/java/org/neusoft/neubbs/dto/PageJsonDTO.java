package org.neusoft.neubbs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * response JSON 单个Map 数据对象（api 返回数据格式）
 * 【Data Transfer Object】
 *
 * @author Suvan
 */
public class PageJsonDTO {
    private Boolean success;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> model = new LinkedHashMap<>();

    /**
     * Constructor
     */
    public PageJsonDTO(boolean success) {
        this.success = success;
        this.message = "";
    }
    public PageJsonDTO(boolean success, Map<String, Object> map) {
        this.success = success;
        this.message = "";

        this.model = null;
        this.model = map;
    }
    public PageJsonDTO(boolean success, String key, Object obj) {
        this.success = success;
        this.message = "";

        this.model.put(key, obj);
    }
    public PageJsonDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public PageJsonDTO(boolean success, String message, String key, Object obj) {
        this.success = success;
        this.message = message;

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
        return "PageJsonDTO{"
                + "success=" + success
                + ", message='" + message + '\''
                + ", model=" + model
                + '}';
    }
}
