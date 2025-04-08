package ca.bcit.termProject.vortexGame;

import javafx.scene.effect.BoxBlur;

import java.util.Random;

/**
 * Represents decorative star elements that form the game's background.
 *
 * <p>This class creates a visually appealing starfield effect by:
 * <ul>
 *   <li>Generating random star positions across the game area</li>
 *   <li>Varying star sizes within defined bounds</li>
 *   <li>Applying consistent visual styling and effects</li>
 *   <li>Maintaining optimal rendering performance</li>
 * </ul>
 *
 * <p>Key Characteristics:
 * <table border="1">
 *   <tr><th>Feature</th><th>Description</th></tr>
 *   <tr><td>Quantity</td><td>10-50 randomly placed stars</td></tr>
 *   <tr><td>Size</td><td>5-15 pixels in diameter</td></tr>
 *   <tr><td>Appearance</td><td>45° rotation with blur effect</td></tr>
 *   <tr><td>Rendering</td><td>Drawn first as background elements</td></tr>
 * </table>
 *
 * <p>Performance Considerations:
 * <ul>
 *   <li>Uses shared BoxBlur instance for efficient rendering</li>
 *   <li>Static spawn method minimizes object creation overhead</li>
 *   <li>CSS styling for maintainable visual properties</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class Star extends GameObject
{
    private static final Random RAND        = new Random();
    private static final int MAX_SIZE       = 15;
    private static final int MIN_SIZE       = 5;
    private static final int ROTATION       = 45;
    private static final int MIN_STARS      = 10;
    private static final int MAX_STARS      = 50;
    private static final BoxBlur STAR_BLUR  = new BoxBlur();
    private static final int STAR_BLUR_INTENSITY = 5;

    /**
     * Creates a new star instance with random position and size.
     *
     * <p>Each star:
     * <ul>
     *   <li>Positions randomly within game bounds</li>
     *   <li>Scales between {@link #MIN_SIZE}-{@link #MAX_SIZE}</li>
     *   <li>Applies standard rotation and blur effects</li>
     *   <li>Uses CSS class for consistent styling</li>
     * </ul>     */
    public Star()
    {
        super(RAND.nextInt(VortexGameEngine.SCREEN_WIDTH_PX),
                RAND.nextInt( VortexGameEngine.SCREEN_HEIGHT_PX),
                RAND.nextInt(MAX_SIZE - MIN_SIZE) + MIN_SIZE);

        getStyleClass().add("star");
        setRotate(ROTATION);
        STAR_BLUR.setWidth(STAR_BLUR_INTENSITY);
        STAR_BLUR.setHeight(STAR_BLUR_INTENSITY);
    }

    /**
     * Generates the complete starfield background for a game session.
     *
     * <p>This static method:
     * <ul>
     *   <li>Creates 10-50 star instances (random quantity)</li>
     *   <li>Adds stars to the beginning of scene graph (background)</li>
     *   <li>Shares common visual effects across all stars</li>
     *   <li>Optimizes performance through batch creation</li>
     * </ul>
     *
     * @param gameEngine The active game engine instance
     */
    public static void spawnStars(final VortexGameEngine gameEngine)
    {
        final Random rand;
        final int totalStars;

        rand = new Random();
        totalStars = rand.nextInt(MAX_STARS - MIN_STARS) + MIN_STARS;

        for (int i = 0; i < totalStars; i++)
        {
            final Star star;

            star = new Star();

            star.setEffect(STAR_BLUR);

            gameEngine.getRoot().getChildren().addFirst(star);
        }
    }
}
