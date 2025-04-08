package ca.bcit.termProject.vortexGame;

import java.util.Random;

/**
 * Represents enemy projectiles that pose obstacles to the player in the game world.
 *
 * <p>Each projectile has randomized characteristics including:
 * <ul>
 *   <li>Size (within defined bounds)</li>
 *   <li>Movement speed (inversely related to size)</li>
 *   <li>Direction (toward random center point)</li>
 *   <li>Rotation behavior</li>
 * </ul>
 *
 * <p>Key Characteristics:
 * <table border="1">
 *   <tr><th>Attribute</th><th>Range</th></tr>
 *   <tr><td>Size</td><td>10-60 pixels</td></tr>
 *   <tr><td>Speed</td><td>5-20 units/frame</td></tr>
 *   <tr><td>Rotation</td><td>Random angular velocity</td></tr>
 *   <tr><td>Lifetime</td><td>Until exiting screen bounds</td></tr>
 * </table>
 *
 * <p>Movement Physics:
 * <ul>
 *   <li>Speed inversely proportional to size (larger = slower)</li>
 *   <li>Direction vector points toward random screen center point</li>
 *   <li>Rotation direction randomly clockwise/counter-clockwise</li>
 *   <li>Movement unaffected by game difficulty</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class Projectile extends GameObject
{
    /**
     * Minimal Projectile Size
     */
    static final int MIN_PROJECTILE_SIZE          = 10;
    /**
     * Maximum Projectile Size
     */
    static final int MAX_PROJECTILE_SIZE          = 60;
    private static final int MIN_PROJECTILE_SPEED = 5;
    private static final int MAX_PROJECTILE_SPEED = 20;
    private static final int SIZE_SPEED_MODIFIER  = 5;
    private static final int CENTER_BOX_MAX       = 550;
    private static final int CENTER_BOX_MIN       = 200;
    private static final int MAP_EDGE             = -100;
    private static final int MIN_SIZE_AFFECT      = 10;
    private static final Random RAND            = new Random();
    private static final int DIR_X              = 0;
    private static final int DIR_Y              = 1;
    private static final int MAX_INITIAL_ANGLE  = 90;
    private static final int RIGHT      = 3;
    private static final int LEFT       = 1;
    private static final int MIN_MAG    = 0;

    private final double directionX;
    private final double directionY;
    private final int speed;
    private double currRot;
    private final int rotDir;

    /**
     * Constructs a new projectile with randomized movement characteristics.
     *
     * <p>The projectile will:
     * <ul>
     *   <li>Appear at specified coordinates</li>
     *   <li>Have CSS class "projectile" for styling</li>
     *   <li>Move toward random center-screen point</li>
     *   <li>Rotate with random angular velocity</li>
     * </ul>
     *
     * @param x The horizontal spawn coordinate (in pixels)
     * @param y The vertical spawn coordinate (in pixels)
     * @param size The diameter of the projectile (10-60 pixels)
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
        this.speed      = calculateSpeed(size);
        this.directionX = direction[DIR_X];
        this.directionY = direction[DIR_Y];
        this.currRot    = RAND.nextDouble(MAX_INITIAL_ANGLE);
        this.rotDir     = RAND.nextInt(RIGHT) - LEFT;
    }

    /**
     * Determines if projectile has exited playable area.
     *
     * <p>Uses expanded bounds (100px beyond screen edges) before culling
     * to prevent visible popping at screen edges.
     *
     * @param screenWidth The current screen width in pixels
     * @param screenHeight The current screen height in pixels
     * @return true if projectile is fully outside culling bounds
     */
    public boolean isOffScreen(final int screenWidth,
                               final int screenHeight)
    {
        return getX() < MAP_EDGE || getX() > screenWidth ||
                getY() < MAP_EDGE || getY() > screenHeight;
    }

    /**
     * Updates projectile position and rotation each frame.
     *
     * <p>Applies the following movement logic:
     * <ul>
     *   <li>Linear movement along pre-calculated direction vector</li>
     *   <li>Continuous rotation with random angular velocity</li>
     *   <li>Speed determined by initial size calculation</li>
     * </ul>
     */
    public void updateMovement()
    {
        setRotate(currRot);
        move(directionX * speed, directionY * speed);
        currRot += RAND.nextInt(speed) * rotDir;
    }

    /*
     * Calculates movement speed based on projectile size.
     *
     * <p>Implements inverse relationship where:
     * <ul>
     *   <li>Small projectiles (≤10px) get full random speed range</li>
     *   <li>Larger projectiles have speed reduced by size/5</li>
     * </ul>
     *
     * @param size The diameter of the projectile
     * @return Calculated speed in range 5-20 units/frame
     */
    private int calculateSpeed(final double size)
    {
        if (size < MIN_SIZE_AFFECT)
        {
            return RAND.nextInt((MAX_PROJECTILE_SPEED - MIN_PROJECTILE_SPEED))
                    + MIN_PROJECTILE_SPEED;
        }
        else
        {
            return RAND.nextInt((MAX_PROJECTILE_SPEED - MIN_PROJECTILE_SPEED
                    - ((int)size / SIZE_SPEED_MODIFIER)))
                    + MIN_PROJECTILE_SPEED;
        }
    }

    /*
     * Generates normalized direction vector toward random screen center point.
     *
     * <p>Target points are constrained to:
     * <ul>
     *   <li>X: 200-750px (avoid edges)</li>
     *   <li>Y: 200-750px (avoid edges)</li>
     * </ul>
     *
     * @param x The projectile's current x-coordinate
     * @param y The projectile's current y-coordinate
     * @return Normalized direction vector [x,y]
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

    /*
     * validates projectile size
     *
     * @param size of projectile
     */
    private void validateProjectile(final double size)
    {
        if (size < MIN_PROJECTILE_SIZE ||
            size > MAX_PROJECTILE_SIZE)
        {
            throw new IllegalArgumentException("Size must be positive.");
        }
    }

    /*
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