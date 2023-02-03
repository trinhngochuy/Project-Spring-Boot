package com.example.project_sem_4.util.exception_custom_message;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ApiExceptionBadRequest extends RuntimeException{
        private String tableName;
        private String fieldName;
        private Object fieldValue;

    public ApiExceptionBadRequest(String tableName, String fieldName, Object fieldValue) {
        super(String.format("Bảng %s lỗi nhập trường %s: '%s'",tableName, fieldName, fieldValue));
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
