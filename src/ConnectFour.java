import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Rich Kirk
 */
public class ConnectFour extends JFrame implements ActionListener {
    /*
     * Game board dimensions TODO create customizable dimensions
     */
    // private static int gameBoardRowCount = 6;
    // private static int gameBoardColCount = 7;
    /**
     * Variable showing whose turn it is, 1 for black, -1 for red.
     */
    private static int playerTurn;
    /**
     * Logic array to store game positions
     */
    private Array gameBoard;
    /**
     * Buttons for user input
     */
    private JButton[][] GUIBoard;
    /**
     * Panel concerning the main menu
     */
    private JPanel menuPanel = new JPanel();
    /**
     * Panel concerning the game
     */
    private JPanel gamePanel = new JPanel();
    /**
     * Menu button to quit
     */
    private JButton quitButton;
    /**
     * Menu button to play
     */
    private JButton playButton;
    /**
     * Two types to play against
     */
    private String[] playerTypes = {"Player", "Bot"};
    /**
     * Drop down selection for player one
     */
    private JComboBox<String> player1Selection;
    /**
     * Drop down selection for player two
     */
    private JComboBox<String> player2Selection;
    /*
     * TODO create a change bot difficulty combo box
     */
    // private JComboBox<String> bot1Levels = new JComboBox<>(difficultyLevels);
    // private static int botDifficultyLevel = 7;
    // private JComboBox<String> bot2Levels = new JComboBox<>(difficultyLevels);
    // private String[] difficultyLevels = { "Beginner","Easy", "Normal", "Hard", "Advanced"};
    /**
     * Initialize a bot with the first turn to not playing
     */
    private static int bot1 = 0;
    /**
     * Initialize a bot with the second turn to not playing
     */
    private static int bot2 = 0;
    /**
     * Initialize a player with the first turn to playing
     */
    private static int player1 = 1;
    /**
     * Initialize a player with the second turn to playing
     */
    private static int player2 = -1;
    /*
    TODO add customizable player names
     */
    // private static String player1Name;
    // private static String player2Name;
    /**
     * Constructor to initialize JFrame
     */
    private ConnectFour() {
        setTitle("Connect 4");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeMenu();
    }

    /**
     * Initializes game GUI panel
     */
    private void initializeGame() {
        /*
        Clear the frame and re-add the game panel
         */
        this.setSize(new Dimension(875, 750));
        gamePanel.removeAll();
        getContentPane().removeAll();
        getContentPane().add(gamePanel);
        getContentPane().revalidate();
        /*
         * Game board dimensions TODO create customizable dimensions
         */
        int gameBoardRowCount = 6;
        int gameBoardColCount = 7;
        /*
        Initialize the boards for play
         */
        gameBoard = new Array(gameBoardRowCount, gameBoardColCount);
        GUIBoard = new JButton[gameBoardRowCount][gameBoardColCount];
        gamePanel.setLayout(new GridLayout(gameBoardRowCount, gameBoardColCount));
        /*

         */
        playerTurn = 1;

        /*
        Initialize the buttons
         */
        for (int r = 0; r < GUIBoard.length; r++) {
            for (int c = 0; c < GUIBoard[r].length; c++) {
                GUIBoard[r][c] = new JButton();

                GUIBoard[r][c].addActionListener(new GameListener(c));

                GUIBoard[r][c].setBackground(Color.WHITE);

                gamePanel.add(GUIBoard[r][c]);
            }
        }
        /*
        If a bot is to go first, schedule its first move
         */
        if(bot1 == 1) {
            botMove();
        }
    }

    /**
     * Initializes menu GUI panel
     */
    private void initializeMenu() {
        /*
        Clear the frame and re-add the menu panel
         */
        this.setSize(new Dimension(500,  500));
        menuPanel.removeAll();
        getContentPane().removeAll();
        getContentPane().revalidate();
        getContentPane().add(menuPanel);
        /*
        Initialize a Spring layout for swing capabilities
         */
        SpringLayout layout = new SpringLayout();
        menuPanel.setLayout(layout);
        SpringUtilities.makeGrid(menuPanel, 4, 1, 50, 50, 50, 50);
        /*
        Initialize the menu buttons
         */
        playButton = new JButton("Play");
        quitButton = new JButton("Quit");
        playButton.addActionListener(this);
        quitButton.addActionListener(this);
        /*
        Initialize the combo boxes
         */
        player1Selection = new JComboBox<>(playerTypes);
        player2Selection = new JComboBox<>(playerTypes);
        player1Selection.setSelectedIndex(0);
        player2Selection.setSelectedIndex(0);
        player1Selection.addActionListener(this);
        player2Selection.addActionListener(this);
        /*
        Add the buttons/combo boxes to the panel
         */
        menuPanel.add(playButton);
        menuPanel.add(player1Selection);
        menuPanel.add(player2Selection);
        menuPanel.add(quitButton);
    }

    /**
     * Updates player type based on ComboBox input
     *
     * @param playerType ComboBox option currently selected
     */
    private void updatePlayerType(String playerType) {
        /*
        Note: Bot vs Bot game play is not allowed
         */
        if(playerType.equals(this.playerTypes[0] + "1")) {
            /*
            Player 1 Selection
             */
            /*
            Clear bot 1 and add player 1
             */
            bot1 = 0;
            player1 = 1;
            /*
            Allow player 2 to be a bot
             */
            player2Selection.setEnabled(true);
        } else if(playerType.equals(this.playerTypes[1] + "1")) {
            /*
            Bot 1 Selection
             */
            /*
            Clear player 1 and add bot 1
             */
            bot1 = 1;
            player1 = 0;
            /*
            Don't allow player 2 to be a bot
             */
            player2Selection.setEnabled(false);
            bot2 = 0;
            player2 = -1;
        } else if(playerType.equals(this.playerTypes[0] + "2")) {
            /*
            Player 2 Selection
             */
            bot2 = 0;
            player2 = -1;
            /*
            Allow player 1 to be a bot
             */
            player1Selection.setEnabled(true);
        } else if(playerType.equals(this.playerTypes[1] + "2")) {
            /*
            Bot 2 Selection
             */
            /*
            Clear player 2 and add bot 2
             */
            bot2 = -1;
            player2 = 0;
            /*
            Don't allow player 1 to be a bot
             */
            player1Selection.setEnabled(false);
            bot1 = 0;
            player1 = 1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConnectFour().setVisible(true));
    }

    /**
     * Takes a players turn and returns a players image icon piece to be set on the button of the GUI board
     *
     * @return an ImageIcon for the current players move
     */
    private ImageIcon getImageIcon() {
        ImageIcon imageIcon = new ImageIcon();
        try {
            if(playerTurn == 1) {
                imageIcon = new ImageIcon(ImageIO.read(getClass().getResource("BlackPiece.png")).getScaledInstance(175, 175, Image.SCALE_SMOOTH));
            } else {
                imageIcon = new ImageIcon(ImageIO.read(getClass().getResource("RedPiece.png")).getScaledInstance(175, 175, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return imageIcon;
    }

    /**
     * Method to make moves for a given bot
     */
    private void botMove() {
        /*
        Receive the best column to be played given a botDifficultyLevel(depth of search)
         */
        int botDifficultyLevel = 7;
        int bestCol = NegamaxBot.bestCol(gameBoard, playerTurn, botDifficultyLevel);
        /*
        Find the row of play for a given column
         */
        int r = gameBoard.rowSize() - 1;
        while(gameBoard.getElement(r, bestCol) != 0) {
            r--;
        }
        /*
        Update the game board
         */
        gameBoard.setElement(r, bestCol, playerTurn);
        GUIBoard[r][bestCol].setIcon(getImageIcon());
        gameEvaluation();
        playerTurn *= -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        Action listener for the menu panel
         */
        Object source = e.getSource();
        if(source == this.quitButton) {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if(source == this.playButton) {
            initializeGame();
        } else if(source == this.player1Selection) {
            JComboBox comboBox = (JComboBox)source;
            String playerType = (String)comboBox.getSelectedItem();
            updatePlayerType(playerType + "1");
        } else if(source == this.player2Selection) {
            JComboBox comboBox = (JComboBox)source;
            String playerType = (String)comboBox.getSelectedItem();
            updatePlayerType(playerType + "2");
        }
    }

    /**
     * Evaluates whether a draw or a winner is reached
     */
    private void gameEvaluation() {
        /*
        If there was a draw or a connect four
         */
        if (GameStateEvaluation.isFourConnected(gameBoard, playerTurn) || GameStateEvaluation.isDraw(gameBoard)) {
            String message;
            String title = "Game Over";
            if (GameStateEvaluation.isDraw(gameBoard)) /* if game results in draw */ {
                message = "Game is a Draw.";
            } else /* If the game was won */ {
                if(playerTurn == bot1) {
                    message = "Bot 1 has won!";
                } else if(playerTurn == bot2) {
                    message = "Bot 2 has won!";
                } else if(playerTurn == player1) {
                    message = "Player 1 has won!";
                } else /* playerTurn == player2 */ {
                    message = "Player 2 has won!";
                }
            }
            int option = JOptionPane.showConfirmDialog(this,
                    message + "\nWould you like to play again?", title, JOptionPane.YES_NO_OPTION);
            if(option == 0) /* Restarting the game */ {
                initializeMenu();
            } else /* Quitting the game */ {
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        }
    }

    /**
     * Class to listen to the game board
     */
    private class GameListener implements ActionListener {
        /*
        Column selected
         */
        private int col;

        /*
        Default Constructor
         */
        GameListener(int c) {
            this.col = c;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            /*
            For a given player turn
             */
            if (playerTurn == player1 || player2 == playerTurn) {
                if (gameBoard.getElement(0, col) == 0) /* Column not full */ {
                    /*
                    Find the row of play for a given column
                     */
                    int r = gameBoard.rowSize() - 1;
                    while (gameBoard.getElement(r, col) != 0) {
                        r--;
                    }
                    /*
                    Update the game board
                     */
                    gameBoard.setElement(r, col, playerTurn);
                    GUIBoard[r][col].setIcon(getImageIcon());
                    gameEvaluation();
                    playerTurn *= -1;
                    /*
                    For a given bot move after a player turn
                     */
                    if(bot1 == playerTurn || bot2 == playerTurn) {
                        botMove();
                    }
                }
            }
        }
    }
}
