package com.etxo.krenoliki.model;

import lombok.Data;
@Data
public class Game {

    //private final int START_SIZE = 15;
    public static Long idCounter = 0L;
    private Long gameId;

    private Player playerOne;
    private Player playerTwo;

    private GameState state;
    private Sign[][] gameBoard;
    private Sign winner;

    /*public Game(Sign[][] gameBoard){
        this.gameBoard = new Sign[START_SIZE][START_SIZE];
    }*/
}
