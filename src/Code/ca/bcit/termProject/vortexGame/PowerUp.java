package ca.bcit.termProject.vortexGame;

import java.util.Random;

import static ca.bcit.termProject.vortexGame.VortexGameEngine.SCREEN_HEIGHT_PX;
import static ca.bcit.termProject.vortexGame.VortexGameEngine.SCREEN_WIDTH_PX;

/**
 * Abstract base class defining common behavior and properties for all power-ups.
 *
 * <p>This class serves as the foundation for all power-up types, providing:
 * <ul>
 *   <li>Standardized size and visual appearance</li>
 *   <li>Common spawning mechanics</li>
 *   <li>Abstract effect application contract</li>
 *   <li>Type enumeration for categorization</li>
 * </ul>
 *
 * <p>Core Characteristics:
 * <table border="1">
 *   <tr><th>Attribute</th><th>Value</th></tr>
 *   <tr><td>Size</td><td>20px diameter</td></tr>
 *   <tr><td>Rotation</td><td>45° fixed angle</td></tr>
 *   <tr><td>CSS Class</td><td>"PowerUp" base styling</td></tr>
 *   <tr><td>Spawning</td><td>Random position and type</td></tr>
 * </table>
 *
 * <p>Implementation Requirements:
 * <ul>
 *   <li>Concrete subclasses must implement {@link #applyEffect(Player)}</li>
 *   <li>All power-ups share common visual properties</li>
 *   <li>New power-up types require enum addition</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public abstract class PowerUp extends GameObject
{
    static final int POWER_UP_SIZE                    = 20;
    private static final int POWER_UP_ANGLE           = 45;
    private static final PowerUpType[] POWER_UP_TYPES = PowerUpType.values();


    /**
     * Enumeration of available power-up types.
     *
     * <p>Used for:
     * <ul>
     *   <li>Random type selection during spawning</li>
     *   <li>Type identification in game systems</li>
     *   <li>New power-up variant registration</li>
     * </ul>
     */
    enum PowerUpType
    {
        SPEED_BOOST,
        BOOST_UP,
        REFRESH_BOOST
    }

    /**
     * Constructs a new power-up with base properties.
     *
     * <p>All power-ups will:
     * <ul>
     *   <li>Use standard 20px size</li>
     *   <li>Display at 45° rotation</li>
     *   <li>Have "PowerUp" CSS class for styling</li>
     *   <li>Implement type-specific behavior</li>
     * </ul>
     *
     * @param x The horizontal spawn coordinate
     * @param y The vertical spawn coordinate
     * @param type The power-up variant being created
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
     * Applies the power-up's unique effect to the player.
     *
     * <p>Contract Requirements:
     * <ul>
     *   <li>Must modify player state meaningfully</li>
     *   <li>Should provide visual/audio feedback</li>
     *   <li>Must handle null player parameter</li>
     * </ul>
     *
     * @param player The player entity to affect
     */
    public abstract void applyEffect(final Player player);

    /**
     * Generates a random power-up at valid game coordinates.
     *
     * <p>Spawn Mechanics:
     * <ul>
     *   <li>Position constrained to visible play area</li>
     *   <li>Type selected randomly from all variants</li>
     *   <li>Immediately added to game scene</li>
     * </ul>
     *
     * @param gameEngine The active game instance
     */
    public static void spawnPowerUp(final VortexGameEngine gameEngine)
    {
        final double x;
        final double y;
        final Random rand;
        final PowerUp powerUp;
        final PowerUp.PowerUpType type;

        rand = new Random();

        x = rand.nextInt(SCREEN_WIDTH_PX - POWER_UP_SIZE);
        y = rand.nextInt(SCREEN_HEIGHT_PX - POWER_UP_SIZE);

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