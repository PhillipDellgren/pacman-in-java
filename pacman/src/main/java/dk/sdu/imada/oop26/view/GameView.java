package dk.sdu.imada.oop26.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import dk.sdu.imada.oop26.controller.Main;
import dk.sdu.imada.oop26.model.GameObserver;
import dk.sdu.imada.oop26.model.GameModel;
import dk.sdu.imada.oop26.model.Entities.Entity;
import dk.sdu.imada.oop26.model.Entities.PacMan;
import dk.sdu.imada.oop26.model.Entities.Ghosts.Blinky;
import dk.sdu.imada.oop26.model.Entities.Ghosts.Clyde;
import dk.sdu.imada.oop26.model.Entities.Ghosts.Inky;
import dk.sdu.imada.oop26.model.Entities.Ghosts.Pinky;
import dk.sdu.imada.oop26.model.maze.MazeModel;
import dk.sdu.imada.oop26.model.maze.MazeModel.CellType;
import dk.sdu.imada.oop26.model.GameModel.Directions;
import dk.sdu.imada.oop26.model.GameModel.Entities;
import dk.sdu.imada.oop26.model.GameModel.GameStates;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class GameView implements GameObserver {

    private GameModel model;
    private MazeModel maze;

    public static double tileSize = 32;
	private double rowCount;
	private double columnCount;
	private double boardWidth;
	private double boardHeight;

    private Image WallImage;
    private Image GatesImage;
    private Image InkyImageUp;
    private Image InkyImageDown;
    private Image InkyImageLeft;
    private Image InkyImageRight;
    private Image PinkyImageUp;
    private Image PinkyImageDown;
    private Image PinkyImageLeft;
    private Image PinkyImageRight;
    private Image ClydeImageUp;
    private Image ClydeImageDown;
    private Image ClydeImageLeft;
    private Image ClydeImageRight;
    private Image BlinkyImageUp;
    private Image BlinkyImageDown;
    private Image BlinkyImageLeft;
    private Image BlinkyImageRight;
    private Image AngryBlinky;
    private Image GhostScared1; 
    private Image GhostScared2;
    private Image scaredImage; 
    private int scaredFrame = 0;
    private long lastScaredFrameSwitch = 0; 
    private static final long Scared_frame_Delay = 250_000_000; // 250 ms.  
    private Image PelletImage;
    private Image EmptyImage;
    private Image GhostDead; 

    private Image PacManUp;
    private Image PacManUp2;
    private Image PacManDown;
    private Image PacMandDown2;
    private Image PacManLeft;
    private Image PacManLeft2; 
    private Image PacManRight;
    private Image PacManRight2;
    private Image PacManClose;
    private int pacManFrame = 0; 
    private long lastPacManFrame = 0; 
    private static final long pacMan_frame_Delay = 50_000_000; // 50 ms. 
    private Image pacManImage;
    private Image FruitImage; 


    private int pelletPixelSize = 4;

    HashSet<Block> walls;
	HashSet<Block> foods;
	HashSet<Block> ghosts;
    HashSet<Block> empties;
    HashSet<Block> gates;
    HashSet<Block> fruits; 

	PacMan pacMan;
    Inky inky;
    Blinky blinky;
    Pinky pinky;
    Clyde clyde;
    private Canvas canvas;
    GraphicsContext gc;
    private List<Entity> entities = new ArrayList<Entity>();





    private Scene gameScene;    
    
    public GameView(Main app, MazeModel mazemodel, GameModel model){
        columnCount = mazemodel.getCols();
        rowCount = mazemodel.getRows() + 2;
        boardWidth = columnCount * mazemodel.getTileSize();
        boardHeight = rowCount * mazemodel.getTileSize();
        this.maze = mazemodel;
        this.model = model;
        Group root = new Group();
        gameScene = new Scene(root, boardWidth, boardHeight);
        gameScene.setFill(Color.BLACK);
        pacMan = (PacMan) model.getEntity(Entities.PACMAN);
        inky = (Inky) model.getEntity(Entities.INKY);
        blinky = (Blinky) model.getEntity(Entities.BLINKY);
        pinky = (Pinky) model.getEntity(Entities.PINKY);
        clyde = (Clyde) model.getEntity(Entities.CLYDE);

        entities.add(clyde);
        entities.add(inky);
        entities.add(blinky);
        entities.add(pinky);


        canvas = new Canvas(boardWidth, boardHeight);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        WallImage = new Image(
            getClass().getResourceAsStream("/media/wall.png"),
            boardWidth, boardHeight, true, false);

        InkyImageUp = new Image(
            getClass().getResourceAsStream("/media/ghosts/giu.png"),
            boardWidth, boardHeight, true, false);

        InkyImageDown = new Image(
            getClass().getResourceAsStream("/media/ghosts/gid.png"),
            boardWidth, boardHeight, true, false);

        InkyImageLeft = new Image(
            getClass().getResourceAsStream("/media/ghosts/gil.png"),
            boardWidth, boardHeight, true, false);
        
        InkyImageRight = new Image(
            getClass().getResourceAsStream("/media/ghosts/gir.png"),
            boardWidth, boardHeight, true, false);

        BlinkyImageUp = new Image(
            getClass().getResourceAsStream("/media/ghosts/gbu.png"),
            boardWidth, boardHeight, true, false);

        BlinkyImageDown = new Image(
            getClass().getResourceAsStream("/media/ghosts/gbd.png"),
            boardWidth, boardHeight, true, false);

        BlinkyImageLeft = new Image(
            getClass().getResourceAsStream("/media/ghosts/gbl.png"),
            boardWidth, boardHeight, true, false);

        BlinkyImageRight = new Image(
            getClass().getResourceAsStream("/media/ghosts/gbr.png"),
            boardWidth, boardHeight, true, false);

        AngryBlinky = new Image(
            getClass().getResourceAsStream("/media/ghosts.png"),
            boardWidth, boardHeight, true, false);

        ClydeImageUp = new Image(
            getClass().getResourceAsStream("/media/ghosts/gcu.png"),
            boardWidth, boardHeight, true, false);

        ClydeImageDown = new Image(
            getClass().getResourceAsStream("/media/ghosts/gcd.png"),
            boardWidth, boardHeight, true, false);

        ClydeImageLeft = new Image(
            getClass().getResourceAsStream("/media/ghosts/gcl.png"),
            boardWidth, boardHeight, true, false);

        ClydeImageRight = new Image(
            getClass().getResourceAsStream("/media/ghosts/gcr.png"),
            boardWidth, boardHeight, true, false);

        PinkyImageUp = new Image(
            getClass().getResourceAsStream("/media/ghosts/gpu.png"),
            boardWidth, boardHeight, true, false);

        PinkyImageDown = new Image(
            getClass().getResourceAsStream("/media/ghosts/gpd.png"),
            boardWidth, boardHeight, true, false);

        PinkyImageLeft = new Image(
            getClass().getResourceAsStream("/media/ghosts/gpl.png"),
            boardWidth, boardHeight, true, false);

        PinkyImageRight = new Image(
            getClass().getResourceAsStream("/media/ghosts/gpr.png"),
            boardWidth, boardHeight, true, false);
        
        GhostScared1 = new Image(
            getClass().getResourceAsStream("/media/ghosts/gs1.png"),
            boardWidth, boardHeight, true, false);
            
        GhostScared2 = new Image(
            getClass().getResourceAsStream("/media/ghosts/gs2.png"),
            boardWidth, boardHeight, true, false);

        GhostDead = new Image(
            getClass().getResourceAsStream("/media/ghosts/ge.png"),
            boardWidth, boardHeight, true, false);

        PacManUp = new Image(
            getClass().getResourceAsStream("/media/pu1.png"),
            boardWidth, boardHeight, true, false);
        
        PacManUp2 = new Image(
            getClass().getResourceAsStream("/media/pu2.png"),
            boardWidth, boardHeight, true, false);

        PacManDown = new Image(
            getClass().getResourceAsStream("/media/pd1.png"),
            boardWidth, boardHeight, true, false);

        PacMandDown2 = new Image(
            getClass().getResourceAsStream("/media/pd2.png"),
            boardWidth, boardHeight, true, false);

        PacManLeft = new Image(
            getClass().getResourceAsStream("/media/pl1.png"),
            boardWidth, boardHeight, true, false);

        PacManLeft2 = new Image(
            getClass().getResourceAsStream("/media/pl2.png"),
            boardWidth, boardHeight, true, false);

        PacManRight = new Image(
            getClass().getResourceAsStream("/media/pr1.png"),
            boardWidth, boardHeight, true, false);

        PacManRight2 = new Image(
            getClass().getResourceAsStream("/media/pr2.png"),
            boardWidth, boardHeight, true, false);

        PacManClose = new Image(
            getClass().getResourceAsStream("/media/pb.png"),
            boardWidth, boardHeight, true, false);

        PelletImage = new Image(
            getClass().getResourceAsStream("/media/pellet.png"),
            boardWidth, boardHeight, true, false);

        FruitImage = new Image(
            getClass().getResourceAsStream("/media/fruit.png"),
            boardWidth, boardHeight, true, false);

        GatesImage = new Image(
            getClass().getResourceAsStream("/media/gates.png"),
            boardWidth, boardHeight, true, false);

        EmptyImage = new Image(
            getClass().getResourceAsStream("/media/EMPTY.png"),
            boardWidth, boardHeight, true, false);

    

        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        gates = new HashSet<Block>();
        empties = new HashSet<Block>();
        fruits = new HashSet<Block>();
        
        for (double r = 0; r < mazemodel.getRows(); r++) {
            for (double c = 0; c < mazemodel.getCols(); c++) {
                double x = c * tileSize;
                double y = r * tileSize;
                switch (mazemodel.getCell(r, c)) {
                    case WALL         -> { walls.add(new Block(WallImage, x, y, tileSize, tileSize)); }
                    case DOT          -> { foods.add(new Block(PelletImage, x, y, pelletPixelSize, pelletPixelSize, CellType.DOT)); }
                    case POWER_PELLET -> { foods.add(new Block(PelletImage, x, y, tileSize, tileSize, CellType.POWER_PELLET));}
                    case GATE         -> { gates.add(new Block(GatesImage, x, y, tileSize, tileSize)); }
                    case EMPTY        -> { empties.add(new Block(EmptyImage, x, y, tileSize, tileSize));}
                    case FRUIT        -> { fruits.add( new Block(FruitImage,x, y, tileSize, tileSize));}
                    default           -> {}
                }
            }
        }

        drawStaticLevel(gc);
        }


    public Scene getScene() {
        return gameScene;
    }

    double z = (tileSize - pelletPixelSize)/2; 
    int ppSize = pelletPixelSize*4;
    double pp = (tileSize - ppSize)/2;

    //All elements are drawn to the maze using the previously created blocks, inside the hashsets.
    //pacman and ghosts are also drawn.
    public void drawStaticLevel(GraphicsContext gc){
        gc.clearRect(0, 0, boardWidth, boardHeight);
            for (Block wall : walls) {
                gc.drawImage(wall.image, wall.startX, wall.startY, wall.width, wall.height);
            }
            
            for (Block food : foods) {
                if (food.type == CellType.DOT) {
                    gc.drawImage(food.image, food.startX+z, food.startY+z, food.width, food.height);
                }

                else if (food.type == CellType.POWER_PELLET) {
                    gc.drawImage(food.image, food.startX+pp, food.startY+pp, ppSize, ppSize);
                }
            }

            for (Block empty : empties) {
                gc.drawImage(empty.image, empty.startX, empty.startY, empty.width, empty.height);   
            }

            for (Block fruit: fruits) {
                gc.drawImage(fruit.image,fruit.startX, fruit.startY, fruit.width, fruit.height);
            }

            for (Block gate : gates) {
                gc.drawImage(gate.image, gate.startX, gate.startY, gate.width, gate.height);
            }

            if (model.getGameState() == GameStates.POWERUP) {
                long now = System.nanoTime();
                if (now - lastScaredFrameSwitch > Scared_frame_Delay) {
                    scaredFrame = (scaredFrame +1) % 2; 
                    lastScaredFrameSwitch = now;
                }
                switch (scaredFrame) {
                    case 0 -> scaredImage = GhostScared1;
                    case 1 -> scaredImage = GhostScared2;
                    default -> scaredImage = GhostScared1;
                }
                gc.drawImage(blinky.getIsAlive() ? scaredImage : GhostDead, blinky.getCol()*tileSize, blinky.getRow()*tileSize, tileSize, tileSize);
                gc.drawImage(inky.getIsAlive() ? scaredImage : GhostDead, inky.getCol()*tileSize, inky.getRow()*tileSize, tileSize, tileSize);
                gc.drawImage(pinky.getIsAlive() ? scaredImage : GhostDead, pinky.getCol()*tileSize, pinky.getRow()*tileSize, tileSize, tileSize);
                gc.drawImage(clyde.getIsAlive() ? scaredImage : GhostDead, clyde.getCol()*tileSize, clyde.getRow()*tileSize, tileSize, tileSize);
                
            }

            
            else if (maze.getpelletsleft()< 30) {
                gc.drawImage(AngryBlinky, blinky.getCol()*tileSize, blinky.getRow()*tileSize, tileSize, tileSize);

                switch (inky.getDirection()) {
                    case UP -> gc.drawImage(InkyImageUp, inky.getCol()*tileSize, inky.getRow()*tileSize, tileSize, tileSize);
                    case DOWN -> gc.drawImage(InkyImageDown, inky.getCol()*tileSize, inky.getRow()*tileSize, tileSize, tileSize);
                    case LEFT -> gc.drawImage(InkyImageLeft, inky.getCol()*tileSize, inky.getRow()*tileSize, tileSize, tileSize);
                    case RIGHT -> gc.drawImage(InkyImageRight, inky.getCol()*tileSize, inky.getRow()*tileSize, tileSize, tileSize);
            }
                switch (pinky.getDirection()) {
                    case UP -> gc.drawImage(PinkyImageUp, pinky.getCol()*tileSize, pinky.getRow()*tileSize, tileSize, tileSize);
                    case DOWN -> gc.drawImage(PinkyImageDown, pinky.getCol()*tileSize, pinky.getRow()*tileSize, tileSize, tileSize);
                    case LEFT -> gc.drawImage(PinkyImageLeft, pinky.getCol()*tileSize, pinky.getRow()*tileSize, tileSize, tileSize);
                    case RIGHT -> gc.drawImage(PinkyImageRight, pinky.getCol()*tileSize, pinky.getRow()*tileSize, tileSize, tileSize);
            }
                switch (clyde.getDirection()) {
                    case UP -> gc.drawImage(ClydeImageUp, clyde.getCol()*tileSize, clyde.getRow()*tileSize, tileSize, tileSize);
                    case DOWN -> gc.drawImage(ClydeImageDown, clyde.getCol()*tileSize, clyde.getRow()*tileSize, tileSize, tileSize);
                    case LEFT -> gc.drawImage(ClydeImageLeft, clyde.getCol()*tileSize, clyde.getRow()*tileSize, tileSize, tileSize);
                    case RIGHT -> gc.drawImage(ClydeImageRight, clyde.getCol()*tileSize, clyde.getRow()*tileSize, tileSize, tileSize);
                }
            }
            else {
                if (!blinky.getIsAlive()) {
                    gc.drawImage(GhostDead, blinky.getCol()*tileSize, blinky.getRow()*tileSize,tileSize,tileSize);
                    }
                else {
                switch (blinky.getDirection()) {
                    case UP -> gc.drawImage(BlinkyImageUp, blinky.getCol()*tileSize, blinky.getRow()*tileSize, tileSize, tileSize);
                    case DOWN -> gc.drawImage(BlinkyImageDown, blinky.getCol()*tileSize, blinky.getRow()*tileSize, tileSize, tileSize);
                    case LEFT -> gc.drawImage(BlinkyImageLeft, blinky.getCol()*tileSize, blinky.getRow()*tileSize, tileSize, tileSize);
                    case RIGHT -> gc.drawImage(BlinkyImageRight, blinky.getCol()*tileSize, blinky.getRow()*tileSize, tileSize, tileSize);
                    }
                }

                if (!inky.getIsAlive()) {
                    gc.drawImage(GhostDead, inky.getCol()*tileSize, inky.getRow()*tileSize,tileSize,tileSize);
                }
                else {
                switch (inky.getDirection()) {
                    case UP -> gc.drawImage(InkyImageUp, inky.getCol()*tileSize, inky.getRow()*tileSize, tileSize, tileSize);
                    case DOWN -> gc.drawImage(InkyImageDown, inky.getCol()*tileSize, inky.getRow()*tileSize, tileSize, tileSize);
                    case LEFT -> gc.drawImage(InkyImageLeft, inky.getCol()*tileSize, inky.getRow()*tileSize, tileSize, tileSize);
                    case RIGHT -> gc.drawImage(InkyImageRight, inky.getCol()*tileSize, inky.getRow()*tileSize, tileSize, tileSize);
                }

                }

                if (!pinky.getIsAlive()) {
                    gc.drawImage(GhostDead, pinky.getCol()*tileSize, pinky.getRow()*tileSize,tileSize,tileSize);
                }
                else {
                    switch (pinky.getDirection()) {
                        case UP -> gc.drawImage(PinkyImageUp, pinky.getCol()*tileSize, pinky.getRow()*tileSize, tileSize, tileSize);
                        case DOWN -> gc.drawImage(PinkyImageDown, pinky.getCol()*tileSize, pinky.getRow()*tileSize, tileSize, tileSize);
                        case LEFT -> gc.drawImage(PinkyImageLeft, pinky.getCol()*tileSize, pinky.getRow()*tileSize, tileSize, tileSize);
                        case RIGHT -> gc.drawImage(PinkyImageRight, pinky.getCol()*tileSize, pinky.getRow()*tileSize, tileSize, tileSize);
                }
                }

                if (!clyde.getIsAlive()) {
                    gc.drawImage(GhostDead, clyde.getCol()*tileSize, clyde.getRow()*tileSize,tileSize,tileSize);
                }
                else {
                    switch (clyde.getDirection()) {
                        case UP -> gc.drawImage(ClydeImageUp, clyde.getCol()*tileSize, clyde.getRow()*tileSize, tileSize, tileSize);
                        case DOWN -> gc.drawImage(ClydeImageDown, clyde.getCol()*tileSize, clyde.getRow()*tileSize, tileSize, tileSize);
                        case LEFT -> gc.drawImage(ClydeImageLeft, clyde.getCol()*tileSize, clyde.getRow()*tileSize, tileSize, tileSize);
                        case RIGHT -> gc.drawImage(ClydeImageRight, clyde.getCol()*tileSize, clyde.getRow()*tileSize, tileSize, tileSize);
                }
                }
            }

            gc.setFill(Color.WHITE);
            Font usedFont = Font.font("Press Start 2P", FontWeight.BOLD, 16);
            gc.setFont(usedFont);
            gc.fillText("YOUR SCORE: " + model.getScore(), 5, boardHeight-40);

            for (int i = 0; i < model.getLives() - 1; i++) {
                gc.drawImage(PacManLeft, i * tileSize * 0.75, 23 * tileSize, tileSize / 2, tileSize / 2);
            }

            if (pacMan.getDirection() == Directions.RIGHT) {
                long now = System.nanoTime();

                if (now - lastPacManFrame > pacMan_frame_Delay) {
                    pacManFrame = (pacManFrame +1) % 3; 
                    lastPacManFrame = now;
                }                 

                switch (pacManFrame) {
                    case 0 -> pacManImage = PacManRight;
                    case 1 -> pacManImage = PacManRight2;
                    case 2 -> pacManImage = PacManClose;
                    default -> pacManImage = PacManRight;
                }

                gc.drawImage(pacManImage, pacMan.getCol()*tileSize, pacMan.getRow()*tileSize, tileSize, tileSize);
            }
            
            else if (pacMan.getDirection() == Directions.LEFT) {
                long now = System.nanoTime();

                if (now - lastPacManFrame > pacMan_frame_Delay) {
                    pacManFrame = (pacManFrame +1) % 3; 
                    lastPacManFrame = now;
                }

                switch (pacManFrame) {
                    case 0 -> pacManImage = PacManLeft;
                    case 1 -> pacManImage = PacManLeft2;
                    case 2 -> pacManImage = PacManClose;
                    default -> pacManImage = PacManLeft;
                }

                gc.drawImage(pacManImage, pacMan.getCol()*tileSize, pacMan.getRow()*tileSize, tileSize, tileSize);

                }
            
            else if (pacMan.getDirection() == Directions.UP) {
                long now = System.nanoTime();

                if (now - lastPacManFrame > pacMan_frame_Delay) {
                    pacManFrame = (pacManFrame +1) % 3; 
                    lastPacManFrame = now;
                }

                switch (pacManFrame) {
                    case 0 -> pacManImage = PacManUp;
                    case 1 -> pacManImage = PacManUp2;
                    case 2 -> pacManImage = PacManClose;
                    default -> pacManImage = PacManUp;
                }

                gc.drawImage(pacManImage, pacMan.getCol()*tileSize, pacMan.getRow()*tileSize, tileSize, tileSize);

                }

            else if (pacMan.getDirection() == Directions.DOWN) {
                long now = System.nanoTime();

                if (now - lastPacManFrame > pacMan_frame_Delay) {
                    pacManFrame = (pacManFrame +1) % 3; 
                    lastPacManFrame = now;
                }

                switch (pacManFrame) {
                    case 0 -> pacManImage = PacManDown;
                    case 1 -> pacManImage = PacMandDown2;
                    case 2 -> pacManImage = PacManClose;
                    default -> pacManImage = PacManDown;
                }
                gc.drawImage(pacManImage, pacMan.getCol()*tileSize, pacMan.getRow()*tileSize, tileSize, tileSize);
                }
        }

    @Override
    public void onGameStateChange() {
        throw new UnsupportedOperationException("Unimplemented method 'onGameStateChange'");
    }


    @Override
    public void update(GameModel gameModel) {
        //removes pellets, when pacman "eats" them.
        foods.remove(new Block(null, pacMan.getCol() * tileSize, pacMan.getRow() * tileSize, tileSize, tileSize));
        fruits.clear();
        //Continuously looking through the maze to see, if a celltype has changed to type fruit.
        for (int r = 0; r < (int) maze.getRows(); r++) {
            for (int c = 0; c < (int) maze.getCols(); c++){
                if (maze.getCell(r, c) == CellType.FRUIT) {
                    fruits.add(new Block(FruitImage, c * tileSize, r * tileSize, tileSize, tileSize, CellType.FRUIT));
                }
            }
        }
        drawStaticLevel(gc);
    }


    public void clear() {
        gc.clearRect(0,0,boardWidth, boardHeight);
    }


    
}