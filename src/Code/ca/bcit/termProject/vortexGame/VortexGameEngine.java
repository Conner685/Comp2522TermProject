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
import static ca.bcit.termProject.vortexGame.Star.spawnStars;

/**
 * The main game engine class for the Vortex bullet hell game.
 * Manages the game loop, player input, object spawning, collisions, and game state transitions.
 * Extends JavaFX's Application class to provide the game window and rendering system.
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class VortexGameEngine extends Application
{
    //Window
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 750;
    static final double HALF_SCREEN_WIDTH = (double) SCREEN_WIDTH / 2;
    private static final double HALF_SCREEN_HEIGHT = (double) SCREEN_HEIGHT / 2;

    //Program Logic
    private static final int INITIAL_STAT = 0;
    private static final double PROJECTILE_SPAWN_EDGE_CHANCE = 0.5;
    private static final long NANOSECONDS_PER_FRAME = 16_000_000;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int INIT_LOG_SCALING = 1;

    //GUI
    private static final int START_TEXT_OFFSET_X = 70;
    private static final int START_TEXT_OFFSET_Y = 0;
    private static final int SURVIVAL_TIME_TEXT_OFFSET_X = 10;
    private static final int SURVIVAL_TIME_TEXT_OFFSET_Y = 20;
    private static final int BOOST_BAR_OFFSET_X = 140;
    private static final int BOOST_BAR_OFFSET_Y = 5;
    private static final double BOOST_BAR_SCALE_X = 1.8;
    private static final double BOOST_BAR_SCALE_Y = 1.2;
    private static final int INITIALIZE_BOOST = 1;

    //Game Objects
    private static final int POWER_UP_SPAWN_RATE = 5;
    private static final int POWER_UP_INITIAL_DELAY = 5;
    private static final int PLAYER_SIZE = 30;
    private static final double HALF_PLAYER_SIZE = (double) PLAYER_SIZE / 2;
    private static final int MIN_PROJECTILE_SIZE = 10;
    private static final int MAX_PROJECTILE_SIZE = 60;
    private static final int INITIAL_PROJECTILE_SPAWN_RATE = 50;
    private static final int MIN_PROJECTILE_SPAWN_RATE = 10;
    private static final int TIME_TO_MAX_DIFFICULTY = 60;     // 1 minutes to reach max difficulty
    private static final int PROJECTILE_SPAWN_UPDATE_INTERVAL = 5;

    private Pane root;
    private Player player;
    private long startTime;
    private int projectileSpawnCounter = INITIAL_STAT;
    private int projectileSpawnRate = INITIAL_PROJECTILE_SPAWN_RATE;
    private boolean powerUpSpawnedThisSecond;
    private boolean isWPressed = false;
    private boolean isSPressed = false;
    private boolean isAPressed = false;
    private boolean isDPressed = false;
    private boolean isShiftPressed = false;

    /**
     * Enum representing the possible states of the game.
     */
    private enum GameState { MENU, PLAYING, GAME_OVER }
    private GameState currentState = GameState.MENU;

    private Text startText;
    private Text survivalTimeText;
    private AnimationTimer gameLoop;
    private ProgressBar boostBar;

    /**
     * The main entry point for the JavaFX application.
     * @param primaryStage The primary stage for this application
     */
    @Override
    public void start(final Stage primaryStage)
    {
        final VortexGameEngine vortexGameEngine;
        final Scene scene;

        vortexGameEngine = new VortexGameEngine();

        scene = new Scene(vortexGameEngine.initializeContent(), SCREEN_WIDTH, SCREEN_HEIGHT);

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
     * Shows the main menu screen.
     */
    public void showMainMenu()
    {
        currentState = GameState.MENU;
        root.getChildren().clear();
        root.getChildren().add(new MainMenu(this));
    }

    /**
     * Shows the main menu screen.
     */
    public void showGameOverScreen(final int survivalTime)
    {
        currentState = GameState.GAME_OVER;
        root.getChildren().clear();
        root.getChildren().add(new GameOverScreen(this, survivalTime));
    }

    /**
     * initializes the root pane
     * @return root pane
     */
    private Pane initializeContent()
    {
        root = new Pane();
        root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.setFocusTraversable(true);
        return root;
    }

    /**
     * Creates and configures the main game content pane.
     */
    public void createContent()
    {
        spawnStars(this);

        player = new Player(HALF_SCREEN_WIDTH - HALF_PLAYER_SIZE,
                HALF_SCREEN_HEIGHT - HALF_PLAYER_SIZE,
                PLAYER_SIZE);
        root.getChildren().add(player);

        startText = new Text("Press Enter to Start");
        startText.getStyleClass().add("start-text");
        startText.setX(HALF_SCREEN_WIDTH - START_TEXT_OFFSET_X);
        startText.setY(HALF_SCREEN_HEIGHT + START_TEXT_OFFSET_Y);
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
            if (code == KeyCode.W) isWPressed = true;
            if (code == KeyCode.S) isSPressed = true;
            if (code == KeyCode.A) isAPressed = true;
            if (code == KeyCode.D) isDPressed = true;
            if (code == KeyCode.SHIFT) isShiftPressed = true;
        });

        root.setOnKeyReleased(e ->
        {
            KeyCode code = e.getCode();
            if (code == KeyCode.W) isWPressed = false;
            if (code == KeyCode.S) isSPressed = false;
            if (code == KeyCode.A) isAPressed = false;
            if (code == KeyCode.D) isDPressed = false;
            if (code == KeyCode.SHIFT) isShiftPressed = false;
        });
    }

    /**
     * Starts a new game session, initializing all game state.
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
            private long lastUpdate = INITIAL_STAT;
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
     * Main game update loop that handles all game logic each frame.
     */
    private void update()
    {
        long currentTime;
        long survivalTime;

        player.updateMovement(isWPressed, isSPressed, isAPressed, isDPressed, isShiftPressed);
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

        if (survivalTime % PROJECTILE_SPAWN_UPDATE_INTERVAL == INITIAL_STAT &&
                survivalTime > INITIAL_STAT) // Skip the very first frame
        {
            // Logarithmic difficulty scaling
            double progress;
            progress = Math.min(INIT_LOG_SCALING, (double)survivalTime / TIME_TO_MAX_DIFFICULTY);
            projectileSpawnRate = (int)(INITIAL_PROJECTILE_SPAWN_RATE -
                    (INITIAL_PROJECTILE_SPAWN_RATE - MIN_PROJECTILE_SPAWN_RATE) *
                            Math.log1p(progress * (Math.E - INIT_LOG_SCALING)));
        }

        if (survivalTime >= POWER_UP_INITIAL_DELAY &&
                survivalTime % POWER_UP_SPAWN_RATE == INITIAL_STAT)
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

    /**
     * Spawns new projectile enemies based on current spawn rate.
     */
    private void spawnProjectiles()
    {
        final Random rand;

        rand = new Random();

        if (projectileSpawnCounter % projectileSpawnRate == INITIAL_STAT)
        {
            double x;
            double y;

            x = rand.nextInt(SCREEN_WIDTH);
            y = rand.nextInt(SCREEN_HEIGHT);

            if (rand.nextDouble() < PROJECTILE_SPAWN_EDGE_CHANCE)
            {
                x = rand.nextDouble() < PROJECTILE_SPAWN_EDGE_CHANCE ?
                        INITIAL_STAT - MAX_PROJECTILE_SIZE : SCREEN_WIDTH + MAX_PROJECTILE_SIZE;
            }
            else
            {
                y = rand.nextDouble() < PROJECTILE_SPAWN_EDGE_CHANCE ?
                        INITIAL_STAT - MAX_PROJECTILE_SIZE : SCREEN_HEIGHT + MAX_PROJECTILE_SIZE;
            }

            final Projectile projectile;

            projectile = new Projectile(
                    x, y,
                    rand.nextInt(MAX_PROJECTILE_SIZE - MIN_PROJECTILE_SIZE) + MIN_PROJECTILE_SIZE
            );
            root.getChildren().add(projectile);
        }
        projectileSpawnCounter++;
    }

    /**
     * Updates positions of all projectiles and removes off-screen ones.
     */
    private void moveProjectiles()
    {
        List<Projectile> projectilesToRemove = new ArrayList<>();

        // Create a copy of the children list to avoid ConcurrentModificationException
        final List<javafx.scene.Node> childrenCopy;
        childrenCopy = new ArrayList<>(root.getChildren());

        childrenCopy.forEach(node ->
        {
            if (node instanceof final Projectile projectile)
            {
                projectile.updateMovement();
                if (projectile.isOffScreen(SCREEN_WIDTH + MAX_PROJECTILE_SIZE,
                        SCREEN_HEIGHT + MAX_PROJECTILE_SIZE))
                {
                    projectilesToRemove.add(projectile);
                }
            }
        });
        root.getChildren().removeAll(projectilesToRemove);
    }

    /**
     * Checks for collisions between player and other game objects.
     */
    private void checkCollisions()
    {
        final List<GameObject> objectsToRemove;
        objectsToRemove = new ArrayList<>();

        // Create a copy of the children list to avoid ConcurrentModificationException
        final List<javafx.scene.Node> childrenCopy;
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

    /**
     * Ends the current game session and shows game over screen.
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

    /**
     * Gets the root
     *
     * @return root pane
     */
    public Pane getRoot()
    {
        return root;
    }
}