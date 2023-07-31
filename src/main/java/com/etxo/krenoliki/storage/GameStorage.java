package com.etxo.krenoliki.storage;

import com.etxo.krenoliki.model.Game;

import java.util.HashMap;
import java.util.Map;

public class GameStorage {

    private static Map<Long, Game> games;
    private static GameStorage instance;

    private GameStorage() {
        Map<Long, Game> games = new HashMap<>();
    }

    public static synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }
    public Map<Long, Game> getGames() {
        return games;
    }

    public void setGame(Game game) {
        games.put(game.getGameId(), game);
    }
}
