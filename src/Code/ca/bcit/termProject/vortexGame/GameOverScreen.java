package ca.bcit.termProject.vortexGame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;

import static ca.bcit.termProject.vortexGame.Star.spawnStars;

/**
 * Represents the game over screen with options to retry or return to menu.
 */
public class GameOverScreen extends Pane
{
    private static final int MENU_ITEM_SPACING = 25;
    private static final int TITLE_OFFSET_Y = 120;
    private static final int SCORE_OFFSET_Y = 180;
    private static final int BUTTON_OFFSET_Y = 240;
    private static final int BUTTON_WIDTH = 240;
    private static final int BUTTON_HEIGHT = 60;
    private static final int TITLE_X_OFFSET = 120;
    private static final int HALF_BUTTON_WIDTH = BUTTON_WIDTH / 2;
    private static final int BUTTON_SPACING = BUTTON_HEIGHT + MENU_ITEM_SPACING;
    private static final double GLOW_LEVEL = 0.3;
    private static final double SHADOW_RADIUS = 10.0;
    private static final Color SHADOW_COLOR = Color.rgb(200, 0, 0, 0.9);

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
     * Creates and configures the game over content with enhanced UI elements.
     */
    private void createContent()
    {
        final DropShadow textShadow;
        final Glow buttonGlow;

        final Text title;
        final Text scoreText;
        final Button retryButton;
        final Button menuButton;
        final Button quitButton;

        retryButton = new Button("RETRY");
        menuButton = new Button("MAIN MENU");
        quitButton = new Button("QUIT");
        scoreText = new Text("Survival Time: " + survivalTime + " seconds");
        title = new Text("GAME OVER");
        textShadow = new DropShadow(SHADOW_RADIUS, SHADOW_COLOR);
        buttonGlow = new Glow(GLOW_LEVEL);

        // Title styling
        title.getStyleClass().add("game-over-title");
        title.setEffect(textShadow);
        title.setX(VortexGameEngine.HALF_SCREEN_WIDTH - TITLE_X_OFFSET);
        title.setY(TITLE_OFFSET_Y);

        // Score styling
        scoreText.getStyleClass().add("game-over-score");
        scoreText.setEffect(textShadow);
        scoreText.setX(VortexGameEngine.HALF_SCREEN_WIDTH - TITLE_X_OFFSET);
        scoreText.setY(SCORE_OFFSET_Y);

        // Button styling and effects
        styleButton(retryButton, VortexGameEngine.HALF_SCREEN_WIDTH - HALF_BUTTON_WIDTH,
                BUTTON_OFFSET_Y, buttonGlow, e -> gameEngine.startGame());

        styleButton(menuButton, VortexGameEngine.HALF_SCREEN_WIDTH - HALF_BUTTON_WIDTH,
                BUTTON_OFFSET_Y + BUTTON_SPACING, buttonGlow, e -> gameEngine.showMainMenu());

        styleButton(quitButton, VortexGameEngine.HALF_SCREEN_WIDTH - HALF_BUTTON_WIDTH,
                BUTTON_OFFSET_Y + 2 * BUTTON_SPACING, buttonGlow,
                e -> ((Stage) gameEngine.getRoot().getScene().getWindow()).close());

        spawnStars(gameEngine);
        getChildren().addAll(title, scoreText, retryButton, menuButton, quitButton);
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