package com.picpaysimplificado.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends PicPayException{

    public NotFoundException(String message) {
        super(message);
    }
}
