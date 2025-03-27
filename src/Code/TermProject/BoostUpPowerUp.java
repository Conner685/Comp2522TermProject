package TermProject;

public class BoostUpPowerUp extends PowerUp
{
    private static final int BOOST_INCREASE = 25;

    public BoostUpPowerUp(final double x,
                          final double y)
    {
        super(x, y, PowerUpType.BOOST_UP);
    }

    @Override
    public void applyEffect(final Player player)
    {
        player.IncrementBoostMod(BOOST_INCREASE);
    }
}