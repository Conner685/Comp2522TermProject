package ca.bcit.termProject.numberGame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

import static ca.bcit.termProject.numberGame.NumberGame.*;

/**
 * Abstract base class for popups.
 */
public abstract class BasePopup
{
    protected final Stage alertStage;
    protected final VBox alertBox;
    private static final int ALERT_WIDTH = 650;
    private static final int ALERT_HEIGHT = 300;

    /**
     * Constructor for BasePopup.
     *
     * @param message The message to display in the popup.
     */
    public BasePopup(final String message)
    {
        alertStage = new Stage();
        alertBox = new VBox(PADDING, new Label(message));
        alertBox.setAlignment(Pos.CENTER);
        alertBox.setPadding(new Insets(ROOT_PADDING));
    }

    /**
     * Displays the popup.
     */
    public void show()
    {
        final Scene alertScene;
        alertScene = new Scene(alertBox, ALERT_WIDTH, ALERT_HEIGHT);
        alertScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS_PATH)).toExternalForm());

        alertStage.setScene(alertScene);
        alertStage.show();
    }
}