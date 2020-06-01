package geekBrainsHomeWork7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard extends JFrame {
    static int dimension = 3;
    static int cellSize = 150;
    private char gameField[][];
    private GameButton gameButton [];

    static char nullSymbol = '\u0000';

    private Game game;

    public GameBoard (Game currentGame) {
        this.game = currentGame;
        initField();
    }

    private void initField() {
        setBounds(cellSize * dimension, cellSize * dimension, 400, 300);
        setTitle("Крестики-нолики");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(new java.awt.Color(0,0,0));

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("Новая игра");
        Font font = new Font("Mono821BT", Font.BOLD, 30);
        newGameButton.setFont(font);
        newGameButton.setBackground(new Color(80, 80, 80));
        newGameButton.setForeground(new Color(207, 207, 214));
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField();
            }
        });
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(newGameButton);
        controlPanel.setSize(cellSize * dimension * dimension, 150);
        controlPanel.setBackground(Color.BLACK);

        JPanel gameFieldPanel = new JPanel();
        gameFieldPanel.setLayout(new GridLayout(dimension, dimension));
        gameFieldPanel.setSize(cellSize * dimension, cellSize * dimension);
        gameFieldPanel.setBackground(Color.BLACK);

        gameField = new char[dimension][dimension];
        gameButton = new GameButton[dimension * dimension];

        for (int i = 0; i < (dimension * dimension); i++) {
            GameButton buttonField = new GameButton(i, this);
            gameFieldPanel.add(buttonField);
            gameButton[i] = buttonField;
            gameButton[i].setFont(font);
            gameButton[i].setForeground(new Color(207, 207, 214));
            gameButton[i].setBackground(new Color(60, 60, 60));
        }

        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(gameFieldPanel, BorderLayout.CENTER);
        setBackground(Color.BLACK);
        setVisible(true);
    }

    void emptyField() {
        for(int i = 0; i < (dimension * dimension); i++){
            gameButton[i].setText("");

            int x = i / GameBoard.dimension;
            int y = i % GameBoard.dimension;

            gameField[x][y] = nullSymbol;

            game.setPlayersTurn(0);
        }
    }

    boolean isTurnable(int x, int y){
        boolean result = false;

        if(gameField[y][x] == nullSymbol){
            result = true;
        }

        return result;
    }

    Game getGame () {
        return game;
    }

    void updateGameField(int x, int y){
        gameField[y][x] = game.getCurrentPlayer().getPlayerSign();
    }

    boolean checkWin(){
        boolean result = false;

        char playerSymbol = getGame().getCurrentPlayer().getPlayerSign();

        if(checkWinDiagonals(playerSymbol) || checkWinLines(playerSymbol)){
            result = true;
        }

        return result;
    }


    private boolean checkWinDiagonals(char playerSymbol){
        boolean leftRight, rightLeft, result;

        leftRight = true;
        rightLeft = true;
        result = false;

        for(int i = 0; i < dimension; i++){
            leftRight &= (gameField[i][i] == playerSymbol); //оптимизация кода, чтобы не было через if/else
            rightLeft &= (gameField[dimension - i - 1][i] == playerSymbol);
        }

        if(leftRight || rightLeft){
            result = true;
        }

        return result;

    }
    private boolean checkWinLines(char playerSymbol){
        boolean cols, rows, result;

        result = false;

        for(int col = 0; col < dimension; col++){
            cols = true;
            rows = true;

            for(int row = 0; row < dimension; row++){
                cols &= (gameField[col][row] == playerSymbol);
                rows &= (gameField[row][col] == playerSymbol);
            }
            if(cols || rows){
                result = true;
                break;
            }
            if(result) {
                break;
            }
        }
        return result;
    }

    boolean isFull(){
        boolean result = true;

        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(gameField[i][j] == nullSymbol){
                    result = false;
                }
            }
        }
        return result;
    }


    // Получаем кнопку
    public GameButton getButton(int buttonIndex) {
        return gameButton[buttonIndex];
    }

    public char getGameField(int i, int j) {
        return gameField[i][j];
    }
}
