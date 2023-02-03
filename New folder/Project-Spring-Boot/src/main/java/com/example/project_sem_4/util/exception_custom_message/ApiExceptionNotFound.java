package com.example.project_sem_4.util.exception_custom_message;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ApiExceptionNotFound extends RuntimeException{
    private String tableName;
    private String fieldName;
    private Object fieldValue;

    public ApiExceptionNotFound(String tableName, String fieldName, Object fieldValue) {
        super(String.format("Bảng %s không tìm thấy %s: '%s'",tableName, fieldName, fieldValue));
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
