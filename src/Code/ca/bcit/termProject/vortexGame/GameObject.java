package ca.bcit.termProject.vortexGame;

import javafx.scene.shape.Rectangle;

/**
 * Simplified version of rectangle to allow for single implementation of Size as well as provide supporting methods
 * for game objects.
 */
public abstract class GameObject extends Rectangle implements Movable
{
    public GameObject(final double x,
                      final double y,
                      final double size)
    {
        super(x, y, size, size);
    }

    /**
     * Moves the projectile by the specified delta values.
     *
     * @param deltaX the change in the x-coordinate
     * @param deltaY the change in the y-coordinate
     */
    public void move(final double deltaX,
                     final double deltaY)
    {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }
}