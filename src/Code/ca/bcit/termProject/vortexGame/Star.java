package ca.bcit.termProject.vortexGame;

import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Pane;

import java.util.Random;

public class Star extends GameObject
{
    private static final Random RAND = new Random();
    private static final int MAX_SIZE = 15;
    private static final int MIN_SIZE = 5;
    private static final int ROTATION = 45;
    private static final int MIN_STARS = 10;
    private static final int MAX_STARS = 50;
    private static final BoxBlur STAR_BLUR = new BoxBlur();
    private static final int STAR_BLUR_INTENSITY = 5;

    public Star()
    {
        super(RAND.nextInt(VortexGameEngine.SCREEN_WIDTH),
                RAND.nextInt( VortexGameEngine.SCREEN_HEIGHT),
                RAND.nextInt(MAX_SIZE - MIN_SIZE) + MIN_SIZE);
        getStyleClass().add("star");
        setRotate(ROTATION);
        STAR_BLUR.setWidth(STAR_BLUR_INTENSITY);
        STAR_BLUR.setHeight(STAR_BLUR_INTENSITY);
    }

    /**
     * Creates background star elements for visual effect.
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
