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
    void itSchouldSetGameByGivenPlayer() {
        // Given
        Player player = new Player("Virgis");
        // When
        //Game game = (new GameService()).setGame(player);
        Game game = underTest.setGame(player);
        // Then
        //Optional<Player> optionalPlayer = Optional.ofNullable(game.getPlayerOne());
        assertThat(game.getPlayerOne().getName()).isEqualTo("Virgis");
    }
    @Test
    void itSchouldConnectSecondPlayerToGame() {
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
    void itSchouldIncreaseBoardRightDown() {
        // Given
        Game game = underTest.setGame(new Player("Virgis"));
        // When
        game.setGameBoard(underTest.increaseBoardLeftUp(game.getGameBoard()));
        // Then
        assertThat(game.getGameBoard()).hasDimensions(18, 18);
    }

    @Test
    void increaseBoardLeftUp(Sign[][] gameBoard) {
    }

    @Test
    void checkWinner() {
    }

    @Test
    void makeMove() {
    }
}