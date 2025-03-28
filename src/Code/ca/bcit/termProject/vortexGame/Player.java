package ca.bcit.termProject.vortexGame;

/**
 * Represents a player in the game that can move and use a boost mechanic.
 */
public class Player extends GameObject
{
    private static final int SPEED = 5;
    private static final int BOOST_SPEED = 10;
    private static final int INITIAL_BOOST_LIMIT = 200;
    private static final int BOOST_DRAIN = 4;
    private static final int BOOST_REGEN = 1;
    private static final int BOOST_MIN = 0;
    private static final int BOOST_CUT_OFF = INITIAL_BOOST_LIMIT / 2;
    private static final int DELTA_START = 0;
    private static final int STARTING_SPEED_MOD = 1;

    private int currentBoost;
    private boolean boostCut;
    private double speedModifier;
    private int boostLimit;

    /**
     * Constructs a Player with the specified position and size.
     *
     * @param x    the x-coordinate of the player
     * @param y    the y-coordinate of the player
     * @param size the size of the player
     */
    public Player(final double x,
                  final double y,
                  final double size)
    {
        super(x, y, size);
        this.currentBoost = INITIAL_BOOST_LIMIT;
        this.boostCut = false;
        this.speedModifier = STARTING_SPEED_MOD;
        this.boostLimit = INITIAL_BOOST_LIMIT;
        getStyleClass().add("player");
    }

    /**
     * Updates the player's movement based on input and boost state.
     *
     * @param isWPressed     whether the W key is pressed
     * @param isSPressed     whether the S key is pressed
     * @param isAPressed     whether the A key is pressed
     * @param isDPressed     whether the D key is pressed
     * @param isShiftPressed whether the Shift key is pressed
     */
    public void updateMovement(final boolean isWPressed,
                               final boolean isSPressed,
                               final boolean isAPressed,
                               final boolean isDPressed,
                               boolean isShiftPressed)
    {
        double deltaX;
        double deltaY;

         deltaX = DELTA_START;
         deltaY = DELTA_START;

        if (isShiftPressed && !boostCut)
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
            isShiftPressed = false;
            boostCut = true;
        }

//        System.out.println(currentBoost);

        if (isWPressed) deltaY -= isShiftPressed ? BOOST_SPEED : SPEED;
        if (isSPressed) deltaY += isShiftPressed ? BOOST_SPEED : SPEED;
        if (isAPressed) deltaX -= isShiftPressed ? BOOST_SPEED : SPEED;
        if (isDPressed) deltaX += isShiftPressed ? BOOST_SPEED : SPEED;

        deltaX *= speedModifier;
        deltaY *= speedModifier;

        move(deltaX, deltaY);

        // Prevent moving out of screen bounds
        setX(Math.max(DELTA_START, Math.min(getX(), VortexGameEngine.SCREEN_WIDTH - getWidth())));
        setY(Math.max(DELTA_START, Math.min(getY(), VortexGameEngine.SCREEN_HEIGHT - getHeight())));
    }

    /**
     * Resets the player's boost level to the maximum limit.
     */
    public void resetBoost()
    {
        currentBoost = boostLimit;
    }

    /**
     * Resets the player's stats to the initial
     */
    public void resetStats()
    {
        this.speedModifier = STARTING_SPEED_MOD;
        this.boostLimit = INITIAL_BOOST_LIMIT;
        this.resetBoost();
    }

    /**
     * Gets the current user boost
     *
     * @return current boost
     */
    public int getBoost()
    {
        return currentBoost;
    }

    /**
     * Gets the max user boost
     *
     * @return max boost
     */
    public int getMaxBoost()
    {
        return INITIAL_BOOST_LIMIT;
    }

    /**
     * Returns if the user can use boost
     *
     * @return boostCut
     */
    public boolean isBoostCut()
    {
        return boostCut;
    }

    /**
     * Increments the speed modifier
     * @param increment amount to modify speed by
     */
    public void IncrementSpeedMod(final double increment)
    {
        this.speedModifier += increment;
        System.out.println("Current Speed Mod" + speedModifier);
    }

    /**
     * Increments the boost limit
     * @param increment amount to increase boost limit by
     */
    public void IncrementBoostMod(final int increment)
    {
        this.boostLimit += increment;
        System.out.println("Current Boost limit" + boostLimit);
    }
}