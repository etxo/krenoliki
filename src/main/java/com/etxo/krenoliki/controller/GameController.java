package com.etxo.krenoliki.controller;

import com.etxo.krenoliki.controller.dto.ConnectRequest;
import com.etxo.krenoliki.exceptions.GameNotFoundException;
import com.etxo.krenoliki.exceptions.InvalidGameException;
import com.etxo.krenoliki.model.Game;
import com.etxo.krenoliki.model.Move;
import com.etxo.krenoliki.model.Player;
import com.etxo.krenoliki.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player){
        return ResponseEntity.ok(gameService.setGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws InvalidGameException{
        return ResponseEntity.ok(gameService.connectToGame(request.getPlayer(), request.getGameId()));
    }

    @PostMapping("/move")
    public ResponseEntity<Game> makeMove(@RequestBody Move request) throws GameNotFoundException, InvalidGameException {
        Game game = gameService.makeMove(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}
