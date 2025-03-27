package TermProject;

public class RefreshBoostPowerUp extends PowerUp
{
    public RefreshBoostPowerUp(final double x,
                               final double y)
    {
        super(x, y, PowerUpType.REFRESH_BOOST);
    }

    @Override
    public void applyEffect(final Player player)
    {
        player.resetBoost();
    }
}