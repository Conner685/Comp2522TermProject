package ca.bcit.termProject.vortexGame;

/**
 * Power-up that immediately restores the player's boost meter to maximum capacity.
 *
 * <p>This power-up provides instant recovery of the player's boost resource, allowing
 * for immediate use of boost abilities. Key characteristics:
 *
 * <table border="1">
 *   <tr><th>Attribute</th><th>Value</th></tr>
 *   <tr><td>Effect</td><td>Full boost restoration</td></tr>
 *   <tr><td>Duration</td><td>Instant application</td></tr>
 *   <tr><td>Stacking</td><td>Always sets to max (does not add)</td></tr>
 *   <tr><td>Visual ID</td><td>"RefreshBoost" CSS identifier</td></tr>
 * </table>
 *
 * <p>Implementation Notes:
 * <ul>
 *   <li>Extends base PowerUp functionality</li>
 *   <li>Uses {@link PowerUpType#REFRESH_BOOST} for identification</li>
 *   <li>Calls {@link Player#resetBoost()} for effect implementation</li>
 *   <li>Appears as distinct visual element with custom styling</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class RefreshBoostPowerUp extends PowerUp
{
    /**
     * Creates a new boost refresh power-up at specified coordinates.
     *
     * <p>The power-up will:
     * <ul>
     *   <li>Appear at the given (x,y) position</li>
     *   <li>Use standard power-up sizing</li>
     *   <li>Have "RefreshBoost" CSS identifier for styling</li>
     *   <li>Be of type {@link PowerUpType#REFRESH_BOOST}</li>
     * </ul>
     *
     * @param x The horizontal spawn coordinate (in pixels)
     * @param y The vertical spawn coordinate (in pixels)
     */
    public RefreshBoostPowerUp(final double x,
                               final double y)
    {
        super(x, y, PowerUpType.REFRESH_BOOST);
        setId("RefreshBoost");
    }

    /**
     * Immediately restores player's boost to maximum capacity.
     *
     * <p>Effects:
     * <ul>
     *   <li>Sets current boost equal to maximum boost</li>
     *   <li>Effect is applied instantly upon collection</li>
     *   <li>Overrides any existing boost cut state</li>
     *   <li>Does not modify maximum boost capacity</li>
     * </ul>
     *
     * @param player The player entity to affect
     */
    @Override
    public void applyEffect(final Player player)
    {
        player.resetBoost();
    }
}