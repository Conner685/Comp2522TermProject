package ca.bcit.termProject.vortexGame;

/**
 * Power that refreshes the players boost
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class RefreshBoostPowerUp extends PowerUp
{
    /**
     * Refresh boost Power up positioning details and styling
     * @param x position
     * @param y position
     */
    public RefreshBoostPowerUp(final double x,
                               final double y)
    {
        super(x, y, PowerUpType.REFRESH_BOOST);
        setId("RefreshBoost");
    }

    /**
     * Sets the players boost to max
     * @param player The player to affect
     */
    @Override
    public void applyEffect(final Player player)
    {
        player.resetBoost();
    }
}