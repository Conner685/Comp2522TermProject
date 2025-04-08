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
 * Represents the game over screen displayed after player defeat.
 *
 * <p>This screen provides:
 * <ul>
 *   <li>Final survival time display</li>
 *   <li>Options to retry or return to main menu</li>
 *   <li>Dramatic visual presentation</li>
 *   <li>Consistent UI styling</li>
 * </ul>
 *
 * <p>UI Characteristics:
 * <table border="1">
 *   <tr><th>Component</th><th>Features</th></tr>
 *   <tr><td>Title</td><td>Red-tinged drop shadow, large font</td></tr>
 *   <tr><td>Score Display</td><td>Formatted survival time</td></tr>
 *   <tr><td>Buttons</td><td>Glow effect on hover, consistent sizing</td></tr>
 *   <tr><td>Background</td><td>Animated star particles</td></tr>
 * </table>
 *
 * <p>Behavioral Flow:
 * <ul>
 *   <li>Retry - Immediately starts new game</li>
 *   <li>Main Menu - Returns to title screen</li>
 *   <li>Quit - Exits application</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class GameOverScreen extends Pane
{
    private static final int MENU_ITEM_SPACING = 25;
    private static final int TITLE_OFFSET_Y = 120;
    private static final int SCORE_OFFSET_Y = 180;
    private static final int BUTTON_OFFSET_Y = 240;
    private static final int QUIT_BUTTON_OFFSET_Y = 2;
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
     * Constructs a new game over screen with survival statistics.
     *
     * <p>Initialization includes:
     * <ul>
     *   <li>Displaying final survival time</li>
     *   <li>Creating navigation options</li>
     *   <li>Setting up visual effects</li>
     *   <li>Adding background elements</li>
     * </ul>
     *
     * @param gameEngine The main game controller
     * @param survivalTime Player's survival duration in seconds
     */
    public GameOverScreen(final VortexGameEngine gameEngine,
                          final long survivalTime)
    {
        this.gameEngine = gameEngine;
        this.survivalTime = survivalTime;
        createContent();
    }

    /*
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
        title.setX(VortexGameEngine.HALF_SCREEN_WIDTH_PX - TITLE_X_OFFSET);
        title.setY(TITLE_OFFSET_Y);

        // Score styling
        scoreText.getStyleClass().add("game-over-score");
        scoreText.setEffect(textShadow);
        scoreText.setX(VortexGameEngine.HALF_SCREEN_WIDTH_PX - TITLE_X_OFFSET);
        scoreText.setY(SCORE_OFFSET_Y);

        // Button styling and effects
        styleButton(retryButton, VortexGameEngine.HALF_SCREEN_WIDTH_PX - HALF_BUTTON_WIDTH,
                BUTTON_OFFSET_Y, buttonGlow, e -> gameEngine.startGame());

        styleButton(menuButton, VortexGameEngine.HALF_SCREEN_WIDTH_PX - HALF_BUTTON_WIDTH,
                BUTTON_OFFSET_Y + BUTTON_SPACING, buttonGlow, e -> gameEngine.showMainMenu());

        styleButton(quitButton, VortexGameEngine.HALF_SCREEN_WIDTH_PX - HALF_BUTTON_WIDTH,
                BUTTON_OFFSET_Y + QUIT_BUTTON_OFFSET_Y * BUTTON_SPACING, buttonGlow,
                e -> ((Stage) gameEngine.getRoot().getScene().getWindow()).close());

        spawnStars(gameEngine);
        getChildren().addAll(title, scoreText, retryButton, menuButton, quitButton);
    }

    /**
     * Applies standardized styling to navigation buttons.
     *
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