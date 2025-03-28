package ca.bcit.termProject.vortexGame;

public class BoostUpPowerUp extends PowerUp
{
    private static final int BOOST_INCREASE = 25;

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
    public void applyEffect(final Player player) //TODO provide visual indicator of increased limit(?) Harass AJ later
    {
        player.IncrementBoostMod(BOOST_INCREASE);
    }
}