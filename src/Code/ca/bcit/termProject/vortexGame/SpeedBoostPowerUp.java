package ca.bcit.termProject.vortexGame;

/**
 * Power-up that permanently increases the player's movement speed.
 *
 * <p>This power-up provides a persistent 20% speed increase that
 * stacks with other speed modifications. Key characteristics:
 *
 * <table border="1">
 *   <tr><th>Attribute</th><th>Value</th></tr>
 *   <tr><td>Boost Amount</td><td>+20% base speed</td></tr>
 *   <tr><td>Duration</td><td>Permanent</td></tr>
 *   <tr><td>Stacking</td><td>Cumulative with other boosts</td></tr>
 *   <tr><td>Visual ID</td><td>"SpeedBoost" CSS identifier</td></tr>
 * </table>
 *
 * <p>Implementation Notes:
 * <ul>
 *   <li>Extends base PowerUp functionality</li>
 *   <li>Uses {@link PowerUpType#SPEED_BOOST} for identification</li>
 *   <li>Increments by {@link #SPEED_BOOST_FACTOR}</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class SpeedBoostPowerUp extends PowerUp
{
    /**
     * Multiplier applied to player's base speed upon collection.
     *
     * <p>Value of 0.2 represents a 20% speed increase that:
     * <ul>
     *   <li>Is applied additively with other speed modifiers</li>
     *   <li>Affects both normal and boost movement speeds</li>
     *   <li>Persists for the remainder of the game session</li>
     * </ul>
     */
    static final double SPEED_BOOST_FACTOR = .2;

    /**
     * Creates a new speed boost power-up at specified coordinates.
     *
     * <p>The power-up will:
     * <ul>
     *   <li>Appear at the given (x,y) position</li>
     *   <li>Use standard power-up sizing</li>
     *   <li>Have "SpeedBoost" CSS identifier for styling</li>
     *   <li>Be of type {@link PowerUpType#SPEED_BOOST}</li>
     * </ul>
     *
     * @param x The horizontal spawn coordinate (in pixels)
     * @param y The vertical spawn coordinate (in pixels)
     */
    public SpeedBoostPowerUp(final double x,
                             final double y)
    {
        super(x, y, PowerUpType.SPEED_BOOST);
        setId("SpeedBoost");
    }

    /**
     * Applies permanent speed increase to the player.
     *
     * <p>Effects:
     * <ul>
     *   <li>Increases player's speed modifier by {@link #SPEED_BOOST_FACTOR}</li>
     *   <li>Modification is immediately active</li>
     *   <li>Change persists through entire game session</li>
     * </ul>
     *
     * @param player The player entity to modify
     */
    @Override
    public void applyEffect(final Player player)
    {
        player.IncrementSpeedMod(SPEED_BOOST_FACTOR);
    }
}