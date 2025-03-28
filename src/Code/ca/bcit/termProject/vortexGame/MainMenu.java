package ca.bcit.termProject.vortexGame;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;

/**
 * Represents the main menu screen with leaderboard and start options.
 */
public class MainMenu extends Pane
{
    private static final int MENU_ITEM_SPACING = 20;
    private static final int TITLE_OFFSET_Y = 100;
    private static final int LEADERBOARD_OFFSET_Y = 150;
    private static final int BUTTON_OFFSET_Y = 400;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;

    private final VortexGameEngine gameEngine;

    /**
     * Constructs the main menu.
     * @param gameEngine The game engine instance
     */
    public MainMenu(final VortexGameEngine gameEngine)
    {
        this.gameEngine = gameEngine;
        createContent();
    }

    /**
     * Creates and configures the menu content.
     */
    private void createContent()
    {
        final Text title;
        final VBox leaderboard;
        final Button startButton;
        final Button quitButton;

        title = new Text("Vortex - Bullet Hell");
        title.getStyleClass().add("title-text");
        title.setX(VortexGameEngine.HALF_SCREEN_WIDTH - 150);
        title.setY(TITLE_OFFSET_Y);

        leaderboard = new VBox(MENU_ITEM_SPACING);
        leaderboard.setLayoutX(VortexGameEngine.HALF_SCREEN_WIDTH - 100);
        leaderboard.setLayoutY(LEADERBOARD_OFFSET_Y);

        final Text leaderboardTitle;
        leaderboardTitle = new Text("Top Scores:");
        leaderboardTitle.getStyleClass().add("leaderboard-title");
        leaderboard.getChildren().add(leaderboardTitle);

        final List<String> scores;
        scores = ScoreManager.getHighestNScores(10);
        for (final String score : scores)
        {
            final Text scoreText;
            scoreText = new Text(score);
            scoreText.getStyleClass().add("leaderboard-score");
            leaderboard.getChildren().add(scoreText);
        }

        startButton = new Button("Start Game");
        startButton.getStyleClass().add("menu-button");
        startButton.setLayoutX(VortexGameEngine.HALF_SCREEN_WIDTH - (double) BUTTON_WIDTH / 2);
        startButton.setLayoutY(BUTTON_OFFSET_Y);
        startButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.setOnAction(e -> gameEngine.startGame());

        quitButton = new Button("Quit");
        quitButton.getStyleClass().add("menu-button");
        quitButton.setLayoutX(VortexGameEngine.HALF_SCREEN_WIDTH - (double) BUTTON_WIDTH / 2);
        quitButton.setLayoutY(BUTTON_OFFSET_Y + BUTTON_HEIGHT + MENU_ITEM_SPACING);
        quitButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        quitButton.setOnAction(e -> ((Stage) gameEngine.getRoot().getScene().getWindow()).close());

        getChildren().addAll(title, leaderboard, startButton, quitButton);
    }
}