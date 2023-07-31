package com.etxo.krenoliki.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidGameException extends Exception{
    private String message;
}
