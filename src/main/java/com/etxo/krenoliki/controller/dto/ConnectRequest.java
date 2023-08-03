package com.etxo.krenoliki.controller.dto;

import com.etxo.krenoliki.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private Long gameId;
}