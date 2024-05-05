package com.picpaysimplificado.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends PicPayException{

    public ForbiddenException(String message) {
        super(message);
    }
}
