package com.etxo.krenoliki.model;

import lombok.Data;

@Data
public class Process {

    private int positionX;
    private int positionY;
    private Player player;
    private Long gameId;
}
