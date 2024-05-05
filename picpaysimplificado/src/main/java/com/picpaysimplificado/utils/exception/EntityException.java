package com.picpaysimplificado.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EntityException extends PicPayException{

    public EntityException(String message) {
        super(message);
    }
}
