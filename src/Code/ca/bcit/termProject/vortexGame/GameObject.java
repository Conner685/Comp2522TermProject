package ca.bcit.termProject.vortexGame;

import javafx.scene.shape.Rectangle;

/**
 * Abstract base class representing all movable game entities with rectangular bounds.
 *
 * <p>This class serves as the foundation for all game objects by providing:
 * <ul>
 *   <li>Standardized position and size properties</li>
 *   <li>Basic movement implementation</li>
 *   <li>Size validation</li>
 *   <li>Common collision detection base</li>
 * </ul>
 *
 * <p>Key Characteristics:
 * <table border="1">
 *   <tr><th>Attribute</th><th>Description</th></tr>
 *   <tr><td>Shape</td><td>Square (equal width/height)</td></tr>
 *   <tr><td>Movement</td><td>Delta-based translation</td></tr>
 *   <tr><td>Size</td><td>Positive values enforced</td></tr>
 *   <tr><td>Position</td><td>2D coordinate system</td></tr>
 * </table>
 *
 * <p>Implementation Notes:
 * <ul>
 *   <li>Extends JavaFX Rectangle for rendering</li>
 *   <li>Implements Movable interface for movement</li>
 *   <li>Uses square dimensions for simplified collision</li>
 *   <li>Enforces minimum size constraints</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public abstract class GameObject extends Rectangle
        implements Movable
{
    private static final int MIN_SIZE = 0;

    /**
     * Constructs a new game object with specified position and size.
     *
     * <p>The object will:
     * <ul>
     *   <li>Be positioned at (x,y) coordinates</li>
     *   <li>Have equal width/height (square)</li>
     *   <li>Validate size is positive</li>
     * </ul>
     *
     * @param x The horizontal position (pixels)
     * @param y The vertical position (pixels)
     * @param size The width/height of the object (pixels)
     */
    public GameObject(final double x,
                      final double y,
                      final double size)
    {
        super(x, y, size, size);
        validateSize(size);
    }

    /**
     * Moves the object by specified delta values.
     *
     * <p>Movement is:
     * <ul>
     *   <li>Additive to current position</li>
     *   <li>Applied to both axes simultaneously</li>
     *   <li>Not bounds-checked</li>
     * </ul>
     *
     * @param deltaX Horizontal displacement (pixels)
     * @param deltaY Vertical displacement (pixels)
     */
    @Override
    public final void move(final double deltaX,
                     final double deltaY)
    {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    /*
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