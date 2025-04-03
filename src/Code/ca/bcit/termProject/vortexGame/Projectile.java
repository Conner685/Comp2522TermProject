package ca.bcit.termProject.vortexGame;

import java.util.Random;

/**
 * Represents a projectile in the game that can move towards a target direction.
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class Projectile extends GameObject
{
    private static final int MIN_PROJECTILE_SPEED = 5;
    private static final int MAX_PROJECTILE_SPEED = 20;
    private static final int SIZE_SPEED_MODIFIER  = 5; // Modifier for speed based on projectile size
    private static final int CENTER_BOX_MAX       = 550; // Max bound for the center of the screen
    private static final int CENTER_BOX_MIN       = 200; // Min bound for the center of the screen
    private static final int MAP_EDGE             = -100; // Edge of the screen with space for projectiles
    private static final int MIN_SIZE_AFFECT      = 10; // Minimum size to affect speed calculation
    private static final Random RAND = new Random();
    private static final int DIR_X = 0;
    private static final int DIR_Y = 1;
    private static final int MIN_SIZE = 0;
    private static final int MAX_INITIAL_ANGLE = 90;
    private static final int RIGHT = 3;
    private static final int LEFT = 1;
    private static final int MIN_MAG = 0;

    private final double directionX;
    private final double directionY;
    private final int speed;
    private double currRot;
    private final int rotDir;

    /**
     * Constructs a Projectile with the specified position and size.
     *
     * @param x    the x-coordinate of the projectile
     * @param y    the y-coordinate of the projectile
     * @param size the size of the projectile (must be positive)
     * @throws IllegalArgumentException if size is not positive
     */
    public Projectile(final double x,
                      final double y,
                      final double size)
    {
        super(x, y, size);

        validateProjectile(size);

        final double[] direction;

        direction = calculateDirection(x, y);

        getStyleClass().add("projectile");
        this.speed = calculateSpeed(size);
        this.directionX = direction[DIR_X];
        this.directionY = direction[DIR_Y];
        this.currRot = RAND.nextDouble(MAX_INITIAL_ANGLE);
        this.rotDir = RAND.nextInt(RIGHT) - LEFT;
    }

    /**
     * Calculates the speed of the projectile based on its size.
     *
     * @param size the size of the projectile
     * @return the calculated speed
     */
    private int calculateSpeed(final double size)
    {
        if (size < MIN_SIZE_AFFECT)
        {
            return RAND.nextInt((MAX_PROJECTILE_SPEED - MIN_PROJECTILE_SPEED)) + MIN_PROJECTILE_SPEED;
        }
        else
        {
            return RAND.nextInt((MAX_PROJECTILE_SPEED - MIN_PROJECTILE_SPEED - ((int)size / SIZE_SPEED_MODIFIER)))
                    + MIN_PROJECTILE_SPEED;
        }
    }

    /**
     * Calculates the direction vector towards a random point within the center bounds of the screen.
     *
     * @param x the x-coordinate of the projectile
     * @param y the y-coordinate of the projectile
     * @return an array containing the normalized direction vector [directionX, directionY]
     */
    private double[] calculateDirection(final double x,
                                        final double y)
    {
        final double centerX;
        final double centerY;

        final double deltaX;
        final double deltaY;
        final double magnitude;

        centerX = RAND.nextInt(CENTER_BOX_MAX) + CENTER_BOX_MIN; // Random center X
        centerY = RAND.nextInt(CENTER_BOX_MAX) + CENTER_BOX_MIN; // Random center Y

        deltaX = centerX - x;
        deltaY= centerY - y;

        magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        validateMagnitude(magnitude);

        return new double[] { deltaX / magnitude, deltaY / magnitude };
    }

    /**
     * Updates the projectile's movement based on its speed and direction.
     */
    public void updateMovement()
    {
        setRotate(currRot);
        move(directionX * speed, directionY * speed);
        currRot += RAND.nextInt(speed) * rotDir;
    }

    /**
     * Checks if the projectile is off the screen.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     * @return true if the projectile is off the screen, false otherwise
     */
    public boolean isOffScreen(final int screenWidth,
                               final int screenHeight)
    {
        return getX() < MAP_EDGE || getX() > screenWidth ||
                getY() < MAP_EDGE || getY() > screenHeight;
    }

    /**
     * validates projectile size
     *
     * @param size of projectile
     */
    private void validateProjectile(final double size)
    {
        if (size <= MIN_SIZE)
        {
            throw new IllegalArgumentException("Size must be positive.");
        }
    }

    /**
     * validates the projectiles vector to not be 0
     *
     * @param magnitude of projectile vector
     */
    private void validateMagnitude(final double magnitude)
    {
        if (magnitude == MIN_MAG)
        {
            throw new IllegalStateException("Direction vector magnitude cannot be zero.");
        }
    }
}