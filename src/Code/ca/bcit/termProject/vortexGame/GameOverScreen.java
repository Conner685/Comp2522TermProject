package ca.bcit.termProject.vortexGame;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Represents the game over screen with options to retry or return to menu.
 */
public class GameOverScreen extends Pane
{
    private static final int MENU_ITEM_SPACING = 20;
    private static final int TITLE_OFFSET_Y = 100;
    private static final int SCORE_OFFSET_Y = 150;
    private static final int BUTTON_OFFSET_Y = 200;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;

    private final VortexGameEngine gameEngine;
    private final long survivalTime;

    /**
     * Constructs the game over screen.
     * @param gameEngine The game engine instance
     * @param survivalTime The player's survival time in seconds
     */
    public GameOverScreen(final VortexGameEngine gameEngine,
                          final long survivalTime)
    {
        this.gameEngine = gameEngine;
        this.survivalTime = survivalTime;
        createContent();
    }

    /**
     * Creates and configures the game over content.
     */
    private void createContent()
    {
        final Text title;
        final Text scoreText;
        final Button retryButton;
        final Button menuButton;
        final Button quitButton;

        title = new Text("Game Over");
        title.getStyleClass().add("game-over-title");
        title.setX(VortexGameEngine.HALF_SCREEN_WIDTH - 100);
        title.setY(TITLE_OFFSET_Y);

        scoreText = new Text("Survival Time: " + survivalTime + "s");
        scoreText.getStyleClass().add("game-over-score");
        scoreText.setX(VortexGameEngine.HALF_SCREEN_WIDTH - 100);
        scoreText.setY(SCORE_OFFSET_Y);

        retryButton = new Button("Retry");
        retryButton.getStyleClass().add("menu-button");
        retryButton.setLayoutX(VortexGameEngine.HALF_SCREEN_WIDTH - BUTTON_WIDTH / 2);
        retryButton.setLayoutY(BUTTON_OFFSET_Y);
        retryButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        retryButton.setOnAction(e -> gameEngine.startGame());

        menuButton = new Button("Main Menu");
        menuButton.getStyleClass().add("menu-button");
        menuButton.setLayoutX(VortexGameEngine.HALF_SCREEN_WIDTH - BUTTON_WIDTH / 2);
        menuButton.setLayoutY(BUTTON_OFFSET_Y + BUTTON_HEIGHT + MENU_ITEM_SPACING);
        menuButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        menuButton.setOnAction(e -> gameEngine.showMainMenu());

        quitButton = new Button("Quit");
        quitButton.getStyleClass().add("menu-button");
        quitButton.setLayoutX(VortexGameEngine.HALF_SCREEN_WIDTH - BUTTON_WIDTH / 2);
        quitButton.setLayoutY(BUTTON_OFFSET_Y + 2 * (BUTTON_HEIGHT + MENU_ITEM_SPACING));
        quitButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        quitButton.setOnAction(e -> ((Stage) gameEngine.getRoot().getScene().getWindow()).close());

        getChildren().addAll(title, scoreText, retryButton, menuButton, quitButton);
    }
}