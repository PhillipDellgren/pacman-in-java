package dk.sdu.imada.oop26.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import dk.sdu.imada.oop26.model.Entities.PacMan;
import dk.sdu.imada.oop26.model.GameModel;
import dk.sdu.imada.oop26.model.GameModel.Directions;
import dk.sdu.imada.oop26.model.GameModel.Entities;
import dk.sdu.imada.oop26.model.GameModel.GameStates;
import dk.sdu.imada.oop26.model.GameObserver;
import dk.sdu.imada.oop26.model.ViewObserver;
import dk.sdu.imada.oop26.model.maze.MazeModel;
import dk.sdu.imada.oop26.view.EndGameView;
import dk.sdu.imada.oop26.view.GameView;
import dk.sdu.imada.oop26.view.StartView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


/**
 * The class @App retpresents the controller of the MVC pattern.
 * It creates the model and initiates the view. It also registers the view on
 * the model allowing
 * the view to oberver the model.
 * 
 * @author DM575-OOP
 */
public class Main extends Application implements GameObserver, ViewObserver {
	private GameModel model;
    private MazeModel maze;
    private AnimationTimer animationTimer;

    private Stage primaryStage;
    private StartView startview;
    private GameView gameview;
    private EndGameView endGameView;
    private PacMan pacMan;
    private int canvasRows;
    private int canvasCols;
    private final String path = "/media/maze";
    private String mapPath;
    private int currentlevel = 1;
    private int Score; 

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setTitle("PacMan 2026");
        primaryStage.setResizable(false);

        var stream = getClass().getResourceAsStream(path + currentlevel + ".txt");
        List<String> lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();

        this.canvasRows = lines.size() + 2; //to have space for lives left and score
        this.canvasCols = lines.get(0).length();

        startview = new StartView(canvasCols, canvasRows);
        startview.registerViewObserver(this);
        

        primaryStage.setScene(startview.getScene());
        primaryStage.show();

    } 

	public static void main(String[] args) {
		launch();
	}
    public void handleKeyPress(KeyEvent event){
        if (event.getCode() == KeyCode.LEFT) {
            
        }
    }

    public void onStartPressed() {
    //     maze = new MazeModel();
    //     try {
    //         maze.loadFromFile(path);
    //     } catch (IOException e) {
    //     }
    //     model = new GameModel(maze);
    //     gameview = new GameView(this, maze, model); // view receives the ready model
    //     pacMan = (PacMan) model.getEntity(Entities.PACMAN);
    //     model.registerObserver(gameview);
    //     model.registerObserver(this);
        
    //     primaryStage.setScene(gameview.getScene());

    //     gameview.getScene().setOnKeyPressed(event -> {
    //         switch (event.getCode()) {
    //             case UP    -> pacMan.setPendingDirection(Directions.UP);
    //             case DOWN  -> pacMan.setPendingDirection(Directions.DOWN);
    //             case LEFT  -> pacMan.setPendingDirection(Directions.LEFT);
    //             case RIGHT -> pacMan.setPendingDirection(Directions.RIGHT);
    //             default    -> pacMan.setPendingDirection(pacMan.getPendingDirection());
    //         }
    //     });

    //     animationTimer = new AnimationTimer() {

    //         @Override
    //         public void handle(long now) {
    //             model.update(now);
    //         }

    //     };
    //     animationTimer.start();

    }

    @Override
    public void onGameStateChange() {
        throw new UnsupportedOperationException("Unimplemented method 'onGameStateChange'");
    }

    @Override
    public void update(GameModel gameModel) {
        if (gameModel.getCurrentGameState() == GameStates.FINISHED) {
            animationTimer.stop();
            gameview.clear();
            endGameView = new EndGameView(model, this, canvasCols, canvasRows);
            primaryStage.setScene(endGameView.getScene());
        }
    }

    @Override
    public void viewStart() {
    maze = new MazeModel();
            try {
                maze.loadFromFile(path + currentlevel + ".txt");
            } 
            catch (IOException e) {
            }
            model = new GameModel(maze, currentlevel, 0, 3);
            gameview = new GameView(this, maze, model); // view receives the ready model
            pacMan = (PacMan) model.getEntity(Entities.PACMAN);
            model.registerObserver(gameview);
            model.registerObserver(this);
            
            primaryStage.setScene(gameview.getScene());

            gameview.getScene().setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case UP    -> pacMan.setPendingDirection(Directions.UP);
                    case DOWN  -> pacMan.setPendingDirection(Directions.DOWN);
                    case LEFT  -> pacMan.setPendingDirection(Directions.LEFT);
                    case RIGHT -> pacMan.setPendingDirection(Directions.RIGHT);
                    default    -> pacMan.setPendingDirection(pacMan.getPendingDirection());
                }
            });

            animationTimer = new AnimationTimer() {

                @Override
                public void handle(long now) {
                    model.update(now);
                }

            };
            animationTimer.start();
    }

    public void nextLevel (int score, int lives){
        currentlevel = currentlevel + 1;
        Score = model.getScore();
        System.out.println("current level is: " + currentlevel);
        mapPath = path + currentlevel + ".txt";
        maze = new MazeModel();
            try {
                maze.loadFromFile(mapPath);
            } 
            catch (IOException e) {
            }
            model = new GameModel(maze, currentlevel, score, lives);
            gameview = new GameView(this, maze, model); // view receives the ready model
            pacMan = (PacMan) model.getEntity(Entities.PACMAN);
            model.registerObserver(gameview);
            model.registerObserver(this);

            primaryStage.setScene(gameview.getScene());

            gameview.getScene().setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case UP    -> pacMan.setPendingDirection(Directions.UP);
                    case DOWN  -> pacMan.setPendingDirection(Directions.DOWN);
                    case LEFT  -> pacMan.setPendingDirection(Directions.LEFT);
                    case RIGHT -> pacMan.setPendingDirection(Directions.RIGHT);
                    default    -> pacMan.setPendingDirection(pacMan.getPendingDirection());
                }
            });

            animationTimer = new AnimationTimer() {

                @Override
                public void handle(long now) {
                    model.update(now);
                }

            };
            animationTimer.start();
    }

}