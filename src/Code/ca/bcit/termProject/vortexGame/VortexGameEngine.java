package ca.bcit.termProject.vortexGame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * The main game engine class for the Vortex bullet hell game.
 * Manages the game loop, player input, object spawning, collisions, and game state transitions.
 * Extends JavaFX's Application class to provide the game window and rendering system.
 */
public class VortexGameEngine extends Application
{
    //Window
    protected static final int SCREEN_WIDTH = 1000;
    protected static final int SCREEN_HEIGHT = 750;
    protected static final double HALF_SCREEN_WIDTH = (double) SCREEN_WIDTH / 2;
    protected static final double HALF_SCREEN_HEIGHT = (double) SCREEN_HEIGHT / 2;

    //Program Logic
    private static final int INITIAL_STAT = 0;
    private static final double PROJECTILE_SPAWN_EDGE_CHANCE = 0.5;
    private static final long NANOSECONDS_PER_FRAME = 16_000_000;
    private static final int MILLISECONDS_PER_SECOND = 1000;

    //GUI
    //TODO Design full main menu and game over screen,
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
    private static final int MIN_STARS = 10;
    private static final int MAX_STARS = 50;
    private static final BoxBlur STAR_BLUR = new BoxBlur();
    private static final int STAR_BLUR_INTENSITY = 5;
    private static final int POWER_UP_SPAWN_RATE = 5;
    private static final int POWER_UP_SIZE = 20;
    private static final int POWER_UP_INITIAL_DELAY = 5;
    private static final PowerUp.PowerUpType[] POWER_UP_TYPES = PowerUp.PowerUpType.values();
    private static final int PLAYER_SIZE = 30;
    private static final double HALF_PLAYER_SIZE = (double) PLAYER_SIZE / 2;
    private static final int INITIAL_PROJECTILE_SPAWN_RATE = 100;
    private static final int MIN_PROJECTILE_SIZE = 10;
    private static final int MAX_PROJECTILE_SIZE = 60;
    private static final int PROJECTILE_SPAWN_RATE_DECREMENT = 5;
    private static final int MIN_PROJECTILE_SPAWN_RATE = 10;

    //TODO Fix this mess
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

        scene = new Scene(vortexGameEngine.initilizeContent(), SCREEN_WIDTH, SCREEN_HEIGHT);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/VortexDesign.css")).toExternalForm());

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

    private Pane initilizeContent()
    {
        root = new Pane();
        root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.setFocusTraversable(true);
        return root;
    }

    /**
     * Creates and configures the main game content pane.
     */
    public void createContent() //TODO Make this a separate class(?)
    {
        STAR_BLUR.setWidth(STAR_BLUR_INTENSITY);
        STAR_BLUR.setHeight(STAR_BLUR_INTENSITY);
        spawnStars();

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
            private long lastUpdate = 0;
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
        gameLoop.start();
    }

    /**
     * Main game update loop that handles all game logic each frame.
     */
    private void update()
    {
        player.updateMovement(isWPressed, isSPressed, isAPressed, isDPressed, isShiftPressed);
        spawnProjectiles();
        moveProjectiles();
        checkCollisions();

        long currentTime;
        long survivalTime;

        currentTime = System.currentTimeMillis();
        survivalTime = (currentTime - startTime) / MILLISECONDS_PER_SECOND;

        survivalTimeText.setText("Time: " + survivalTime + "s");

        boostBar.setProgress((double) player.getBoost()/player.getMaxBoost());
        if (player.isBoostCut())
        {
            boostBar.setId("boostCut");
        }
        else
        {
            boostBar.setId("");
        }

        //TODO Scaling is fucked right now, fix it so people could conceivably get more then 50 seconds
        if (survivalTime % PROJECTILE_SPAWN_RATE_DECREMENT == INITIAL_STAT &&
                projectileSpawnRate > MIN_PROJECTILE_SPAWN_RATE)
        {
            projectileSpawnRate--;
        }

        if (survivalTime >= POWER_UP_INITIAL_DELAY &&
                survivalTime % POWER_UP_SPAWN_RATE == INITIAL_STAT)
        {
            if (!powerUpSpawnedThisSecond)
            {
                spawnPowerUp();
                System.out.println("Power up spawned at: " + survivalTime);
                powerUpSpawnedThisSecond = true;
            }
        }
        else
        {
            powerUpSpawnedThisSecond = false;
        }

//        System.out.println(player.getSpeedModifier());
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
     * Creates background star elements for visual effect.
     */
    private void spawnStars()
    {
        final Random rand;
        final int totalStars;

        rand = new Random();
        totalStars = rand.nextInt(MAX_STARS - MIN_STARS) + MIN_STARS;

        for (int i = 0; i < totalStars; i++)
        {
            final Star star;

            star = new Star();

            star.setEffect(STAR_BLUR);

            root.getChildren().addFirst(star);
        }
    }

    /**
     * Spawns a random power-up at a random position.
     */
    private void spawnPowerUp()
    {
        final double x;
        final double y;
        final Random rand;
        final PowerUp powerUp;
        final PowerUp.PowerUpType type;

        rand = new Random();

        x = rand.nextInt(SCREEN_WIDTH - POWER_UP_SIZE);
        y = rand.nextInt(SCREEN_HEIGHT - POWER_UP_SIZE);

        type = POWER_UP_TYPES[rand.nextInt(POWER_UP_TYPES.length)];

        powerUp = switch (type)
        {
            case SPEED_BOOST -> new SpeedBoostPowerUp(x, y);
            case BOOST_UP -> new BoostUpPowerUp(x, y);
            case REFRESH_BOOST -> new RefreshBoostPowerUp(x, y);
        };

        root.getChildren().add(powerUp);
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
        currentState = GameState.GAME_OVER;
        final long endTime;
        final long survivalTime;

        endTime = System.currentTimeMillis();
        survivalTime = (endTime - startTime) / MILLISECONDS_PER_SECOND;

        this.showGameOverScreen((int) survivalTime);
        ScoreManager.saveScore(survivalTime);
        if (gameLoop != null)
        {
            gameLoop.stop();
        }
    }

    public Pane getRoot()
    {
        return root;
    }
}