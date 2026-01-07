package com.mycompany.tic_tac_toe_app.model;

import java.util.Objects;

public class PlayerDTO {

    private String userName;
    private int score;
    private PlayerStatus status;

    public enum PlayerStatus {
        OFFLINE, IDLE, PLAYING
    }

    public PlayerDTO(String userName, int score, PlayerStatus status) {
        this.userName = userName;
        this.score = score;
        this.status = status;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("Player (%s, %s, %s)", userName, String.valueOf(score), status.name());
    }
    
 
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlayerDTO player = (PlayerDTO) obj;
        return Objects.equals(userName, player.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName); 
    }
   
}
