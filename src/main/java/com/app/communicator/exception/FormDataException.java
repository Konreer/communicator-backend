package com.app.communicator.exception;

import org.springframework.validation.BindingResult;

public class FormDataException extends RuntimeException{
    private BindingResult bindingResult;

    public FormDataException(String message, BindingResult bindingResult){
        super(message);
        this.bindingResult = bindingResult;
    }
}
