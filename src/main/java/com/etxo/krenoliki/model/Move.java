package com.etxo.krenoliki.model;

import lombok.Data;

@Data
public class Move {

    private int positionX;
    private int positionY;
    Player player;
    private Long gameId;
}
