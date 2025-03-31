package ca.bcit.termProject.vortexGame;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.List;

import static ca.bcit.termProject.vortexGame.Star.spawnStars;

/**
 * Represents the main menu screen with leaderboard and start options.
 */
public class MainMenu extends Pane
{
    private static final int MENU_ITEM_SPACING = 8;
    private static final int TITLE_OFFSET_Y = 120;
    private static final int LEADERBOARD_OFFSET_Y = 180;
    private static final int BUTTON_OFFSET_Y = 400;
    private static final int BUTTON_WIDTH = 240;
    private static final int BUTTON_HEIGHT = 60;
    private static final int SCORE_INDEX_SHIFT = 1;
    private static final int TUTORIAL_OFFSET_Y = 560;
    private static final int TUTORIAL_TITLE_OFFSET_Y = 540;
    private static final int TITLE_X_OFFSET = 180;
    private static final int LEADERBOARD_X_OFFSET = 120;
    private static final int HALF_BUTTON_WIDTH = BUTTON_WIDTH / 2;
    private static final double GLOW_LEVEL = 0.3;
    private static final double SHADOW_RADIUS = 10.0;
    private static final Color SHADOW_COLOR = Color.rgb(100, 100, 255, 0.9);
    private static final int MAX_SCORES_DISPLAYED = 10;
    private static final int COLUMN_SPACING = 40;
    private static final int SCORE_SPACING = 10;
    private static final int TOP_SCORES_COUNT = 5;

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
     * Creates and configures the menu content with enhanced UI elements.
     */
    private void createContent()
    {
        final DropShadow textShadow;
        final Glow buttonGlow;
        final Text title;
        final VBox leaderboard;
        final Button startButton;
        final Button quitButton;
        final Text tutorialTitle;
        final Text tutorial;
        final Text leaderboardTitle;
        final StringBuilder tutorialText;

        textShadow       = new DropShadow(SHADOW_RADIUS, SHADOW_COLOR);
        buttonGlow       = new Glow(GLOW_LEVEL);
        title            = new Text("VORTEX - BULLET HELL");
        leaderboard      = new VBox(MENU_ITEM_SPACING);
        startButton      = new Button("START GAME");
        quitButton       = new Button("QUIT");
        tutorialTitle    = new Text("HOW TO PLAY");
        tutorial         = new Text();
        leaderboardTitle = new Text("TOP SCORES:");
        tutorialText     = new StringBuilder();

        setupTitle(title, textShadow);
        setupLeaderboard(leaderboard, leaderboardTitle, textShadow);
        setupButtons(startButton, quitButton, buttonGlow);
        setupTutorial(tutorialTitle, tutorial, textShadow, tutorialText);

        spawnStars(gameEngine);
        getChildren().addAll(title, leaderboard, startButton, quitButton, tutorialTitle, tutorial);
    }

    /**
     * Sets up the main title styling.
     * @param title The title text
     * @param shadow The drop shadow effect
     */
    private void setupTitle(final Text title,
                            final DropShadow shadow)
    {
        title.getStyleClass().add("title-text");
        title.setEffect(shadow);
        title.setX(VortexGameEngine.HALF_SCREEN_WIDTH - TITLE_X_OFFSET);
        title.setY(TITLE_OFFSET_Y);
    }

    /**
     * Sets up the leaderboard with two columns of scores.
     * @param leaderboard The leaderboard container
     * @param title The leaderboard title
     * @param shadow The drop shadow effect
     */
    private void setupLeaderboard(final VBox leaderboard,
                                  final Text title,
                                  final DropShadow shadow)
    {
        final HBox scoreColumns;
        final VBox leftColumn;
        final VBox rightColumn;
        final List<String> allScores;

        leaderboard.setLayoutX(VortexGameEngine.HALF_SCREEN_WIDTH - LEADERBOARD_X_OFFSET);
        leaderboard.setLayoutY(LEADERBOARD_OFFSET_Y);

        title.getStyleClass().add("leaderboard-title");
        title.setEffect(shadow);
        leaderboard.getChildren().add(title);

        scoreColumns = new HBox(COLUMN_SPACING);
        leftColumn = new VBox(SCORE_SPACING);
        rightColumn = new VBox(SCORE_SPACING);
        allScores = ScoreManager.getHighestNScores(MAX_SCORES_DISPLAYED);

        scoreColumns.setAlignment(Pos.CENTER);
        addScoresToColumn(leftColumn, allScores, 0, TOP_SCORES_COUNT, shadow);
        addScoresToColumn(rightColumn, allScores, TOP_SCORES_COUNT, MAX_SCORES_DISPLAYED, shadow);

        scoreColumns.getChildren().addAll(leftColumn, rightColumn);
        leaderboard.getChildren().add(scoreColumns);
    }

    /**
     * Adds scores to a column within the specified range.
     * @param column The column to add scores to
     * @param scores The list of all scores
     * @param start The starting index
     * @param end The ending index
     * @param shadow The drop shadow effect
     */
    private void addScoresToColumn(final VBox column,
                                   final List<String> scores,
                                   final int start,
                                   final int end,
                                   final DropShadow shadow)
    {
        for (int i = start; i < end && i < scores.size(); i++)
        {
            final Text scoreText;
            scoreText = new Text((i + SCORE_INDEX_SHIFT) + ". " + scores.get(i));
            scoreText.getStyleClass().add("leaderboard-score");
            scoreText.setEffect(shadow);
            column.getChildren().add(scoreText);
        }
    }

    /**
     * Sets up the tutorial section.
     * @param title The tutorial title
     * @param tutorial The tutorial text
     * @param shadow The drop shadow effect
     * @param tutorialText The string builder for tutorial text
     */
    private void setupTutorial(final Text title,
                               final Text tutorial,
                               final DropShadow shadow,
                               final StringBuilder tutorialText)
    {
        title.getStyleClass().add("title-text");
        title.setEffect(shadow);
        title.setY(TUTORIAL_TITLE_OFFSET_Y);

        tutorialText.append("• Survive as long as possible against swarms of meteors\n");
        tutorialText.append("• Movement: WASD keys\n");
        tutorialText.append("• Boost: SHIFT key (limited resource)\n");
        tutorialText.append("• Collect power-ups:\n");
        tutorialText.append("   BLUE - Speed increase\n");
        tutorialText.append("   PURPLE - Boost capacity\n");
        tutorialText.append("   GREEN - Full boost recharge\n");
        tutorialText.append("• Good luck and aim for the top!");

        tutorial.setText(tutorialText.toString());
        tutorial.getStyleClass().add("tutorial-text");
        tutorial.setY(TUTORIAL_OFFSET_Y);
    }

    /**
     * Sets up the menu buttons.
     * @param startButton The start game button
     * @param quitButton The quit button
     * @param glow The glow effect
     */
    private void setupButtons(final Button startButton,
                              final Button quitButton,
                              final Glow glow)
    {
        styleButton(startButton,
                VortexGameEngine.HALF_SCREEN_WIDTH - HALF_BUTTON_WIDTH,
                BUTTON_OFFSET_Y,
                glow,
                e -> gameEngine.startGame());

        styleButton(quitButton,
                VortexGameEngine.HALF_SCREEN_WIDTH - HALF_BUTTON_WIDTH,
                BUTTON_OFFSET_Y + BUTTON_HEIGHT + MENU_ITEM_SPACING,
                glow,
                e -> ((Stage) gameEngine.getRoot().getScene().getWindow()).close());
    }

    /**
     * Styles a button with consistent appearance and hover effects.
     * @param button The button to style
     * @param x X position
     * @param y Y position
     * @param glow Glow effect
     * @param action Event handler
     */
    private void styleButton(final Button button,
                             final double x,
                             final double y,
                             final Glow glow,
                             final EventHandler<ActionEvent> action)
    {
        button.getStyleClass().add("menu-button");
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setOnAction(action);

        button.setOnMouseEntered(e -> button.setEffect(glow));
        button.setOnMouseExited(e -> button.setEffect(null));
    }
}