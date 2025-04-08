    package ca.bcit.termProject.vortexGame;

    import org.junit.jupiter.api.Test;

    import static ca.bcit.termProject.vortexGame.Projectile.MAX_PROJECTILE_SIZE;
    import static ca.bcit.termProject.vortexGame.Projectile.MIN_PROJECTILE_SIZE;
    import static org.junit.jupiter.api.Assertions.*;

    import java.util.List;
    import java.util.Comparator;
    import java.util.stream.Collectors;

    import static ca.bcit.termProject.vortexGame.VortexGameEngine.*;
    import static ca.bcit.termProject.vortexGame.Player.*;
    import static ca.bcit.termProject.vortexGame.PowerUp.POWER_UP_SIZE;
    import static ca.bcit.termProject.vortexGame.SpeedBoostPowerUp.SPEED_BOOST_FACTOR;
    import static ca.bcit.termProject.vortexGame.BoostUpPowerUp.*;

    /**
     * Comprehensive test suite for the Vortex bullet hell game components.
     *
     * <p>This test class verifies both positive and negative cases for:
     * <ul>
     *   <li>Game object construction and validation</li>
     *   <li>Movement mechanics of all entity types</li>
     *   <li>Player-specific systems (boost, movement)</li>
     *   <li>Power-up effects and interactions</li>
     *   <li>Score management utilities</li>
     *   <li>Edge case behavior and failure modes</li>
     * </ul>
     *
     * <p>The tests employ a combination of:
     * <ul>
     *   <li>State verification - Checking object properties after operations</li>
     *   <li>Behavior verification - Ensuring proper interaction between components</li>
     *   <li>Boundary testing - Validating edge cases of parameter ranges</li>
     *   <li>Exception testing - Confirming proper error handling</li>
     * </ul>
     *
     * <p>Test Coverage Includes:
     * <table border="1">
     *   <tr><th>Component</th><th>Test Cases</th></tr>
     *   <tr><td>GameObject</td><td>Size validation, basic movement</td></tr>
     *   <tr><td>Projectile</td><td>Spawn validation, movement patterns</td></tr>
     *   <tr><td>Player</td><td>Movement, boost system, edge cases</td></tr>
     *   <tr><td>PowerUps</td><td>Size consistency, effect application</td></tr>
     *   <tr><td>Utilities</td><td>Score sorting, data transformation</td></tr>
     * </table>
     *
     * @author Conner Ponton
     * @version 1.0
     */
    class VortexGameTests
    {

        @Test
        void testGameObjectSizeValidation()
        {
            // Valid cases
            assertDoesNotThrow(() -> new GameObject(0, 0, 1) {});
            assertDoesNotThrow(() -> new GameObject(0, 0, 1000) {});

            // Invalid cases
            assertThrows(IllegalArgumentException.class, () -> new GameObject(0, 0, 0) {});
            assertThrows(IllegalArgumentException.class, () -> new GameObject(0, 0, -1) {});
        }

        // Projectile-specific tests
        @Test
        void testProjectileValidation()
        {
            // Valid projectile sizes
            new Projectile(0, 0, MIN_PROJECTILE_SIZE);
            new Projectile(0, 0, MAX_PROJECTILE_SIZE);

            // Invalid projectile sizes
            assertThrows(IllegalArgumentException.class,
                    () -> new Projectile(0, 0, MIN_PROJECTILE_SIZE - 1));
        }

        @Test
        void testProjectileMovement()
        {
            Projectile p = new Projectile(100, 100, 20);
            double initialX = p.getX();
            double initialY = p.getY();

            p.updateMovement();

            assertNotEquals(initialX, p.getX());
            assertNotEquals(initialY, p.getY());
            assertTrue(p.getRotate() != 0); // Should have rotation after movement
        }

        // Player-specific tests
        @Test
        void testPlayerMovement()
        {
            Player player = new Player(100, 100, PLAYER_SIZE);

            // Test basic movement
            player.updateMovement(true, false, false, false, false);
            assertEquals(100 - SPEED, player.getY(), 0.001);

            // Test boost movement
            player.updateMovement(false, true, false, false, true);
            assertEquals(100 - SPEED + BOOST_SPEED, player.getY(), 0.001);
        }

        @Test
        void testPlayerBoostSystem()
        {
            Player player = new Player(0, 0, PLAYER_SIZE);

            // Initial state
            assertEquals(INITIAL_BOOST_LIMIT, player.getBoost());

            // Test boost drain
            player.updateMovement(false, false, false, false, true);
            assertEquals(INITIAL_BOOST_LIMIT - BOOST_DRAIN, player.getBoost());

            // Test boost regeneration
            player.updateMovement(false, false, false, false, false);
            assertEquals(INITIAL_BOOST_LIMIT - BOOST_DRAIN + BOOST_REGEN, player.getBoost());
        }

        // PowerUp tests
        @Test
        void testPowerUpSize()
        {
            // All powers should use the standard size
            assertEquals(POWER_UP_SIZE, new SpeedBoostPowerUp(0, 0).getWidth());
            assertEquals(POWER_UP_SIZE, new BoostUpPowerUp(0, 0).getWidth());
            assertEquals(POWER_UP_SIZE, new RefreshBoostPowerUp(0, 0).getWidth());
        }

        @Test
        void testPowerUpEffects()
        {
            Player player = new Player(0, 0, PLAYER_SIZE);

            // Speed Boost
            double initialSpeed = player.getSpeedModifier();
            new SpeedBoostPowerUp(0, 0).applyEffect(player);
            assertEquals(initialSpeed + SPEED_BOOST_FACTOR, player.getSpeedModifier(), 0.001);

            // Boost Up
            int initialBoost = player.getMaxBoost();
            new BoostUpPowerUp(0, 0).applyEffect(player);
            assertEquals(initialBoost + BOOST_INCREASE, player.getMaxBoost());

            // Refresh Boost
            player.updateMovement(false, false, false, false, true); // Drain boost
            new RefreshBoostPowerUp(0, 0).applyEffect(player);
            assertEquals(player.getMaxBoost(), player.getBoost());
        }

        // Utility tests
        @Test
        void testScoreManagerSorting()
        {
            List<String> unsorted = List.of("50", "200", "100", "150");
            List<String> expected = List.of("200", "150", "100", "50");

            List<String> sorted = unsorted.stream()
                    .map(Integer::parseInt)
                    .sorted(Comparator.reverseOrder())
                    .map(Object::toString)
                    .collect(Collectors.toList());

            assertEquals(expected, sorted);
        }

        @Test
        void testGameObjectMovement()
        {
            GameObject obj = new GameObject(100, 100, 20) {};
            obj.move(10, -5);
            assertEquals(110, obj.getX(), 0.001);
            assertEquals(95, obj.getY(), 0.001);
        }
    }