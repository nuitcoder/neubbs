package org.neusoft.neubbs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

/**
 *  response JSON 列表传输对象
 *
 *  @author Suvan
 */
public class PageJsonListDTO {
    private Boolean success;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, Object>> model;

    /**
     * Constructor
     */
    public PageJsonListDTO(Boolean success, List<Map<String, Object>> listModel) {
        this.success = success;
        this.message = "";
        this.model = listModel;
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
    public List<Map<String, Object>> getModel() {
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
    public void setModel(List<Map<String, Object>> listModel) {
        this.model = listModel;
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
