package TermProject;

public class SpeedBoostPowerUp extends PowerUp {
    private static final double SPEED_BOOST_FACTOR = .3;

    public SpeedBoostPowerUp(final double x,
                             final double y)
    {
        super(x, y, PowerUpType.SPEED_BOOST);
    }

    @Override
    public void applyEffect(final Player player)
    {
        player.IncrementSpeedMod(SPEED_BOOST_FACTOR);
    }
}