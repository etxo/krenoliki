package com.etxo.krenoliki.model;

import lombok.Data;

@Data
public class Game {

    private Long gameId = 0L;
    private Player player1;
    private Player player2;

    private GameState state;
    private Sign[][] gameBoard;
    private Sign winner;

    public void fillBoard() {
        for(int i = 0; i < gameBoard.length; i++) {
            for(int j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = Sign.n;
            }
        }
    }
}
