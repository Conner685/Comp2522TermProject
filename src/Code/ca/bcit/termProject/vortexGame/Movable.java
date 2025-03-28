package ca.bcit.termProject.vortexGame;

/**
 * Represents an object that can move in the game.
 */
public interface Movable
{
    /**
     * Moves the object by the specified delta values.
     *
     * @param deltaX the change in the x-coordinate
     * @param deltaY the change in the y-coordinate
     */
    void move(final double deltaX,
              final double deltaY);
}