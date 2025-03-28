package ca.bcit.termProject.vortexGame;

public class SpeedBoostPowerUp extends PowerUp {
    private static final double SPEED_BOOST_FACTOR = .3;

    /**
     * SpeedBoost Power up positioning details and styling
     * @param x position
     * @param y position
     */
    public SpeedBoostPowerUp(final double x,
                             final double y)
    {
        super(x, y, PowerUpType.SPEED_BOOST);
        setId("SpeedBoost");
    }

    /**
     * Applies permanent speed boost
     * @param player The player to affect
     */
    @Override
    public void applyEffect(final Player player)
    {
        player.IncrementSpeedMod(SPEED_BOOST_FACTOR);
    }
}