package ca.bcit.termProject.numberGame;

import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * A specialized popup window that displays game results and provides post-game options.
 *
 * <p>This concrete implementation extends {@link BasePopup} to provide:
 * <ul>
 *   <li>Customizable result messaging</li>
 *   <li>Game restart functionality</li>
 *   <li>Application exit option</li>
 *   <li>Consistent visual styling</li>
 * </ul>
 *
 * <p>Behavior Characteristics:
 * <table border="1">
 *   <tr><th>Action</th><th>Result</th></tr>
 *   <tr><td>Play Again</td><td>Restarts game via {@link NumberGame#startNewGame()}</td></tr>
 *   <tr><td>Quit</td><td>Closes both popup and main game window</td></tr>
 * </table>
 *
 * <p>UI Structure:
 * <ul>
 *   <li>Inherits base layout from {@code BasePopup}</li>
 *   <li>Adds two action buttons horizontally</li>
 *   <li>Maintains consistent button styling</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class GameResultPopup
        extends BasePopup
{
    private static final int GRID_BASE = 0;

    /**
     * Constructs a new result popup with game-specific actions.
     *
     * @param message The result message to display (typically includes game statistics)
     * @param game The active game instance for callback operations
     */
    public GameResultPopup(final String message,
                           final NumberGame game)
    {
        super(message);
        final Button quitButton;
        final Button playAgainButton;


        playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(e ->
        {
            alertStage.close();
            game.startNewGame();
        });
        quitButton = new Button("Quit");
        quitButton.setOnAction(e ->
        {
            alertStage.close();
            ((Stage) game.getGridButtons(GRID_BASE, GRID_BASE).getScene().getWindow()).close();
        });

        alertBox.getChildren().addAll(playAgainButton, quitButton);
    }
}