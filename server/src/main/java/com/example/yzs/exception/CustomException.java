package com.example.yzs.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException{
    private String msg;

    public CustomException(String msg){
        this.msg = msg;
    }
}
