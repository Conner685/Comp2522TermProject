package ca.bcit.termProject.vortexGame;

/**
 * Functional interface representing objects capable of movement within the game world.
 *
 * <p>This interface defines the fundamental contract for all movable game entities,
 * providing a standardized way to:
 * <ul>
 *   <li>Translate objects in 2D space</li>
 *   <li>Support polymorphic movement handling</li>
 *   <li>Enable movement-related lambda expressions</li>
 * </ul>
 *
 * <p>Implementation Requirements:
 * <table border="1">
 *   <tr><th>Characteristic</th><th>Requirement</th></tr>
 *   <tr><td>Coordinate System</td><td>Screen space (pixels)</td></tr>
 *   <tr><td>Parameters</td><td>Delta values (change amounts)</td></tr>
 *   <tr><td>Thread Safety</td><td>Implementations must be thread-safe</td></tr>
 *   <tr><td>Side Effects</td><td>Must update internal position state</td></tr>
 * </table>
 *
 * <p>Common Implementors:
 * <ul>
 *   <li>{@link GameObject} - Base class for all game entities</li>
 *   <li>{@link Player} - Player-controlled character</li>
 *   <li>{@link Projectile} - Enemy projectiles</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
@FunctionalInterface
public interface Movable
{
    /**
     * Translates the object's position by specified amounts.
     *
     * <p>Contract Requirements:
     * <ul>
     *   <li>Must apply delta values to current position</li>
     *   <li>Should handle any finite double values</li>
     *   <li>Must maintain object's valid state</li>
     *   <li>Should not perform bounds checking</li>
     * </ul>
     *
     * <p>Example Usage:
     * <pre>{@code
     * Movable player = new Player();
     * player.move(5.0, -2.5); // Move right 5px, up 2.5px
     * }</pre>
     *
     * @param deltaX Horizontal displacement (positive = right)
     * @param deltaY Vertical displacement (positive = down)
     */
    void move(final double deltaX,
              final double deltaY);
}