package ca.bcit.termProject.vortexGame;

/**
 * Power that increases the players boost limit
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class BoostUpPowerUp extends PowerUp
{
    static final int BOOST_INCREASE = 25;
    public static final double BOOST_BAR_SIZE_INCREASE = BOOST_INCREASE / 100.0;

    /**
     * Boost limit Power up positioning details and styling
     * @param x position
     * @param y position
     */
    public BoostUpPowerUp(final double x,
                          final double y)
    {
        super(x, y, PowerUpType.BOOST_UP);
        setId("BoostUp");
    }

    /**
     * Increases the players max boost
     * @param player The player to affect
     */
    @Override
    public void applyEffect(final Player player)
    {
        player.IncrementBoostMod(BOOST_INCREASE);
    }
}