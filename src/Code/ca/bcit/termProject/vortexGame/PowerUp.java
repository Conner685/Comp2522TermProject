package ca.bcit.termProject.vortexGame;

import java.util.Random;

import static ca.bcit.termProject.vortexGame.VortexGameEngine.SCREEN_HEIGHT;
import static ca.bcit.termProject.vortexGame.VortexGameEngine.SCREEN_WIDTH;

/**
 * Functions as a basic template for all power ups, providing an automated size, style affects, and details universal
 * to power ups.
 */
public abstract class PowerUp extends GameObject
{
    private static final int POWER_UP_SIZE = 20;
    private static final int POWER_UP_ANGLE = 45;
    private static final PowerUpType[] POWER_UP_TYPES = PowerUpType.values();


    /**
     * Each power up type used for random selection of children
     */
    protected enum PowerUpType
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

    /**
     * Spawns a random power-up at a random position.
     */
    public static void spawnPowerUp(final VortexGameEngine gameEngine)
    {
        final double x;
        final double y;
        final Random rand;
        final PowerUp powerUp;
        final PowerUp.PowerUpType type;

        rand = new Random();

        x = rand.nextInt(SCREEN_WIDTH - POWER_UP_SIZE);
        y = rand.nextInt(SCREEN_HEIGHT - POWER_UP_SIZE);

        type = POWER_UP_TYPES[rand.nextInt(POWER_UP_TYPES.length)];

        powerUp = switch (type)
        {
            case SPEED_BOOST -> new SpeedBoostPowerUp(x, y);
            case BOOST_UP -> new BoostUpPowerUp(x, y);
            case REFRESH_BOOST -> new RefreshBoostPowerUp(x, y);
        };

        gameEngine.getRoot().getChildren().add(powerUp);
    }
}