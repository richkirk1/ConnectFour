/**
 * Class to determine if a game of connect four has been won or if it was a draw.
 *
 * @author Rich Kirk
 */
class GameStateEvaluation {
    /**
     * Determines if a player has connected four or not
     *
     * @param board board for the given game
     * @param player check to see whther that given player has won
     * @return whether or not {@code player} has connected four on the given {@code board}
     */
    static boolean isFourConnected(Array board, int player) {
        /*
        Horizontal Check
         */
        for (int c = 0; c < board.colSize() - 3 ; c++ ){
            for (int r = 0; r < board.rowSize(); r++){
                if (board.getElement(r, c) == player && board.getElement(r, c + 1) == player
                        && board.getElement(r, c + 2) == player && board.getElement(r, c + 3) == player){
                    return true;
                }
            }
        }
        /*
        Vertical Check
         */
        for (int r = 0; r < board.rowSize() - 3 ; r++ ){
            for (int c = 0; c < board.colSize(); c++){
                if (board.getElement(r, c) == player && board.getElement(r + 1, c) == player
                        && board.getElement(r + 2, c) == player && board.getElement(r + 3, c) == player){
                    return true;
                }
            }
        }
        /*
        Bottom right to top left
         */
        for (int r = 3; r < board.rowSize(); r++){
            for (int c = 0; c < board.colSize() - 3; c++){
                if (board.getElement(r, c) == player && board.getElement(r - 1, c + 1) == player
                        && board.getElement(r - 2, c + 2) == player && board.getElement(r - 3, c + 3) == player){
                    return true;
                }
            }
        }
        /*
        Top left to bottom right
         */
        for (int r = 3; r < board.rowSize(); r++){
            for (int c = 3; c < board.colSize(); c++){
                if (board.getElement(r, c) == player && board.getElement(r - 1, c - 1) == player
                        && board.getElement(r - 2, c - 2) == player && board.getElement(r - 3, c - 3) == player){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Determines if a draw has been reached in a game
     *
     * @return whether the game board is full and thus a draw
     */
    static boolean isDraw(Array board) {
        for (int c = 0; c < board.colSize(); c++) {
            if (board.getElement(0, c) == 0) {
                return false;
            }
        }
        return true;
    }
}
