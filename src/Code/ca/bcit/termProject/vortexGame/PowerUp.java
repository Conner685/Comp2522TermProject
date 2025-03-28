package ca.bcit.termProject.vortexGame;

/**
 * Functions as a basic template for all power ups, providing an automated size, style affects, and details universal
 * to power ups.
 */
public abstract class PowerUp extends GameObject
{
    private static final int POWER_UP_SIZE = 20;
    private static final int POWER_UP_ANGLE = 45;

    /**
     * Each power up type used for random selection of children
     */
    public enum PowerUpType
    {
        SPEED_BOOST,
        BOOST_UP,
        REFRESH_BOOST
    }

    /**
     * Base power up template
     * @param x position
     * @param y position
     * @param type specific power up
     */
    public PowerUp(final double x,
                   final double y,
                   final PowerUpType type)
    {
        super(x, y, POWER_UP_SIZE);
        setRotate(POWER_UP_ANGLE);
        getStyleClass().add("PowerUp");
    }

    /**
     * Applies the power-up effect to the player
     * @param player The player to affect
     */
    public abstract void applyEffect(final Player player);
}