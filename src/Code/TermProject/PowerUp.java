package TermProject;

import javafx.scene.paint.Color;

public abstract class PowerUp extends GameObject
{
    private static final int POWER_UP_SIZE = 20;
    private static final int POWER_UP_ANGLE = 45;

    public enum PowerUpType
    {
        SPEED_BOOST,
        BOOST_UP,
        REFRESH_BOOST
    }

    public PowerUp(final double x,
                   final double y,
                   final PowerUpType type)
    {
        super(x, y, POWER_UP_SIZE);
        setRotate(POWER_UP_ANGLE);
        setFill(getColorForType(type));
    }

    private Color getColorForType(final PowerUpType type)
    {
        return switch (type)
        {
            case SPEED_BOOST -> Color.GREEN;
            case BOOST_UP -> Color.PURPLE;
            case REFRESH_BOOST -> Color.BLUE;
        };
    }

    /**
     * Applies the power-up effect to the player
     * @param player The player to affect
     */
    public abstract void applyEffect(final Player player);
}