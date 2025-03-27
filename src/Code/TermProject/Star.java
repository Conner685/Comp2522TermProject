package TermProject;

import java.util.Random;

public class Star extends GameObject
{
    private static final Random RAND = new Random();
    private static final int MAX_SIZE = 15;
    private static final int MIN_SIZE = 5;
    private static final int ROTATION = 45;

    public Star()
    {
        super(RAND.nextInt(VortexGameEngine.getScreenWidth()),
                RAND.nextInt(VortexGameEngine.getScreenHeight()),
                RAND.nextInt(MAX_SIZE - MIN_SIZE) + MIN_SIZE);
        getStyleClass().add("star");
        setRotate(ROTATION);
    }
}
