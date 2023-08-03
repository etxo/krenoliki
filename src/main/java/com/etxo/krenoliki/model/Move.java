package com.etxo.krenoliki.model;

import lombok.Data;

@Data
public class Move {

    private int xPosition;
    private int yPosition;
    private Player player;
    private Long gameId;
}
