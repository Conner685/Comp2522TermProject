package ca.bcit.termProject.numberGame;

import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Concrete class for game result popups.
 */
public class GameResultPopup extends BasePopup
{
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

        final Button playAgainButton;
        playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(e ->
        {
            alertStage.close();
            game.startNewGame();
        });

        final Button quitButton;
        quitButton = new Button("Quit");
        quitButton.setOnAction(e ->
        {
            alertStage.close();
            ((Stage) game.getGridButtons(0, 0).getScene().getWindow()).close();
        });

        alertBox.getChildren().addAll(playAgainButton, quitButton);
    }
}