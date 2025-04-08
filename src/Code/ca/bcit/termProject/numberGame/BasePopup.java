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
 * Abstract base class for creating standardized popup dialogs in the number game.
 *
 * <p>This class provides foundational functionality for:
 * <ul>
 *   <li>Consistent popup sizing and styling</li>
 *   <li>Message display infrastructure</li>
 *   <li>Basic layout management</li>
 *   <li>CSS styling integration</li>
 * </ul>
 *
 * <p>Core Characteristics:
 * <table border="1">
 *   <tr><th>Feature</th><th>Specification</th></tr>
 *   <tr><td>Dimensions</td><td>650x300 pixels (fixed)</td></tr>
 *   <tr><td>Layout</td><td>Vertical box (VBox) with consistent padding</td></tr>
 *   <tr><td>Styling</td><td>Uses application CSS ({@value NumberGame#CSS_PATH})</td></tr>
 *   <tr><td>Positioning</td><td>Centered content alignment</td></tr>
 * </table>
 *
 * <p>Implementation Notes:
 * <ul>
 *   <li>Designed for extension by concrete popup types</li>
 *   <li>Manages JavaFX stage/scene creation</li>
 *   <li>Provides protected access to core components</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public abstract class BasePopup
{
    private static final int ALERT_WIDTH  = 650;
    private static final int ALERT_HEIGHT = 300;

    protected final Stage alertStage;
    protected final VBox alertBox;

    /**
     * Constructs a new base popup with the specified message.
     *
     * @param message The informational content to display (required)
     */
    public BasePopup(final String message)
    {
        alertStage = new Stage();
        alertBox = new VBox(PADDING, new Label(message));
        alertBox.setAlignment(Pos.CENTER);
        alertBox.setPadding(new Insets(ROOT_PADDING));
    }

    /**
     * Displays the constructed popup window.
     *
     * <p>This final method:
     * <ul>
     *   <li>Applies the scene styling</li>
     *   <li>Sets the fixed window dimensions</li>
     *   <li>Makes the window visible</li>
     * </ul>
     */
    public final void show()
    {
        final Scene alertScene;
        alertScene = new Scene(alertBox, ALERT_WIDTH, ALERT_HEIGHT);
        alertScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CSS_PATH)).toExternalForm());

        alertStage.setScene(alertScene);
        alertStage.show();
    }
}