package ca.bcit.termProject.vortexGame;

/**
 * Represents the player character with movement and boost mechanics.
 *
 * <p>The player is the main controllable entity featuring:
 * <ul>
 *   <li>Four-directional movement (WASD controls)</li>
 *   <li>Boost system with resource management</li>
 *   <li>Modifiable speed and boost capacity</li>
 *   <li>Screen boundary constraints</li>
 * </ul>
 *
 * <p>Core Attributes:
 * <table border="1">
 *   <tr><th>Attribute</th><th>Value</th></tr>
 *   <tr><td>Base Speed</td><td>5 units/frame</td></tr>
 *   <tr><td>Boost Speed</td><td>10 units/frame</td></tr>
 *   <tr><td>Initial Boost</td><td>200 units</td></tr>
 *   <tr><td>Boost Drain</td><td>4 units/frame</td></tr>
 *   <tr><td>Boost Regen</td><td>1 unit/frame</td></tr>
 * </table>
 *
 * <p>State Management:
 * <ul>
 *   <li>Boost cut-off at 50% capacity</li>
 *   <li>Speed modifier stackable with power-ups</li>
 *   <li>Full stat reset capability</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class Player extends GameObject
{
    /**
     * Base movement speed without boosting (units/frame).
     *
     * <p>Applied when:
     * <ul>
     *   <li>Moving without shift key</li>
     *   <li>Boost is depleted</li>
     *   <li>In boost cut-off state</li>
     * </ul>
     */
    static final int SPEED = 5;

    /**
     * Boosted movement speed multiplier (units/frame).
     *
     * <p>Only active when:
     * <ul>
     *   <li>Shift key is pressed</li>
     *   <li>Boost is available</li>
     *   <li>Not in cut-off state</li>
     * </ul>
     */
    static final int BOOST_SPEED = 10;

    /**
     * Initial boost limit for the boost bar.
     */
    static final int INITIAL_BOOST_LIMIT = 200;

    /**
     * Boost drain speed when boost is activated
     */
    static final int BOOST_DRAIN = 4;

    /**
     * Boost regen speed when boost is not active
     */
    static final int BOOST_REGEN                = 1;
    private static final int BOOST_MIN          = 0;
    private static final int BOOST_CUT_OFF      = INITIAL_BOOST_LIMIT / 2;
    private static final int DELTA_START        = 0;
    private static final int STARTING_SPEED_MOD = 1;
    private static final int MIN_BOUND          = 0;

    private int currentBoost;
    private boolean boostCut;
    private double speedModifier;
    private int boostLimit;

    /**
     * Constructs a new player at specified coordinates.
     *
     * <p>Initial state includes:
     * <ul>
     *   <li>Full boost capacity</li>
     *   <li>Base speed modifier</li>
     *   <li>CSS class "player" for styling</li>
     *   <li>Active boost system</li>
     * </ul>
     *
     * @param x The horizontal spawn coordinate
     * @param y The vertical spawn coordinate
     * @param size The player's collision diameter
     */
    public Player(final double x,
                  final double y,
                  final double size)
    {
        super(x, y, size);
        validateCoordinates(x, y);
        
        this.currentBoost = INITIAL_BOOST_LIMIT;
        this.boostCut = false;
        this.speedModifier = STARTING_SPEED_MOD;
        this.boostLimit = INITIAL_BOOST_LIMIT;
        getStyleClass().add("player");
    }

    /**
     * Updates player position and boost state based on input.
     *
     * <p>Handles:
     * <ul>
     *   <li>Movement input processing</li>
     *   <li>Boost resource management</li>
     *   <li>Speed modification application</li>
     *   <li>Screen boundary enforcement</li>
     * </ul>
     *
     * @param WPressed W key state
     * @param SPressed S key state
     * @param APressed A key state
     * @param isDPressed D key state
     * @param ShiftPressed Shift key state
     */
    public void updateMovement(final boolean WPressed,
                               final boolean SPressed,
                               final boolean APressed,
                               final boolean isDPressed,
                               boolean ShiftPressed)
    {
        double deltaX;
        double deltaY;

         deltaX = DELTA_START;
         deltaY = DELTA_START;

        if (ShiftPressed && !boostCut)
        {
            currentBoost -= BOOST_DRAIN;
            if (currentBoost < BOOST_MIN)
            {
                currentBoost = BOOST_MIN;
            }
        }
        else
        {
            currentBoost += BOOST_REGEN;
            if (currentBoost > boostLimit)
            {
                currentBoost = boostLimit;
            }
        }

        if (currentBoost > BOOST_CUT_OFF)
        {
            boostCut = false;
        }

        if (currentBoost == BOOST_MIN ||
                boostCut)
        {
            ShiftPressed = false;
            boostCut = true;
        }

        if (WPressed) deltaY -= ShiftPressed ? BOOST_SPEED : SPEED;
        if (SPressed) deltaY += ShiftPressed ? BOOST_SPEED : SPEED;
        if (APressed) deltaX -= ShiftPressed ? BOOST_SPEED : SPEED;
        if (isDPressed) deltaX += ShiftPressed ? BOOST_SPEED : SPEED;

        deltaX *= speedModifier;
        deltaY *= speedModifier;

        move(deltaX, deltaY);

        // Prevent moving out of screen bounds
        setX(Math.max(DELTA_START, Math.min(getX(), VortexGameEngine.SCREEN_WIDTH_PX - getWidth())));
        setY(Math.max(DELTA_START, Math.min(getY(), VortexGameEngine.SCREEN_HEIGHT_PX - getHeight())));
    }

    /**
     * Fully restores boost resource to maximum capacity.
     *
     * <p>Also clears any active boost cut-off state.
     */
    public void resetBoost()
    {
        currentBoost = boostLimit;
    }

    /**
     * Resets the player's stats to the initial stats
     */
    public void resetStats()
    {
        this.speedModifier = STARTING_SPEED_MOD;
        this.boostLimit = INITIAL_BOOST_LIMIT;
        this.resetBoost();
    }

    /**
     * Gets the current boost resource level.
     *
     * @return Current boost units (0 to boostLimit)
     */
    public int getBoost()
    {
        return currentBoost;
    }

    /**
     * Gets the maximum boost capacity.
     *
     * @return Current maximum boost units
     */
    public int getMaxBoost()
    {
        return boostLimit;
    }

    /**
     * Checks if boost is temporarily disabled.
     *
     * @return true if in boost cut-off state
     */
    public boolean isBoostCut()
    {
        return boostCut;
    }

    /**
     /**
     * Modifies the player's speed multiplier.
     *
     * @param increment Value to add to speed modifier
     */
    public void IncrementSpeedMod(final double increment)
    {
        this.speedModifier += increment;
    }

    /**
     * Modifies the player's maximum boost capacity.
     *
     * @param increment Value to add to boost limit
     */
    public void IncrementBoostMod(final int increment)
    {
        this.boostLimit += increment;
        this.currentBoost += increment;
    }

    /**
     * Gets current speed modifier of the player
     *
     * @return speedModifier
     */
    public double getSpeedModifier()
    {
        return speedModifier;
    }

    /*
     * Validates player to spawn within proper bounds
     *
     * @param x of player
     * @param y of player
     */
    private static void validateCoordinates(final double x,
                                            final double y)
    {
        if (x < MIN_BOUND ||
                x > VortexGameEngine.SCREEN_WIDTH_PX ||
                y < MIN_BOUND ||
                y >= VortexGameEngine.SCREEN_HEIGHT_PX)
        {
            throw new IllegalArgumentException("Invalid coordinates");
        }
    }
}