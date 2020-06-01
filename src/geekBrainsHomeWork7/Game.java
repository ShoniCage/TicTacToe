package geekBrainsHomeWork7;

import javax.swing.*;

public class Game {
    private GameBoard board;
    private GamePlayer [] players = new GamePlayer [2];
    private int playersTurn = 0;

    public Game () {
        this.board = new GameBoard(this);
    }

    public void initGame() {
        players[0] = new GamePlayer('X', true);
        players[1] = new GamePlayer('O', false);
    }

    void passTurn() {
        if (playersTurn == 0) playersTurn = 1;
        else playersTurn = 0;
    }

    public void setPlayersTurn(int playersTurn) {
        this.playersTurn = playersTurn;
    }

    GamePlayer getCurrentPlayer () { return players[playersTurn]; }

    char getPlayers (int number) {
        return players[number].getPlayerSign();
    }

    void showMessage (String messageText) {
        JOptionPane.showMessageDialog(board, messageText);
    }

}

