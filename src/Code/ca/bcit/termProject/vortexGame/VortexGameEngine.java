package ca.bcit.termProject.vortexGame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static ca.bcit.termProject.vortexGame.Player.INITIAL_BOOST_LIMIT;
import static ca.bcit.termProject.vortexGame.PowerUp.spawnPowerUp;
import static ca.bcit.termProject.vortexGame.Projectile.MAX_PROJECTILE_SIZE;
import static ca.bcit.termProject.vortexGame.Projectile.MIN_PROJECTILE_SIZE;
import static ca.bcit.termProject.vortexGame.Star.spawnStars;

/**
 * The main game engine class for the Vortex bullet hell game.
 *
 * <p>This class serves as the core game engine that manages:
 * <ul>
 *   <li>The game loop and frame-by-frame updates</li>
 *   <li>Player input handling and movement</li>
 *   <li>Object spawning (projectiles, power-ups, stars)</li>
 *   <li>Collision detection between game objects</li>
 *   <li>Game state management (menu, playing, game over)</li>
 *   <li>Difficulty progression over time</li>
 *   <li>Score tracking and persistence</li>
 * </ul>
 *
 * <p>The engine extends JavaFX's {@link Application} class to provide the game window
 * and rendering system, using a hierarchical scene graph for efficient rendering.</p>
 *
 * <p>Key Features:
 * <ul>
 *   <li>Progressive difficulty system that increases spawn rates logarithmically</li>
 *   <li>Boost mechanic with visual feedback through progress bars</li>
 *   <li>Randomized projectile spawning from screen edges</li>
 *   <li>Power-up system with temporary player enhancements</li>
 *   <li>Particle effects through the star background system</li>
 * </ul>
 *
 * <p>The game follows a standard state pattern with three main states:
 * <ol>
 *   <li>MENU - Shows start screen and high scores</li>
 *   <li>PLAYING - Active game session</li>
 *   <li>GAME_OVER - Shows survival time and restart options</li>
 * </ol>
 *
 * <p>All game objects are managed through the root {@link Pane} which serves as the
 * container for the scene graph. The engine implements a fixed time-step game loop
 * using JavaFX's {@link AnimationTimer} for consistent performance across hardware.</p>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class VortexGameEngine extends Application
{
    //Window
    /**
     * Screen width in pixels
     */
    static final int SCREEN_WIDTH_PX = 1000;
    /**
     * Screen height in pixels
     */
    static final int SCREEN_HEIGHT_PX = 750;
    /**
     * Half the screen width in pixels
     */
    static final double HALF_SCREEN_WIDTH_PX          = (double) SCREEN_WIDTH_PX / 2;
    private static final double HALF_SCREEN_HEIGHT_PX = (double) SCREEN_HEIGHT_PX / 2;

    //Program Logic
    private static final int INITIAL_VALUE                      = 0;
    private static final double PROJECTILE_SPAWN_EDGE_CHANCE    = 0.5;
    private static final long NANOSECONDS_PER_FRAME             = 16_000_000;
    private static final int MILLISECONDS_PER_SECOND            = 1000;
    private static final int INIT_LOG_SCALING_MULTIPLIER        = 1;

    //GUI
    private static final int START_TEXT_OFFSET_X            = 70;
    private static final int START_TEXT_OFFSET_Y            = 0;
    private static final int SURVIVAL_TIME_TEXT_OFFSET_X    = 10;
    private static final int SURVIVAL_TIME_TEXT_OFFSET_Y    = 20;
    private static final int BOOST_BAR_OFFSET_X             = 140;
    private static final int BOOST_BAR_OFFSET_Y             = 5;
    private static final double BOOST_BAR_SCALE_X           = 1.8;
    private static final double BOOST_BAR_SCALE_Y           = 1.2;
    private static final int INITIALIZE_BOOST               = 1;

    //Game Objects
    private static final int POWER_UP_SPAWN_RATE    = 5;
    private static final int POWER_UP_INITIAL_DELAY = 5;
    /**
     * Universal pixel size of the player
     */
    static final int PLAYER_SIZE = 30;
    private static final double HALF_PLAYER_SIZE                = (double) PLAYER_SIZE / 2;
    private static final int INITIAL_PROJECTILE_SPAWN_RATE      = 50;
    private static final int MIN_PROJECTILE_SPAWN_RATE          = 10;
    private static final int TIME_TO_MAX_DIFFICULTY             = 60;
    private static final int PROJECTILE_SPAWN_UPDATE_INTERVAL   = 5;

    private Pane root;
    private Player player;
    private long startTime;
    private int projectileSpawnCounter  = INITIAL_VALUE;
    private int projectileSpawnRate     = INITIAL_PROJECTILE_SPAWN_RATE;
    private boolean powerUpSpawnedThisSecond;
    private boolean WPressed      = false;
    private boolean SPressed      = false;
    private boolean APressed      = false;
    private boolean DPressed      = false;
    private boolean ShiftPressed  = false;

    private enum GameState
    {
        MENU,
        PLAYING,
        GAME_OVER
    }
    private GameState currentState = GameState.MENU;

    private Text startText;
    private Text survivalTimeText;
    private AnimationTimer gameLoop;
    private ProgressBar boostBar;

    /**
     * Initializes the JavaFX application and configures the primary stage.
     *
     * <p>This method:
     * <ul>
     *   <li>Sets up the main game scene</li>
     *   <li>Loads CSS stylesheets</li>
     *   <li>Configures window properties</li>
     *   <li>Displays the initial menu screen</li>
     * </ul>
     *
     * @param primaryStage The primary window container for JavaFX
     */
    @Override
    public void start(final Stage primaryStage)
    {
        final VortexGameEngine vortexGameEngine;
        final Scene scene;

        vortexGameEngine = new VortexGameEngine();

        scene = new Scene(vortexGameEngine.initializeContent(), SCREEN_WIDTH_PX, SCREEN_HEIGHT_PX);

        scene.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/VortexDesign.css")).toExternalForm());

        primaryStage.setTitle("Vortex - Bullet Hell");
        primaryStage.setScene(scene);
        vortexGameEngine.showMainMenu();
        primaryStage.show();
        primaryStage.toFront();
        primaryStage.requestFocus();
    }

    /**
     * Transitions to the main menu screen, clearing any existing game state.
     *
     * <p>Effects:
     * <ul>
     *   <li>Sets game state to MENU</li>
     *   <li>Clears all game objects from the scene</li>
     *   <li>Instantiates a new MainMenu instance</li>
     * </ul>
     */
    public void showMainMenu()
    {
        currentState = GameState.MENU;
        root.getChildren().clear();
        root.getChildren().add(new MainMenu(this));
    }

    /**
     * Displays the game over screen with survival statistics.
     *
     * @param survivalTime The player's survival time in seconds
     */
    public void showGameOverScreen(final int survivalTime)
    {
        currentState = GameState.GAME_OVER;
        root.getChildren().clear();
        root.getChildren().add(new GameOverScreen(this, survivalTime));
    }

    /**
     * Initializes all game visual elements and control systems.
     *
     * <p>Creates and configures:
     * <ul>
     *   <li>Player character at screen center</li>
     *   <li>Background star particles</li>
     *   <li>UI elements (timer text, boost meter)</li>
     *   <li>Keyboard input handlers</li>
     * </ul>
     */
    public void createContent()
    {
        spawnStars(this);

        player = new Player(HALF_SCREEN_WIDTH_PX - HALF_PLAYER_SIZE,
                HALF_SCREEN_HEIGHT_PX - HALF_PLAYER_SIZE,
                PLAYER_SIZE);
        root.getChildren().add(player);

        startText = new Text("Press Enter to Start");
        startText.getStyleClass().add("start-text");
        startText.setX(HALF_SCREEN_WIDTH_PX - START_TEXT_OFFSET_X);
        startText.setY(HALF_SCREEN_HEIGHT_PX + START_TEXT_OFFSET_Y);
        root.getChildren().add(startText);

        survivalTimeText = new Text("Time: 0s");
        survivalTimeText.getStyleClass().add("survival-time-text");
        survivalTimeText.setX(SURVIVAL_TIME_TEXT_OFFSET_X);
        survivalTimeText.setY(SURVIVAL_TIME_TEXT_OFFSET_Y);
        root.getChildren().add(survivalTimeText);

        boostBar = new ProgressBar(INITIALIZE_BOOST);
        boostBar.setTranslateX(BOOST_BAR_OFFSET_X);
        boostBar.setTranslateY(BOOST_BAR_OFFSET_Y);
        boostBar.setScaleX(BOOST_BAR_SCALE_X);
        boostBar.setScaleY(BOOST_BAR_SCALE_Y);
        root.getChildren().add(boostBar);

        root.setOnKeyPressed(e ->
        {
            KeyCode code = e.getCode();
            if (code == KeyCode.W) WPressed = true;
            if (code == KeyCode.S) SPressed = true;
            if (code == KeyCode.A) APressed = true;
            if (code == KeyCode.D) DPressed = true;
            if (code == KeyCode.SHIFT) ShiftPressed = true;
        });

        root.setOnKeyReleased(e ->
        {
            KeyCode code = e.getCode();
            if (code == KeyCode.W)
            {
                WPressed = false;
            }
            if (code == KeyCode.S)
            {
                SPressed = false;
            }
            if (code == KeyCode.A)
            {
                APressed = false;
            }
            if (code == KeyCode.D)
            {
                DPressed = false;
            }
            if (code == KeyCode.SHIFT)
            {
                ShiftPressed = false;
            }
        });
    }

    /**
     * Begins a new game session with fresh game state.
     *
     * <p>This method:
     * <ul>
     *   <li>Resets all game statistics</li>
     *   <li>Initializes a new game loop</li>
     *   <li>Sets initial difficulty parameters</li>
     *   <li>Resets player state and position</li>
     * </ul>
     */
    public void startGame()
    {
        root.getChildren().clear();
        this.createContent();
        currentState = GameState.PLAYING;
        startText.setVisible(false);
        startTime = System.currentTimeMillis();
        projectileSpawnRate = INITIAL_PROJECTILE_SPAWN_RATE;
        powerUpSpawnedThisSecond = false;
        player.resetStats();
        gameLoop = new AnimationTimer()
        {
            private long lastUpdate = INITIAL_VALUE;
            @Override
            public void handle(long now)
            {
                if (currentState == GameState.PLAYING)
                {
                    if (now - lastUpdate >= NANOSECONDS_PER_FRAME)
                    {
                        update();
                        lastUpdate = now;
                    }
                }
            }
        };
        root.requestFocus();
        gameLoop.start();
    }

    /**
     * Returns the root pane of the game scene, which contains all visual elements.
     *
     * @return The root {@link Pane} used for rendering the game.
     */
    public Pane getRoot()
    {
        return root;
    }

    /*
     * Initializes the root pane with default settings.
     * @return The configured root pane.
     */
    private Pane initializeContent()
    {
        root = new Pane();
        root.setPrefSize(SCREEN_WIDTH_PX, SCREEN_HEIGHT_PX);
        root.setFocusTraversable(true);
        return root;
    }

    /*
     * Main game update cycle executed each frame (~60 times per second).
     */
    private void update()
    {
        long currentTime;
        long survivalTime;

        player.updateMovement(WPressed, SPressed, APressed, DPressed, ShiftPressed);
        spawnProjectiles();
        moveProjectiles();
        checkCollisions();

        currentTime = System.currentTimeMillis();
        survivalTime = (currentTime - startTime) / MILLISECONDS_PER_SECOND;

        survivalTimeText.setText("Time: " + survivalTime + "s");

        boostBar.setProgress((double) player.getBoost()/player.getMaxBoost());
        if (player.isBoostCut())
        {
            boostBar.setId("boostCut");
        }
        else if (player.getBoost() > INITIAL_BOOST_LIMIT)
        {
            boostBar.setId("boostOverCharge");
        }
        else
        {
            boostBar.setId("");
        }

        if (survivalTime % PROJECTILE_SPAWN_UPDATE_INTERVAL == INITIAL_VALUE &&
                survivalTime > INITIAL_VALUE) // Skip the very first frame
        {
            // Logarithmic difficulty scaling
            double progress;
            progress = Math.min(INIT_LOG_SCALING_MULTIPLIER, (double)survivalTime / TIME_TO_MAX_DIFFICULTY);
            projectileSpawnRate = (int)(INITIAL_PROJECTILE_SPAWN_RATE -
                    (INITIAL_PROJECTILE_SPAWN_RATE - MIN_PROJECTILE_SPAWN_RATE) *
                            Math.log1p(progress * (Math.E - INIT_LOG_SCALING_MULTIPLIER)));
        }

        if (survivalTime >= POWER_UP_INITIAL_DELAY &&
                survivalTime % POWER_UP_SPAWN_RATE == INITIAL_VALUE)
        {
            if (!powerUpSpawnedThisSecond)
            {
                spawnPowerUp(this);
                powerUpSpawnedThisSecond = true;
            }
        }
        else
        {
            powerUpSpawnedThisSecond = false;
        }
    }

    /*
     * Generates new projectiles based on current spawn rate.
     */
    private void spawnProjectiles()
    {
        final Random rand;

        rand = new Random();

        if (projectileSpawnCounter % projectileSpawnRate == INITIAL_VALUE)
        {
            double x;
            double y;

            x = rand.nextInt(SCREEN_WIDTH_PX);
            y = rand.nextInt(SCREEN_HEIGHT_PX);

            if (rand.nextDouble() < PROJECTILE_SPAWN_EDGE_CHANCE)
            {
                x = rand.nextDouble() < PROJECTILE_SPAWN_EDGE_CHANCE ?
                        INITIAL_VALUE - MAX_PROJECTILE_SIZE : SCREEN_WIDTH_PX + MAX_PROJECTILE_SIZE;
            }
            else
            {
                y = rand.nextDouble() < PROJECTILE_SPAWN_EDGE_CHANCE ?
                        INITIAL_VALUE - MAX_PROJECTILE_SIZE : SCREEN_HEIGHT_PX + MAX_PROJECTILE_SIZE;
            }

            final Projectile projectile;

            projectile = new Projectile(x, y,
                    rand.nextInt(MAX_PROJECTILE_SIZE - MIN_PROJECTILE_SIZE) + MIN_PROJECTILE_SIZE
            );
            root.getChildren().add(projectile);
        }
        projectileSpawnCounter++;
    }

    /*
     * Updates positions of all active projectiles and culls off-screen ones.
     */
    private void moveProjectiles()
    {
        final List<Projectile> projectilesToRemove;
        final List<javafx.scene.Node> childrenCopy;

        projectilesToRemove = new ArrayList<>();
        childrenCopy = new ArrayList<>(root.getChildren());

        childrenCopy.forEach(node ->
        {
            if (node instanceof final Projectile projectile)
            {
                projectile.updateMovement();
                if (projectile.isOffScreen(SCREEN_WIDTH_PX + MAX_PROJECTILE_SIZE,
                        SCREEN_HEIGHT_PX + MAX_PROJECTILE_SIZE))
                {
                    projectilesToRemove.add(projectile);
                }
            }
        });
        root.getChildren().removeAll(projectilesToRemove);
    }

    /**
     * Detects collisions between player and game objects.
     *
     */
    private void checkCollisions()
    {
        final List<GameObject> objectsToRemove;
        final List<javafx.scene.Node> childrenCopy;

        objectsToRemove = new ArrayList<>();
        childrenCopy = new ArrayList<>(root.getChildren());

        childrenCopy.forEach(node ->
        {
            if (node instanceof final Projectile projectile)
            {
                if (player.getBoundsInParent().intersects(projectile.getBoundsInParent()))
                {
                    endGame();
                    objectsToRemove.add(projectile);
                }
            }
            else if (node instanceof final PowerUp powerUp)
            {
                if (player.getBoundsInParent().intersects(powerUp.getBoundsInParent()))
                {
                    powerUp.applyEffect(player);
                    objectsToRemove.add(powerUp);
                }
            }
        });

        root.getChildren().removeAll(objectsToRemove);
    }

    /*
     * Terminates current game session and transitions to game over state.
     *
     */
    private void endGame()
    {
        final long endTime;
        final long survivalTime;

        currentState = GameState.GAME_OVER;

        endTime = System.currentTimeMillis();
        survivalTime = (endTime - startTime) / MILLISECONDS_PER_SECOND;

        this.showGameOverScreen((int) survivalTime);
        ScoreManager.saveScore(survivalTime);
        if (gameLoop != null)
        {
            gameLoop.stop();
        }
    }
}