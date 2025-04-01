package ca.bcit.termProject.numberGame;

import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Concrete class for game result popups.
 * @author Conner Ponton
 * @version 1.0
 */
public final class GameResultPopup
        extends BasePopup
{
    private static final int GRID_BASE = 0;
    /**
     * Constructor for GameResultPopup.
     *
     * @param message The message to display in the popup.
     * @param game    The NumberGame instance.
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