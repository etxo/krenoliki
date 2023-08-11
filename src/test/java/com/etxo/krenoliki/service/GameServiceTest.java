package com.etxo.krenoliki.service;

import com.etxo.krenoliki.model.Game;
import com.etxo.krenoliki.model.Player;
import com.etxo.krenoliki.model.Sign;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest
class GameServiceTest {
    @Autowired
    private GameService underTest;

    @Test
    void itShouldSetGameByGivenPlayer() {
        // Given
        Player player = new Player("Virgis");
        // When
        //Game game = (new GameService()).setGame(player);
        Game game = underTest.setGame(player);
        // Then
        assertThat(game.getPlayerOne().getName()).isEqualTo("Virgis");
    }
    @Test
    void itShouldConnectSecondPlayerToGame() {
        // Given
        Game game = underTest.setGame(new Player("Virgis"));
        Player playerTwo = new Player("Andre");
        // When
        game = underTest.connectToGame(playerTwo, game.getGameId());
        // Then
        assertThat(game.getPlayerTwo().getName()).isEqualTo("Andre");
        assertThat(game.getGameId()).isEqualTo(1L);
        assertThat(game.getGameBoard()).hasDimensions(15, 15);
    }

    @Test
    void itShouldEnlargeBoardDown() {
        // Given
        Game game = underTest.setGame(new Player("Virgis"));
        game.getGameBoard()[7][12] = Sign.x;
        game.getGameBoard()[9][8] = Sign.o;
        // When
        game.setGameBoard(underTest.enlargeBoardDown(game.getGameBoard()));
        underTest.drawTheBoard(game.getGameBoard());
        // Then
        assertThat(game.getGameBoard()).hasDimensions(18, 15);
        assertThat(game.getGameBoard()[7][12]).isEqualTo(Sign.x);
        assertThat(game.getGameBoard()[9][8]).isEqualTo(Sign.o);
    }

    @Test
    void itShouldEnlargeBoardUp() {
        // Given
        Game game = underTest.setGame(new Player("Max"));
        game.getGameBoard()[3][6] = Sign.x;
        game.getGameBoard()[4][7] = Sign.o;
        // When
        game.setGameBoard(underTest.enlargeBoardUp(game.getGameBoard()));
        underTest.drawTheBoard(game.getGameBoard());
        // Then
        assertThat(game.getGameBoard()).hasDimensions(18,15);
        assertThat(game.getGameBoard()[6][6]).isEqualTo(Sign.x);
        assertThat(game.getGameBoard()[7][7]).isEqualTo(Sign.o);
    }
    @Test
    void itShouldEnlargeBoardToRight() {
        // Given
        Game game = underTest.setGame(new Player("Andrey"));
        game.getGameBoard()[3][12] = Sign.x;
        game.getGameBoard()[11][8] = Sign.o;
        // When
        game.setGameBoard(underTest.enlargeBoardToRight(game.getGameBoard()));
        underTest.drawTheBoard(game.getGameBoard());
        // Then
        assertThat(game.getGameBoard()).hasDimensions(15, 18);
        assertThat(game.getGameBoard()[3][12]).isEqualTo(Sign.x);
        assertThat(game.getGameBoard()[11][8]).isEqualTo(Sign.o);
    }
    @Test
    void itShouldEnlargeBoardToLeft() {
        // Given
        Game game = underTest.setGame(new Player("Andrey"));
        game.getGameBoard()[3][7] = Sign.x;
        game.getGameBoard()[11][8] = Sign.o;
        // When
        game.setGameBoard(underTest.enlargeBoardToLeft(game.getGameBoard()));
        underTest.drawTheBoard(game.getGameBoard());
        // Then
        assertThat(game.getGameBoard()).hasDimensions(15, 18);
        assertThat(game.getGameBoard()[3][10]).isEqualTo(Sign.x);
        assertThat(game.getGameBoard()[11][11]).isEqualTo(Sign.o);
    }
    @Test
    void itShouldCheckMainDiagonal() {
        Game game = underTest.setGame(new Player("Anatol"));
        Sign[][] board = game.getGameBoard();
        for (int i = 7, j = 3; i < 12; i++, j++){
            board [i][j] = Sign.x;
        }
        game.setGameBoard(board);
        // When
        boolean isXtheWinner = underTest.checkMainDiagonal(game.getGameBoard(), Sign.x);
        underTest.drawTheBoard(game.getGameBoard());
        // Then
        assertThat(isXtheWinner).isTrue();
        //assertThat(isOtheWinner).isTrue();
    }

    @Test
    void itShouldCheckSecondDiagonal() {
        Game game = underTest.setGame(new Player("Anatol"));
        Sign[][] board = game.getGameBoard();
        for (int i = 9, j = 12; i < 14; i++, j--){
            board [i][j] = Sign.x;
        }
        /*for (int i = 2, j = 11; i < 7; i++, j--) {
            board[i][j] = Sign.x;
        }*/
        game.setGameBoard(board);
        // When
        boolean isXtheWinner = underTest.checkSecondDiagonal(game.getGameBoard(), Sign.x);
        underTest.drawTheBoard(game.getGameBoard());
        // Then
        assertThat(isXtheWinner).isTrue();
        //assertThat(isOtheWinner).isTrue();
    }
    @Test
    void itShouldCheckWinner() {
        // Given
        Game game = underTest.setGame(new Player("Anatol"));
        Sign[][] board = game.getGameBoard();
        for (int i = 7; i < 12; i++){
            board [i][i] = Sign.x;
        }
        for (int i = 6, j = 7; i > 1; i--, j++){
            board[i][j] = Sign.o;
        }
        game.setGameBoard(board);
        // When
        boolean isXtheWinner = underTest.checkWinner(game.getGameBoard(), Sign.x);
        boolean isOtheWinner = underTest.checkWinner(game.getGameBoard(), Sign.o);
        underTest.drawTheBoard(game.getGameBoard());
        // Then
        assertThat(isXtheWinner).isTrue();
        assertThat(isOtheWinner).isTrue();
    }

    @Test
    void makeMove() {
    }
}