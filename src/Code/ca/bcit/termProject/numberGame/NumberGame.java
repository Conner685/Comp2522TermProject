package ca.bcit.termProject.numberGame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Random;

/**
 * The NumberGame class implements a simple number placement game using JavaFX.
 * The player must place randomly generated numbers in ascending order on a 4x5 grid.
 * @author Conner Ponton
 * @version 1.0
 */
public final class NumberGame
        extends Application
        implements GeneralGameLogic
{
    private static final int ROWS = 4;
    private static final int COLS = 5;
    private static final int TOTAL_SQUARES = ROWS * COLS;
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 1000;
    private static final int INITIAL_STAT = 0;
    private static final int INITIAL_NUM = -1;

    private static final int BUTTON_SIZE = 100;

    private static final int SCENE_WIDTH = 650;
    private static final int SCENE_HEIGHT = 750;
    static final String CSS_PATH = "/numberStyle.css";

    private final int[] numbers;
    private int currentNumberIndex;
    private int successfulPlacements;
    private int gamesPlayed;
    private int gamesWon;

    private final Button[][] gridButtons;
    private final Label statusLabel;
    private final Label currentNumberLabel;

    /**
     * Public constructor required by JavaFX.
     */
    public NumberGame()
    {
        numbers              = new int[TOTAL_SQUARES];
        currentNumberIndex   = INITIAL_STAT;
        successfulPlacements = INITIAL_STAT;
        gamesPlayed          = INITIAL_STAT;
        gamesWon             = INITIAL_STAT;
        gridButtons          = new Button[ROWS][COLS];
        statusLabel          = new Label();
        currentNumberLabel   = new Label();
    }

    /**
     * Overrides base start function from application.
     * @param primaryStage main stage for the game
     */
    @Override
    public void start(final Stage primaryStage)
    {
        initializeUI(primaryStage);
    }

    /**
     * Initializes the UI components and sets up the game.
     *
     * @param primaryStage The primary stage for the application.
     */
    private void initializeUI(final Stage primaryStage)
    {
        primaryStage.setTitle("Number Game");
        final VBox root;
        final GridPane grid;
        final Scene scene;

        grid = createGrid();
        root = createRootLayout(grid);

        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS_PATH)).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.toFront();
        primaryStage.requestFocus();
    }

    /**
     * Creates the grid of buttons for the game.
     *
     * @return The initialized GridPane.
     */
    private GridPane createGrid()
    {
        final GridPane grid;
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(PADDING);
        grid.setVgap(PADDING);
        grid.setPadding(new Insets(PADDING));

        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                final Button button;
                button = createGridButton(row, col);
                gridButtons[row][col] = button;
                grid.add(button, col, row);
            }
        }

        return grid;
    }

    /**
     * Creates a button for the grid.
     *
     * @param row The row of the button.
     * @param col The column of the button.
     * @return The initialized Button.
     */
    private Button createGridButton(final int row,
                                    final int col)
    {
        final Button button;
        button = new Button();

        button.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setDisable(true);
        button.setOnAction(e -> handleButtonClick(row, col));
        return button;
    }

    /**
     * Creates the root layout for the game.
     *
     * @param grid The grid of buttons.
     * @return The initialized VBox.
     */
    private VBox createRootLayout(final GridPane grid)
    {
        final Label titleLabel;
        final Button startButton;
        final VBox root;

        startButton = new Button("Start Game");
        startButton.setOnAction(e -> startNewGame());

        titleLabel = new Label("Number Placement Game");
        titleLabel.setId("title");

        statusLabel.setText("Click 'Start Game' to begin.");

        root = new VBox(ROOT_PADDING, titleLabel, statusLabel, currentNumberLabel, grid, startButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(ROOT_PADDING));

        return root;
    }

    /**
     * Starts a new game by resetting the game state and generating new random numbers.
     */
    @Override
    public void startNewGame()
    {
        resetGameState();
        enableGridButtons();
        generateRandomNumbers();
        updateStatusLabels();
    }

    /**
     * Resets the game state.
     */
    private void resetGameState()
    {
        currentNumberIndex   = INITIAL_STAT;
        successfulPlacements = INITIAL_STAT;
        gamesPlayed++;
    }

    /**
     * Enables all buttons in the grid.
     */
    private void enableGridButtons()
    {
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                gridButtons[row][col].setText("");
                gridButtons[row][col].setDisable(false);
            }
        }
    }

    /**
     * Generates random numbers for the game.
     */
    private void generateRandomNumbers()
    {
        final Random rand;
        rand = new Random();
        for (int i = 0; i < TOTAL_SQUARES; i++)
        {
            numbers[i] = rand.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
        }
    }

    /**
     * Updates the status labels.
     */
    private void updateStatusLabels()
    {
        statusLabel.setText("Click a square to place the next number.");
        currentNumberLabel.setText("Current number to place: " + numbers[currentNumberIndex]);
    }

    /**
     * Handles button clicks on the grid.
     *
     * @param row The row of the clicked button.
     * @param col The column of the clicked button.
     */
    private void handleButtonClick(final int row,
                                   final int col)
    {
        if (currentNumberIndex >= TOTAL_SQUARES)
        {
            return;
        }

        final Button button;
        button = gridButtons[row][col];
        if (!button.getText().isEmpty())
        {
            return;
        }

        button.setText(String.valueOf(numbers[currentNumberIndex]));

        if (isAscendingOrder())
        {
            successfulPlacements++;
            currentNumberIndex++;

            if (currentNumberIndex < TOTAL_SQUARES)
            {
                currentNumberLabel.setText("Current number to place: " + numbers[currentNumberIndex]);
            }

            if (currentNumberIndex == TOTAL_SQUARES)
            {
                endGame(true);
            }
        }
        else
        {
            endGame(false);
        }
    }

    /**
     * Checks if the numbers in the grid are in ascending order.
     *
     * @return True if the numbers are in ascending order, false otherwise.
     */
    private boolean isAscendingOrder()
    {
        int previousNumber;
        previousNumber = INITIAL_NUM;

        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                final String text;
                text = gridButtons[row][col].getText();
                if (!text.isEmpty())
                {
                    final int currentNumber;
                    currentNumber = Integer.parseInt(text);
                    if (currentNumber < previousNumber)
                    {
                        return false;
                    }
                    previousNumber = currentNumber;
                }
            }
        }
        return true;
    }

    /**
     * Ends the game and displays the result.
     *
     * @param won Whether the player won the game.
     */
    @Override
    public void endGame(final boolean won)
    {
        disableGridButtons();
        showGameResult(won);
        if (won)
        {
            gamesWon++;
        }
    }

    /**
     * Disables all buttons in the grid.
     */
    private void disableGridButtons()
    {
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                gridButtons[row][col].setDisable(true);
            }
        }
    }

    /**
     * Displays the game result in an alert window.
     *
     * @param won Whether the player won the game.
     */
    @Override
    public void showGameResult(final boolean won)
    {
        final GameResultPopup popup;
        final String scoreMessage;
        final String resultMessage;
        resultMessage = won ? "Congratulations! You won the game." : "Game over! You lost.";

        scoreMessage = String.format(
                "Games Played: %d, Games Won: %d, Successful Placements: %d, Average: %.2f",
                gamesPlayed, gamesWon, successfulPlacements,
                (double) successfulPlacements / gamesPlayed
        );

        popup = new GameResultPopup(resultMessage + "\n\n" + scoreMessage, this);
        popup.show();
    }

    /**
     * Gets specific grid button
     * @return grid button
     */
    public Button getGridButtons(final int x,
                                 final int y)
    {
        if (x >= gridButtons.length ||
                x < INITIAL_STAT ||
                y >= gridButtons.length ||
                y < INITIAL_STAT)
        {
            throw new IllegalArgumentException("Attempting to get invalid grid button");
        }
        return gridButtons[x][y];
    }
}