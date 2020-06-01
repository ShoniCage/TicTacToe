package geekBrainsHomeWork7;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameActionListener implements ActionListener {
    private int rating = 0, max = 0;
    private int ratingX, ratingY;

    GameBoard board;
    GameButton gameButton;

    private int row;
    private int cell;


    public GameActionListener (int row, int cell, GameButton button) {
        this.row = row;
        this.cell = cell;
        this.gameButton = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        board = gameButton.getBoard();

        if (board.isTurnable(row, cell) && board.getGame().getCurrentPlayer().isRealPlayer()) {
            updateByPlayersData(board);

            if (board.isFull()) {
                board.getGame().showMessage("Ничья");
                board.emptyField();
            } else {
                updateByAIData(board);
            }
        } else {
            board.getGame().showMessage("Некорректный ход");
        }


    }

    private void updateByPlayersData(GameBoard board) {
        // Обновляем матрицу игры
        board.updateGameField(row, cell);
        // Обновляем содержимое кнопки
        gameButton.setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));
        // После хода проверим состояние победы
        isEndGame(board);
    }

    public void updateByAIData (GameBoard board) {
        AITurn();
        if (max == 0) {
            // Генерация координат хода компьютера
            int x, y;
            Random rnd = new Random();

            do {
                x = rnd.nextInt(GameBoard.dimension);
                y = rnd.nextInt(GameBoard.dimension);
            } while (!board.isTurnable(x, y));

            // Обновим матрицу игры
            board.updateGameField(x, y);

            // Обновим содержимое кнопки
            int cellIndex = GameBoard.dimension * x + y;
            board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));
        } else {
            // Обновим матрицу игры
            board.updateGameField(ratingX, ratingY);
            int cellIndex = GameBoard.dimension * ratingX + ratingY;
            board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));
        }
        // Проверить победу
        isEndGame(board);
    }

    void isEndGame (GameBoard board) {
        if (board.checkWin()) {
            gameButton.getBoard().getGame().showMessage("Выиграли " + board.getGame().getCurrentPlayer().getPlayerSign() + "!");
            board.emptyField();
        } else {
            board.getGame().passTurn();
        }
    }

    void AITurn () {
        for(int i = 0; i < board.dimension; i++) {
            for (int j = 0; j < board.dimension; j++) {
                if (board.getGameField(i, j) == board.nullSymbol) {
                    rating = RatingCell(j, i);
                }

                if (rating > max) {
                    max = rating;
                    ratingX = j;
                    ratingY = i;
                }
            }
        }
    }

    int RatingCell(int x, int y) { // составление рейтинга ячеек
        int rating1 = 0, rating2 = 0, rating3 = 0, rating4 = 0, rating5 = 0;
        int a = y;
        int b = x;
        rating1 = (Rating(a, b, board.getGame().getPlayers(0)) == 10)? 9 : 0;
        rating2 = (Rating(a, b, board.getGame().getPlayers(1)) == 10)? 10 : 0;
        rating3 = (Rating(a, b, board.getGame().getPlayers(1)) == 5)? 7 : 0; // иначе = пустые соседние ячейки
        rating4 = (Rating(a, b, board.getGame().getPlayers(0)) == 0)? 6 : 0; // иначе = пустые соседние ячейки
        rating5 = (Rating(a, b, board.getGame().getPlayers(1)) == 7)? 8 : 0; // при проверке центральной ячейки рейтинг выше, чем у остальных при заполненности одним символом
        int [] massrating = {rating1, rating2, rating3, rating4, rating5};
        int max = 0;
        for (int cell : massrating) {
            if (cell > max) {
                max = cell;
            }
        }
        return max;
    }

    private int Rating(int y, int x, char symbol) {

        int result = 0, r1 = 0, r2 = 0;
        if (x == 0 && y == 0) {
            if (board.getGameField(y, x+1) == symbol && board.getGameField(y, x+2) == symbol || board.getGameField(y+1,x+1) == symbol
                    && board.getGameField(y+2,x+2) == symbol || board.getGameField(y+1,x) == symbol && board.getGameField(y+2,x) == symbol) {
                r1 = 10;
            }
            if (board.getGameField(y,x+1) == symbol || board.getGameField(y+1, x+1) == symbol || board.getGameField(y+1,x) == symbol) {
                r2 = 5;
            }
        }
        if (x == 1 && y == 0) {
            if (board.getGameField(y, x-1) == symbol && board.getGameField(y,x+1) == symbol || board.getGameField(y+1, x) ==symbol
                    && board.getGameField(y+2,x) == symbol) {
                r1 = 10;
            }
            if (board.getGameField(y,x-1) == symbol || board.getGameField(y+1,x) == symbol || board.getGameField(y, x+1) == symbol) {
                r2 = 5;
            }
        }
        if (x == 2 && y == 0) {
            if (board.getGameField(y, x-2) == symbol && board.getGameField(y, x-1) == symbol || board.getGameField(y+2,x-2) == symbol
                    && board.getGameField(y+1,x-1) == symbol || board.getGameField(y+2, x) == symbol && board.getGameField(y+1, x) == symbol) {
                r1 = 10;
            }
            if (board.getGameField(y, x-1) == symbol || board.getGameField(y+1,x-1) == symbol || board.getGameField(y+1, x) == symbol) {
                r2 = 5;
            }
        }
        if (x == 0 && y == 1) {
            if (board.getGameField(y, x+2) == symbol && board.getGameField(y, x+1) == symbol || board.getGameField(y-1, x) == symbol
                    && board.getGameField(y+1, x) == symbol) {
                r1 = 10;
            }
            if (board.getGameField(y-1,x) == symbol || board.getGameField(y,x+1) == symbol || board.getGameField(y+1,x) == symbol) {
                r2 = 5;
            }
        }
        if (x == 1 && y == 1) {
            if (board.getGameField(y, x-1) == symbol && board.getGameField(y, x+1) == symbol || board.getGameField(y-1, x) == symbol
                    && board.getGameField(y+1, x) == symbol || board.getGameField(y-1, x-1) == symbol && board.getGameField(y+1, x+1) == symbol) {
                r1 = 10;
            }
            if (board.getGameField(y-1,x-1) == symbol || board.getGameField(y-1, x) == symbol || board.getGameField(y-1, x+1) == symbol || board.getGameField(y, x+1) == symbol
                    || board.getGameField(y+1, x+1) == symbol || board.getGameField(y+1, x) == symbol || board.getGameField(y+1, x-1) == symbol || board.getGameField(y, x-1) == symbol) {
                r2 = 7;
            }

        }
        if (x == 2 && y == 1) {
            if (board.getGameField(y, x-2) == symbol && board.getGameField(y, x-1) == symbol || board.getGameField(y-1, x) == symbol && board.getGameField(y+1, x) == symbol) {
                r1 = 10;
            }
            if (board.getGameField(y, x-1) == symbol || board.getGameField(y-1, x) == symbol || board.getGameField(y+1, x) == symbol) {
                r2 = 5;
            }
        }
        if (x == 0 && y == 2) {
            if (board.getGameField(y, x+2) == symbol && board.getGameField(y, x+1) == symbol || board.getGameField(y-2, x) == symbol && board.getGameField(y-1, x) == symbol
                    || board.getGameField(y-2, x+2) == symbol && board.getGameField(y-1,x+1) == symbol) {
                r1 = 10;
            }
            if (board.getGameField(y, x+1) == symbol || board.getGameField(y-1,x+1) == symbol || board.getGameField(y, x+1) == symbol) {
                r2 = 5;
            }
        }
        if (x == 1 && y == 2) {
            if (board.getGameField(y, x-1) == symbol && board.getGameField(y, x+1) == symbol || board.getGameField(y-2, x) == symbol
                    && board.getGameField(y-1, x) == symbol) {
                r1 = 10;
            }
            if (board.getGameField(y, x-1) == symbol || board.getGameField(y-1, x) == symbol || board.getGameField(y, x+1) == symbol) {
                r2 = 5;
            }
        }
        if (x == 2 && y == 2) {
            if (board.getGameField(y, x-2) == symbol &&  board.getGameField(y, x-1) == symbol || board.getGameField(y-2, x) == symbol
                    && board.getGameField(y-1, x) == symbol || board.getGameField(y-2, x-2) == symbol && board.getGameField(y-1, x-1) == symbol) {
                r1 = 10;
            }
            if (board.getGameField(y, x-1) == symbol || board.getGameField(y-1, x-1) == symbol || board.getGameField(y-1, x) == symbol) {
                r2 = 5;
            }
        }
        if (r1 > r2) { result = r1; }
        else result = r2;
        return result;
    }


}
