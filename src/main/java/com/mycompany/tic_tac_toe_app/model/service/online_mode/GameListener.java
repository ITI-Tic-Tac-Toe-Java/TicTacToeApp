/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tic_tac_toe_app.model.service.online_mode;

/**
 *
 * @author thaowpstasaiid
 */
public interface GameListener {
        void onOpponentMove(int row, int col, String symbol);
        void onGameResult(String result);
    }
