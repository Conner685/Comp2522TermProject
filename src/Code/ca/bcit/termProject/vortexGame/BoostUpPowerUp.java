package ca.bcit.termProject.vortexGame;

/**
 * Power-up that permanently increases the player's maximum boost capacity.
 *
 * <p>This power-up provides a persistent 25% increase to the player's boost meter capacity.
 * Key characteristics:
 *
 * <table border="1">
 *   <tr><th>Attribute</th><th>Value</th></tr>
 *   <tr><td>Boost Increase</td><td>25 units (permanent)</td></tr>
 *   <tr><td>Visual ID</td><td>"BoostUp" CSS identifier</td></tr>
 *   <tr><td>Type</td><td>{@link PowerUpType#BOOST_UP}</td></tr>
 *   <tr><td>Effect</td><td>Increases both current and max boost</td></tr>
 * </table>
 *
 * <p>Implementation Details:
 * <ul>
 *   <li>Modifies both current and maximum boost values</li>
 *   <li>Effect stacks with multiple collections</li>
 *   <li>Uses distinct visual styling (purple color)</li>
 *   <li>Immediately applies effect on collection</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class BoostUpPowerUp extends PowerUp
{
    /**
     * Fixed amount to increase boost capacity by (in units).
     *
     * <p>This value:
     * <ul>
     *   <li>Represents a 25% increase to base capacity</li>
     *   <li>Is applied to both current and max boost</li>
     *   <li>Stacks with previous collections</li>
     * </ul>
     */
    static final int BOOST_INCREASE = 25;

    /**
     * Constructs a new boost capacity power-up at specified coordinates.
     *
     * <p>The power-up will:
     * <ul>
     *   <li>Appear at given (x,y) position</li>
     *   <li>Use standard power-up size</li>
     *   <li>Have "BoostUp" CSS identifier</li>
     *   <li>Be of type {@link PowerUpType#BOOST_UP}</li>
     * </ul>
     *
     * @param x The horizontal spawn coordinate (pixels)
     * @param y The vertical spawn coordinate (pixels)
     */
    public BoostUpPowerUp(final double x,
                          final double y)
    {
        super(x, y, PowerUpType.BOOST_UP);
        setId("BoostUp");
    }

    /**
     * Permanently increases the player's boost capacity.
     *
     * <p>Effects:
     * <ul>
     *   <li>Increases max boost by {@value #BOOST_INCREASE} units</li>
     *   <li>Adds equivalent amount to current boost</li>
     *   <li>Updates visual boost meter immediately</li>
     * </ul>
     *
     * @param player The player entity to modify
     */
    @Override
    public void applyEffect(final Player player)
    {
        player.IncrementBoostMod(BOOST_INCREASE);
    }
}