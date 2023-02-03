package com.example.project_sem_4.util.exception_custom_message;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ApiExceptionCustomNotFound extends RuntimeException{
    private String message;

    public ApiExceptionCustomNotFound(String message) {
        super(message);
        this.message = message;
    }
}
