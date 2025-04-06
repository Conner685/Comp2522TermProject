package ca.bcit.termProject.vortexGame;

import javafx.scene.shape.Rectangle;

/**
 * Simplified version of rectangle to allow for single implementation of Size as well as provide supporting methods
 * for game objects.
 *
 * @author Conner Ponton
 * @version 1.0
 */
public abstract class GameObject extends Rectangle
        implements Movable
{
    private static final int MIN_SIZE = 0;

    public GameObject(final double x,
                      final double y,
                      final double size)
    {
        super(x, y, size, size);
        validateSize(size);
    }

    /**
     * Moves the projectile by the specified delta values.
     *
     * @param deltaX the change in the x-coordinate
     * @param deltaY the change in the y-coordinate
     */
    @Override
    public final void move(final double deltaX,
                     final double deltaY)
    {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    /**
     * Validates objects do not spawn with a negative size.
     *
     * @param size of object
     */
    private static void validateSize(final double size)
    {
        if (size <= MIN_SIZE)
        {
            throw new IllegalArgumentException("Cannot have negative size");
        }
    }

}