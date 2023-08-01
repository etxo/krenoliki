package com.etxo.krenoliki.service;

import com.etxo.krenoliki.exceptions.GameNotFoundException;
import com.etxo.krenoliki.exceptions.InvalidGameException;
import com.etxo.krenoliki.model.*;
import com.etxo.krenoliki.model.Process;
import com.etxo.krenoliki.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Arrays;

import static com.etxo.krenoliki.model.GameState.*;

@Service
@AllArgsConstructor
public class GameService {

    private final int FIELD_SIZE = 15;
    public Game createGame(Player player){
        Game game = new Game();
        game.setGameId(Game.idCounter++);
        game.setGameBoard(new Sign[FIELD_SIZE][FIELD_SIZE]);
        game.fillBoard();
        player.setSign(Sign.x);
        game.setPlayer1(player);
        game.setState(NEW);

        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game connectToGame (Player player2, Long gameId) {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParameterException("the game does not exist!");
        }

        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game.getPlayer2() != null) {
            throw new InvalidParameterException("this game has already been played");
        }

        game.setPlayer2(player2);
        game.setState(RUNNING);
        GameStorage.getInstance().setGame(game);

        return game;
    }



    public Sign[][] increaseBoardByThree(Sign[][] board){

        Sign[][] tempBoard = new Sign[board.length +3][board.length +3];

        for(int i = 0; i < board.length; i++) {
            tempBoard [i] = Arrays.copyOf(board[i], board.length + 3);
        }
        return board = tempBoard;
    }
    public Sign checkWinner (Sign[][] gB){
        if(checkRowsAndColumns(gB) == Sign.x || checkDiagonal(gB) == Sign.x) {
            //System.out.println("the winner is Andrey");
            return Sign.x;
        }
        if(checkRowsAndColumns(gB) == Sign.o || checkDiagonal(gB) == Sign.o) {
            //System.out.println("the winner is Virgis");
            return Sign.o;
        }
        return null;
    }

    private Sign checkRowsAndColumns(Sign[][] gB) throws IndexOutOfBoundsException{

        for(int i = 0; i < gB.length; i++){

            int countRows5 = 0;
            int countColumns5 =0;

            for(int j = 1; j < gB[i].length; j++){ //checks the rows

                if(gB[i][j] == gB[i][j-1]){
                    countRows5++;
                }else{
                    countRows5 = 0;
                }

                if(countRows5 == 5){
                    return gB[i][j];
                }
                if(gB[j][i] == gB[j-1][i]){
                    countColumns5++;
                }else{
                    countColumns5 = 0;
                }

                if(countColumns5 == 5){
                    return gB[j][i];
                }
            }
        }
        return null;
    }

    private Sign checkDiagonal (Sign[][] gB) throws IndexOutOfBoundsException{

        for(int k = 3; k < gB.length-1; k++){

            int count5 = 0;
            for(int j = k, i = 1; j >= 0; j--, i++){
                if(gB[i][j] == gB[i-1][j+1]){
                    count5++;
                }else{
                    count5 = 0;
                }

                if(count5 == 5){
                    return gB[i][j];
                }
            }

            for(int j = gB[0].length - k, i = 1; j < gB[0].length; j++, i++){
                if(gB[i][j] == gB[i-1][j-1]){
                    count5++;
                }else{
                    count5 = 0;
                }

                if(count5 == 5){
                    return gB[i][j];
                }
            }
        }
        return null;
    }

    public void drawTheField(Sign[][] field) { //drawing the field in the console as long as we don't have a frontend
        //and after the game is over

        for (Sign[] x : field) {
            for (Sign y : x) {
                System.out.print(" " + y);
            }
            System.out.println();
        }
    }

    public Game makeMove(Process move) throws GameNotFoundException, InvalidGameException {
        if(!GameStorage.getInstance().getGames().containsKey(move.getGameId())){
            throw new GameNotFoundException("game not found!");
        }
        Game game = GameStorage.getInstance().getGames().get(move.getGameId());
        if(game.getState().equals(OVER)){
            throw new InvalidGameException("this game is already finished");
        }
        Sign[][] gameBoard = game.getGameBoard();
        gameBoard[move.getPositionX()][move.getPositionY()] = move.getPlayer().getSign();

        Sign winner = checkWinner(game.getGameBoard());
        if(winner != null){
            game.setState(OVER);
        }

        if(move.getPositionX() > gameBoard.length - 3 || move.getPositionY() > gameBoard.length - 3){
            gameBoard = increaseBoardByThree(gameBoard);
        }

        GameStorage.getInstance().setGame(game);
        return game;
    }
}
