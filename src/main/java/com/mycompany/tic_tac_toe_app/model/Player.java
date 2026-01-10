package com.mycompany.tic_tac_toe_app.model;

public class Player {

    private final String name;
    private final PlayerStatus status;

    public Player(String name, PlayerStatus status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public PlayerStatus getStatus() {
        return status;
    }
}
