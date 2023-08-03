package com.etxo.krenoliki.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameNotFoundException extends Exception{
    private String message;
}
