package TermProject;

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

/**
 * Manages the core game logic, including player movement, projectile spawning, and collisions.
 */
public class VortexGameEngine extends Application
{
    //Window
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 750;
    private static final double HALF_SCREEN_WIDTH = (double) SCREEN_WIDTH / 2;
    private static final double HALF_SCREEN_HEIGHT = (double) SCREEN_HEIGHT / 2;

    //Program Logic
    private static final int INITIAL_STAT = 0;
    private static final double PROJECTILE_SPAWN_EDGE_CHANCE = 0.5;
    private static final long NANOSECONDS_PER_FRAME = 16_000_000;
    private static final int MILLISECONDS_PER_SECOND = 1000;

    //GUI
    private static final int START_TEXT_OFFSET_X = 70;
    private static final int START_TEXT_OFFSET_Y = 0;
    private static final int GAME_OVER_TEXT_OFFSET_X = 100;
    private static final int GAME_OVER_TEXT_OFFSET_Y = 30;
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

    private enum GameState { MENU, PLAYING, GAME_OVER }
    private GameState currentState = GameState.MENU;

    private Text startText;
    private Text gameOverText;
    private Text survivalTimeText;
    private AnimationTimer gameLoop;
    private ProgressBar boostBar;

    @Override
    public void start(final Stage primaryStage)
    {
        final VortexGameEngine vortexGameEngine;
        final Scene scene;
        vortexGameEngine = new VortexGameEngine();

        scene = new Scene(vortexGameEngine.createContent(), SCREEN_WIDTH, SCREEN_HEIGHT);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/VortexDesign.css")).toExternalForm());

        primaryStage.setTitle("Vortex - Bullet Hell");
        primaryStage.setScene(scene);
        primaryStage.show();

        vortexGameEngine.getRoot().requestFocus();
    }

    public Pane createContent()
    {
        root = new Pane();
        root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.setFocusTraversable(true);
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

        gameOverText = new Text();
        gameOverText.getStyleClass().add("game-over-text");
        gameOverText.setVisible(false);
        gameOverText.setX(HALF_SCREEN_WIDTH - GAME_OVER_TEXT_OFFSET_X);
        gameOverText.setY(HALF_SCREEN_HEIGHT + GAME_OVER_TEXT_OFFSET_Y);
        root.getChildren().add(gameOverText);

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
            if (code == KeyCode.ENTER && currentState != GameState.PLAYING)
            {
                if (currentState == GameState.MENU) startGame();
                else if (currentState == GameState.GAME_OVER) restartGame();
            }
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

        root.requestFocus();
        return root;
    }

    public void startGame()
    {
        currentState = GameState.PLAYING;
        startText.setVisible(false);
        gameOverText.setVisible(false);
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
                powerUpSpawnedThisSecond = true;
            }
        }
        else
        {
            powerUpSpawnedThisSecond = false;
        }

//        System.out.println(player.getSpeedModifier());
    }

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

    private void spawnStars()
    {
        final Random rand;
        final int totalStars;

        rand = new Random();
        totalStars = rand.nextInt(MAX_STARS - MIN_STARS) + MIN_STARS;

        for (int i = 0; i < totalStars; i++)
        {
            root.getChildren().addFirst(new Star());
        }
    }

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

    private void moveProjectiles()
    {
        List<Projectile> projectilesToRemove = new ArrayList<>();
        root.getChildren().forEach(node ->
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

    private void checkCollisions()
    {
        final List<GameObject> objectsToRemove = new ArrayList<>();
        root.getChildren().forEach(node ->
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

    private void endGame()
    {
        currentState = GameState.GAME_OVER;
        final long endTime;
        final long survivalTime;

        endTime = System.currentTimeMillis();
        survivalTime = (endTime - startTime) / MILLISECONDS_PER_SECOND;

        gameOverText.setText("Game Over! Final Score: " + survivalTime + "s\nPress Enter to Restart");
        gameOverText.setVisible(true);
        ScoreManager.saveScore(survivalTime);
        if (gameLoop != null)
        {
            gameLoop.stop();
        }
    }

    private void restartGame()
    {
        // Reset game objects
        root.getChildren().removeIf(node -> node instanceof Projectile);
        root.getChildren().removeIf(node -> node instanceof Star);
        root.getChildren().removeIf(node -> node instanceof PowerUp);
        spawnStars();

        // Reset player
        player.setX(HALF_SCREEN_WIDTH - HALF_PLAYER_SIZE);
        player.setY(HALF_SCREEN_HEIGHT - HALF_PLAYER_SIZE);
        player.resetStats();

        // Reset game state
        gameOverText.setVisible(false);
        projectileSpawnRate = INITIAL_PROJECTILE_SPAWN_RATE;
        currentState = GameState.PLAYING;
        startTime = System.currentTimeMillis();
        powerUpSpawnedThisSecond = false;
        root.requestFocus();
        gameLoop.start();
    }

    public static int getScreenWidth()
    {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight()
    {
        return SCREEN_HEIGHT;
    }

    public Pane getRoot()
    {
        return root;
    }
}