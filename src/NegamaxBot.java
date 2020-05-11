/**
 * @author Rich Kirk
 */
class NegamaxBot {
    /**
     * Returns the max of two integers
     *
     * @param a first int
     * @param b second int
     * @return maximum value of {@code a} and {@code b}
     */
    private static int max(int a, int b) {
        if(a > b) {
            return a;
        }
        return b;
    }

    /**
     * Counts number of times an integer has appeared in an array
     *
     * @param vector array to search for the given {@code instance}
     * @param instance int to search for in {@code vector}
     * @return number of times {@code instance} is in {@code vector}
     */
    private static int countInstances(int[] vector, int instance) {
        int count = 0;
        for (int e : vector) {
            if (e == instance) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns a score of a given position in a game of connect four for a given player
     *
     * @param board game board to evaluate
     * @param playerTurn player to evaluate {@code board} for
     * @return score of {@code playerTurn} in a state of {@code board}
     */
    private static int evaluate(Array board, int playerTurn) {
        return evaluateBoard(board, playerTurn) + evaluateConnect(board, playerTurn);
    }

    /**
     * Evaluates a board based on positional advantage, weighing areas in the middle of the board higher
     * than positions on the edges.
     *
     * @param board game board to evaluate
     * @param playerTurn player to evaluate {@code board} for
     * @return score of the positions of {@code playerTurn} in a state of {@code board}
     */
    private static int evaluateBoard(Array board, int playerTurn) {
        int score = 0;

        Array evaluationBoard = new Array(board.rowSize(), board.colSize());

        for (int r = 0; r < board.rowSize(); r++) {
            for (int c = 0; c < board.colSize(); c++) {
                evaluationBoard.setElement(r, c, 5 - (Math.abs((evaluationBoard.colSize() / 2) - c)
                        + Math.abs((evaluationBoard.rowSize() / 2) - r)
                        - (evaluationBoard.colSize() % 2 - 1) * Math.abs((evaluationBoard.colSize() / 2) - c - 1)
                        - (evaluationBoard.rowSize() % 2 - 1) * Math.abs((evaluationBoard.rowSize() / 2) - r - 1))
                        / (2 - (evaluationBoard.colSize() % 2 - 1) - (evaluationBoard.colSize() % 2 - 1)));
            }
        }

        for (int r = 0; r < board.rowSize(); r++) {
            for (int c = 0; c < board.colSize(); c++) {
                 if(board.getElement(r, c) == playerTurn) {
                     score += evaluationBoard.getElement(r, c);
                 } else if(board.getElement(r, c) == playerTurn * -1) {
                     score -= evaluationBoard.getElement(r, c);
                 }
            }
        }

        return score;
    }

    /**
     * Evaluates a players number of connections with its other pieces
     *
     * @param board game board to evaluate
     * @param playerTurn player to evaluate {@code board} for
     * @return a score of a given {@code playerTurn}'s in a given {@code board}
     */
    private static int evaluateConnect(Array board, int playerTurn) {
        int score = 0;
        /*
        Rows
         */
        for (int r = 0; r < board.rowSize(); r++) {
            /*
            Initialize and reset a count per row
             */
            int[] rowArray = board.getRow(r);
            for (int c = 0; c < board.colSize() - 4; c++) {
                int[] block = {rowArray[c], rowArray[c + 1], rowArray[c + 2],  rowArray[c + 3]};
                score += evaluateBlock(block, playerTurn);
            }
        }
        /*
        Columns
         */
        for (int c = 0; c < board.colSize(); c++) {
            /*
            Initialize and reset a count per row
             */
            int[] colArray = board.getCol(c);
            for (int r = 0; c < board.rowSize() - 4; c++) {
                int[] block = {colArray[r], colArray[r + 1], colArray[r + 2],  colArray[r + 3]};
                score += evaluateBlock(block, playerTurn);
            }
        }
        /*
        Diagonal - Top left to bottom right
         */
        for (int r = 3; r < board.rowSize(); r++) {
            for (int c = 3; c < board.colSize(); c++) {
                int[] block = {board.getElement(r, c), board.getElement(r - 1, c - 1), board.getElement(r - 2, c - 2), board.getElement(r - 3, c - 3)};
                score += evaluateBlock(block, playerTurn);
            }
        }
        /*
        Diagonals - Top right to bottom left
         */
        for (int r = 3; r < board.rowSize(); r++) {
            for (int c = 0; c < board.colSize() - 3; c++) {
                int[] block = {board.getElement(r, c), board.getElement(r - 1, c + 1), board.getElement(r - 2, c + 2), board.getElement(r - 3, c + 3)};
                score += evaluateBlock(block, playerTurn);
            }
        }
        return score;
    }

    /**
     * Evaluates a players and their enemies connections in a given 4x1 block
     *
     * @param block 4x1 or 1x4 block to evaluate
     * @param playerTurn player to evaluate {@code block} for
     * @return {@code playerTurn}'s connections compared to their enemy in a given {@code block}
     */
    private static int evaluateBlock(int[] block, int playerTurn) {
        int score = 0;
        int playerCount = countInstances(block, playerTurn);
        int opponentCount = countInstances(block, playerTurn * -1);
        int emptyCount = 4 - playerCount - opponentCount;
        /*
        Weigh connections of 3 highest and 2 second highest, etc.
         */
        if(playerCount == 3 && emptyCount == 1) {
            score += 10;
        } else if(playerCount == 2 && emptyCount == 2) {
            score += 4;
        } else if(opponentCount == 2 && emptyCount == 2) {
            score -= 5;
        } else if(opponentCount == 3 && emptyCount == 1) {
            score -= 8;
        }
        return score;
    }

    /**
     * Evaluates a given players score given a current board state using a negamax algorithm, which focuses on
     * maximizing a players score in a 2 player game. Search variant of minimax algorithm which relies on the principle
     * that max(a, b) = -min(-a, -b).
     *
     *
     * @param board game board to evaluate
     * @param depth number of nodes to explore down a move tree
     * @param alpha an alpha score for alpha-beta pruning
     * @param beta a beta score for alpha-beta pruning
     * @param playerTurn player maximizing their score
     * @return {@code playerTurn}'s highest score given the {@code board}
     */
    private static int negamaxPruning(Array board, int depth, int alpha, int beta, int playerTurn) {
        if (GameStateEvaluation.isDraw(board)) {
            return 0;
        } else if (GameStateEvaluation.isFourConnected(board, playerTurn * -1)) {
            return Integer.MIN_VALUE + 1;
        } else if (depth == 0) {
            return evaluate(board, playerTurn);
        }
        int maxScore = Integer.MIN_VALUE + 1;
        for (int c = 0; c < board.colSize(); c++) {
            if (board.getElement(0, c) == 0) /* If the selected column is empty */ {
                int r = board.rowSize() - 1;
                while (board.getElement(r, c) != 0) {
                    r--;
                }
                board.setElement(r, c, playerTurn);
                int score = -1 * negamaxPruning(board, depth - 1, -1 * beta, -1 * alpha, playerTurn * -1);
                board.setElement(r, c, 0);
                maxScore = max(maxScore, score);
                alpha = max(alpha, score);
                if(alpha >= beta || maxScore == Integer.MAX_VALUE) {
                    break;
                }
            }
        }
        return maxScore;
    }

    /**
     * Selects the best column for a given player to play given a certain game baord. Further, restrict its search
     * capabilities to a certain depth.
     *
     * @param board game state to evaluate the given player
     * @param playerTurn player to explore the best column for
     * @param depth depth at which to explore the game trees nodes
     * @return integer of a column at which {@code playerTurn} has the highest score for a given {@code board}
     */
    static int bestCol(Array board, int playerTurn, int depth) {
        int bestCol = 0;
        int bestScore = Integer.MIN_VALUE + 1;

        for (int c = 0; c < board.colSize(); c++) {
            if(board.getElement(0, c) == 0) /* If the selected column is empty*/  {
                int r = board.rowSize() - 1;
                while(board.getElement(r, c) != 0) {
                    r--;
                }
                board.setElement(r, c, playerTurn);
                int score = -1 * negamaxPruning(board, depth, Integer.MIN_VALUE + 1, Integer.MAX_VALUE,playerTurn * -1);
                if(GameStateEvaluation.isFourConnected(board, playerTurn)) {
                    board.setElement(r, c, 0);
                    return c;
                }
                board.setElement(r, c, 0);

                if (score >= bestScore) {
                    bestScore = score;
                    bestCol = c;
                }
            }
        }
        System.out.println();
        return bestCol;
    }
}
