package com.cambio.ep2.convertidor;

import lombok.Data;

@Data
public class ConvertidorException extends RuntimeException{
    private final int code;

    public ConvertidorException(int codeResponse){
        this.code = codeResponse;
    }
}
