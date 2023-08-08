package com.etxo.krenoliki.service;

import com.etxo.krenoliki.exceptions.GameNotFoundException;
import com.etxo.krenoliki.exceptions.InvalidGameException;
import com.etxo.krenoliki.model.*;
import com.etxo.krenoliki.storage.GameStorage;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.Arrays;

import static com.etxo.krenoliki.model.GameState.*;

@Service
@NoArgsConstructor
public class GameService {
    public Game setGame(Player player){

        int FIELD_SIZE = 15; //default board size, should be enough for a usual game
        Game game = new Game();
        game.setGameBoard(new Sign[FIELD_SIZE][FIELD_SIZE]);
        fillBoard(game.getGameBoard());
        game.setGameId(Game.idCounter + 1L);
        game.setPlayerOne(player);
        game.setState(NEW);
        GameStorage.getInstance().addGame(game);
        return game;
    }
    public Sign[][] fillBoard(Sign[][] board) {
        //filling the board with 'n' as long as there is no frontend.
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = Sign.n;
            }
        }
        return board;
    }

    public Game connectToGame (Player playerTwo, Long gameId) {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParameterException("the game does not exist!");
        }

        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game.getPlayerTwo() != null) {
            throw new InvalidParameterException("this game has already been played");
        }

        game.setPlayerTwo(playerTwo);
        game.setState(RUNNING);
        GameStorage.getInstance().addGame(game);

        return game;
    }

    public Sign[][] increaseBoardRightDown(Sign[][] board){

        Sign[][] newBoard = new Sign[board.length +3][board.length +3];

        for(int i = 0; i < board.length; i++) {
            newBoard [i] = Arrays.copyOf(board[i], board.length + 3);
        }
        return newBoard;
    }

    public Sign[][] increaseBoardLeftUp(Sign[][] board){

        Sign[][] newBoard = new Sign[board.length +3][board.length +3];

        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++){
                newBoard[i + 3][j + 3] = board[i][j];
            }
        }
        return newBoard;
    }
    public boolean checkWinner (Sign[][] board, Sign sign){

        return checkRowsAndColumns(board, sign) || checkDiagonal(board, sign);
    }

    private boolean checkRowsAndColumns(Sign[][] board, Sign sign) throws IndexOutOfBoundsException{

        for(int i = 0; i < board.length; i++){

            int countRows5 = 1;
            int countColumns5 =1;

            for(int j = 1; j < board[i].length; j++){
                //checks the rows
                if(board[i][j] == board[i][j-1] && board[i][j] == sign){
                    countRows5++;
                }else{
                    countRows5 = 0;
                }

                if(countRows5 == 5){
                    return true;
                }
                //check the columns
                if(board[j][i] == board[j-1][i] && board[i][j] == sign){
                    countColumns5++;
                }else{
                    countColumns5 = 0;
                }

                if(countColumns5 == 5){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDiagonal (Sign[][] board, Sign sign) throws IndexOutOfBoundsException {

        int boardSize = board.length;
        for(int k = 4; k < boardSize; k++){
            //go from right to left

            int count5 = 0;
            for(int i = 1, j = k-1; j >= 0; i++,  j--){
                if(board[i][j] == sign && board[i-1][j+1] == sign){
                    count5++;
                }else{
                    count5 = 0;
                }
                if(count5 == 5){
                    return true;
                }
            }
            for(int i = 1, j = boardSize - k; j < boardSize; j++, i++){
                //go from left to right
                if(board[i][j] == sign && board[i-1][j-1] == sign){
                    count5++;
                }else{
                    count5 = 0;
                }
                if(count5 == 5){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDiagonalFromLeftToRight (Sign[][] board, Sign sign) throws IndexOutOfBoundsException {

        int boardSize = board.length;
        for (int k = 1; k <= boardSize - 4; k++) {

            int count5ToTheRight = 1;
            int count5ToTheLeft = 1;
            for (int i = 1, j = k; j < boardSize; i++, j++) {
                if (board[i][j] == sign && board[i - 1][j - 1] == sign) {
                    count5ToTheRight++;
                    System.out.println(count5ToTheRight);

                } else {
                    count5ToTheRight = 1;
                }
                if (board[j][i] == sign && board[j - 1][i - 1] == sign) {
                    count5ToTheLeft++;

                } else {
                    count5ToTheLeft = 1;
                }
                if (count5ToTheLeft == 5 || count5ToTheRight == 5)
                    return true;
            }
        }
        return false;
    }

    public boolean checkDiagonalFromRightToLeft (Sign[][] board, Sign sign) throws IndexOutOfBoundsException {

        int boardSize = board.length;
        for (int k = boardSize - 1; k >= 0; k--) {

            int count5ToTheLeft = 1;
            int count5ToTheRight = 1;
            for (int i = 1, j = k - 1; j >= 0; i++, j--) {
                if (board[i][j] == sign && board[i - 1][j + 1] == sign) {
                    count5ToTheLeft++;
                } else {
                    count5ToTheLeft = 1;
                }
            }if (count5ToTheLeft == 5)
                return true;
            for (int i = boardSize - k, j = boardSize - 2; i >= 0; i++, j--) {
                if (board[i][j] == sign && board[i-1][j+1] == sign) {
                    count5ToTheRight++;
                }else{
                    count5ToTheRight = 1;
                }
            }
            if (count5ToTheRight == 5)
                return true;
        }
        return false;
    }



    public void drawTheBoard(Sign[][] board) {
        //drawing the field in the console after the game is over
        // as long as we don't have a frontend

        for (Sign[] x : board) {
            for (Sign y : x) {
                System.out.print(" " + y);
            }
            System.out.println();
        }
    }

    public Game makeMove(Move move) throws GameNotFoundException, InvalidGameException, IndexOutOfBoundsException {
        if(!GameStorage.getInstance().getGames().containsKey(move.getGameId())){
            throw new GameNotFoundException("game not found!");

        }
        Game game = GameStorage.getInstance().getGames().get(move.getGameId());
        if(game.getState().equals(OVER)){
            throw new InvalidGameException("this game is already finished");
        }

        Sign[][] gameBoard = game.getGameBoard();
        if (move.getPlayer().equals(game.getPlayerOne())) {
            gameBoard[move.getXPosition()][move.getYPosition()] = Sign.x;
            //The first player gets always the 'x' sign...

            if (checkWinner(gameBoard, Sign.x)){
                game.setState(OVER);
                game.setWinner(Sign.x);
                return game;
            }
        }else if (move.getPlayer().equals(game.getPlayerTwo())) {
            gameBoard[move.getXPosition()][move.getYPosition()] = Sign.o;
            //and the second one gets the 'o' sign.

            if (checkWinner(gameBoard, Sign.o)){
                game.setState(OVER);
                game.setWinner(Sign.o);
                return game;
            }
        }else{
            throw new GameNotFoundException("game not found!");
        }

            if (move.getXPosition() > gameBoard.length - 2 || move.getYPosition() > gameBoard.length - 2) {
                gameBoard = increaseBoardRightDown(gameBoard);
            }

            if (move.getXPosition() < 2 || move.getYPosition() < 2) {
                gameBoard = increaseBoardLeftUp(gameBoard);

            }

            GameStorage.getInstance().addGame(game);
            return game;
        }
}
