package dk.sdu.imada.oop26.model;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dk.sdu.imada.oop26.model.Entities.Entity;
import dk.sdu.imada.oop26.model.Entities.Ghost;
import dk.sdu.imada.oop26.model.Entities.Ghosts.Blinky;
import dk.sdu.imada.oop26.model.Entities.Ghosts.Clyde;
import dk.sdu.imada.oop26.model.Entities.Ghosts.Inky;
import dk.sdu.imada.oop26.model.Entities.Ghosts.Pinky;
import dk.sdu.imada.oop26.model.Entities.PacMan;
import dk.sdu.imada.oop26.model.maze.MazeModel;
import dk.sdu.imada.oop26.model.maze.MazeModel.CellType;
import javafx.event.Event;
import javafx.event.EventHandler;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;



public class GameModel implements Subject, EventHandler {
	private final MazeModel maze;
	
	public enum Correctness {TOO_SMALL, CORRECT, TOO_LARGE, UNDEF}
	public enum Directions {UP,DOWN,RIGHT,LEFT}
	public enum GameStates {NORMAL, POWERUP, IMMUNE, FINISHED}
	public enum Entities {PACMAN, INKY, BLINKY, PINKY, CLYDE}

	PacMan pacMan;

	double pacmanX;
	double pacmanY;
	long lastUpdate;

	GameStates currentGameState = GameStates.NORMAL;
	private static final int powerUpDuration = 5000;
	private long activatedAt = Long.MAX_VALUE;
	private static final int immuneDuration = 3000;

	private final double rowCount;
	private final double columnCount;
	private int pacManLives;
	private int score;

	private Ghost blinky;
	private Ghost inky; 
	private Ghost pinky; 
	private Ghost clyde;  
	private final List<Entity> entities = new ArrayList<>();
	private final double pacmanVelocity = 0.125;
	private final double ghostVelocity = 0.1;
	private long fruitTimer = 0; 
	private static final long fruiterTimer_delay = 10_000_000_000L; // 10 s.
	private int currentLevel = 1;
	private static final int Max_Level = 2; 
	private Clip pelletClip;
	//Remembers where the last pellets was eaten
	private int lastPelletRow;
	private int lastPelletCol;
	private Clip powerPelletClip;
	private Clip ghostSound;
	private Clip sendGhostHome;


	private final List<GameObserver> observers = new LinkedList<>();
	
	public GameModel(MazeModel maze, int gameLevel, int score, int lives) {
	pacManLives = lives;
	this.score = score;
    this.maze = maze;
    rowCount = maze.getRows();
    columnCount = maze.getCols();
	String levelPath = "/media/maze" + gameLevel + ".txt";
	try {
			AudioInputStream stream0 = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/media/sounds/wav/eatpellet.wav"));
			pelletClip = AudioSystem.getClip();
			pelletClip.open(stream0);

			AudioInputStream stream1 = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/media/sounds/wav/ghostscatter.wav"));
			powerPelletClip = AudioSystem.getClip();
			powerPelletClip.open(stream1);

			AudioInputStream stream2 = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/media/sounds/wav/ghostnormalmove_fixed.wav"));
			ghostSound = AudioSystem.getClip();
			ghostSound.open(stream2);

			AudioInputStream stream3 = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/media/sounds/wav/ghostreturnhome.wav"));
			sendGhostHome = AudioSystem.getClip();
			sendGhostHome.open(stream3);
	}
			
			catch (Exception e) {
			System.err.println("Could not load sound");
			}

    // find spawn position for pacman & ghosts based on the maze
		for (double r = 0; r < rowCount; r++) {
			for (double c = 0; c < columnCount; c++) {
				if (maze.getCell(r, c) == CellType.PACMAN) {
					pacMan = new PacMan(r,c,Directions.RIGHT, pacmanVelocity);
				}
				if (maze.getCell(r, c) == CellType.BLINKY) {
					blinky = new Blinky(r, c, Directions.RIGHT, ghostVelocity, new Point(2,26), levelPath);
				}
				if (maze.getCell(r, c) == CellType.CLYDE){
					clyde = new Clyde(r, c, Directions.LEFT, ghostVelocity, new Point(19,26),levelPath);
				}
				if (maze.getCell(r, c) == CellType.INKY) {
					inky = new Inky(r, c, Directions.LEFT, ghostVelocity, new Point(19,1),levelPath);					
				}
				if (maze.getCell(r, c) == CellType.PINKY) {
					pinky = new Pinky(r, c, Directions.LEFT, ghostVelocity, new Point(2,1),levelPath);					
				}
			}
		}
		
		entities.add(blinky);
		entities.add(pacMan);
		entities.add(inky);
		entities.add(pinky);
		entities.add(clyde);

	}
	
	public void removeLife(){
		pacManLives -= 1;
	}

	public int getLives(){
		return pacManLives;
	}

	public void powerup_collected(GameStates currentState){
		if (maze.consumePowerpellet(pacmanY, pacmanX)) {
			currentState = GameStates.POWERUP;
		}		
	}

	public void activateGameState(){
		if (currentGameState != GameStates.NORMAL){
		activatedAt = System.currentTimeMillis();
		}
	}

	//Activates a timer, when gamestate changes to POWERUP or IMMUNE.
	//When timer reaches the designated duration each state must be in, game state returns to NORMAL.
	public boolean isActive(){
		if (currentGameState == GameStates.POWERUP){
			long activeFor = System.currentTimeMillis() - activatedAt;
			return activeFor >= 0 && activeFor <= powerUpDuration;
		}
		else if (currentGameState == GameStates.IMMUNE){
			long activeFor = System.currentTimeMillis() - activatedAt;
			return activeFor >= 0 && activeFor <= immuneDuration;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean registerObserver(final GameObserver observer) {
		return observers.add(observer);
	}

	@Override
	public boolean deregisterObserver(final GameObserver observer) {
		return observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		observers.forEach(e -> e.update(this));
	}

	@Override
	public void handle(Event event) {
		for (GameObserver Observer : observers) {
			Observer.update(null);
		}
	}

	private void handleFruitTimer (long now) {
		if (fruitTimer == 0) {
			fruitTimer = now;
			return;
		}

		if (now - fruitTimer >= fruiterTimer_delay) {
			fruitTimer = now;
			maze.addFruit();
		}
	}

	
	public void update(long now)
	{
		
		if (!maze.hasPellets()) {
			currentGameState = GameStates.FINISHED;
			pelletClip.close();
			ghostSound.close();
			notifyObservers();
			return;
		}

		if (pacManLives == 0 ) {
			currentGameState = GameStates.FINISHED;
			notifyObservers();
			return;
		}
		
		
		handleFruitTimer(now);


		if (now - lastUpdate > 20_000_000L) 
			{	
			for (Entity entity : entities) {
				double r = entity.getRow();
				double c = entity.getCol();
				//Fix entity position if it is slightly off grid
				if (Math.abs(r - Math.round(r)) < 0.0001 ){
					entity.setRow(Math.round(r));
					r = Math.round(r);
				}
				if (Math.abs(c - Math.round(c)) < 0.0001){
					entity.setCol(Math.round(c));
					c = Math.round(c);
				}

				if (maze.getpelletsleft() < 30) {
				//If blinky is not in a perfect tile, the change of velocity will create an error
					if (Math.round(blinky.getCol()) == blinky.getCol() && Math.round(blinky.getRow()) == blinky.getRow()) {
						blinky.setVelocity(pacMan.getVelocity());
					}			
				}
				System.out.println(maze.getpelletsleft());

					if (entity instanceof Ghost ghost){
						boolean wasDead = !((Ghost)entity).getIsAlive(); //Needs to be created, since we check if the ghost is not alive. We cant just write the check inside the if, because we only go inside the if, if the ghost is alive. Contradiction.
						((Ghost)entity).hasRespawned(); // set attribute isAlive to true if respawned point has been reached. 
						
						// Set velocity up when ghosts have been eaten
						if (((Ghost)entity).getIsAlive() == false && Math.round(entity.getCol()) == entity.getCol() && Math.round(entity.getRow()) == entity.getRow()) {
							entity.setVelocity(ghostVelocity * 2);							
						}
						//Set velocity back down when ghosts have respawned
						else if (((Ghost)entity).getIsAlive() == true && Math.round(entity.getCol()) == entity.getCol() && Math.round(entity.getRow()) == entity.getRow()) {
							if (entity instanceof Blinky && maze.getpelletsleft() < 30) {
								entity.setVelocity(pacMan.getVelocity());									
							}
							else {entity.setVelocity(ghostVelocity);}
							if (wasDead && sendGhostHome.isRunning()){
								//if (((Blinky)entity).getIsAlive() && ((Inky)entity).getIsAlive() && ((Pinky)entity).getIsAlive() && ((Clyde)entity).getIsAlive()){
									sendGhostHome.stop();
								//}
							}
						}

						if (Math.round(entity.getRow()) == entity.getRow() && Math.round(entity.getCol()) == entity.getCol()) {
							Point startPoint = new Point(((int) entity.getRow()), ( (int)entity.getCol() ) );
							if (getCurrentGameState() == GameStates.NORMAL){
								if (ghostSound != null && !ghostSound.isRunning()){
									ghostSound.loop(Clip.LOOP_CONTINUOUSLY);
								}

								if (ghost instanceof Blinky || ghost instanceof Pinky){
									ghost.setTarget(new Point(((int) pacMan.getRow()), ( (int) pacMan.getCol() ) ), pacMan.getDirection(), getCurrentGameState());
								}

								if (maze.getpelletsleft() < 181){
									inky.setTarget(new Point(((int) pacMan.getRow()), ( (int) pacMan.getCol() ) ), pacMan.getDirection(), getCurrentGameState());
								}

								if (maze.getpelletsleft() < 156){
									clyde.setTarget(new Point(((int) pacMan.getRow()), ( (int) pacMan.getCol() ) ), pacMan.getDirection(), getCurrentGameState());
								}
							}

							if (getCurrentGameState() != GameStates.NORMAL){
								if (ghostSound.isRunning() && getCurrentGameState() != GameStates.IMMUNE){
									//Ensures that the ghost movement sound does not stop working when immune state is active.
									ghostSound.stop();
								}

								if (ghost instanceof Blinky || ghost instanceof Pinky){
									ghost.setTarget(new Point(ghost.getScatterPoint()), pacMan.getDirection(), getCurrentGameState());
								}

								if (maze.getpelletsleft() < 181) {
									inky.setTarget(new Point(ghost.getScatterPoint()), pacMan.getDirection(), getCurrentGameState());
								}

								if (maze.getpelletsleft() < 156){
									clyde.setTarget(new Point(ghost.getScatterPoint()), pacMan.getDirection(), getCurrentGameState());
								}


							}
							Point target = ghost.getTarget();
							if (ghost.getIsAlive()== false) {
								target = ghost.getRespawnPoint();
							}
							entity.setDirection(ghost.getNextMove(startPoint, target, ghost.getDirection()));
						}
					}
						
				//We created a "pending" direction, so pacman can turn as soon as possible, as opposed to spamming a key
				//when comming close to a corner. Pending direction is set, when key is pressed.
				if (entity instanceof PacMan && ((Math.floor(r) == r && Math.floor(c) == c && (maze.isWalk(r, c, pacMan.getPendingDirection())) && (maze.isGate(r, c, pacMan.getPendingDirection()))))){
					pacMan.setDirection(pacMan.getPendingDirection());
						move(entity);
						collision(entity);

					}
					//If a entity can move, it is moved followed by checking for collision.
					else if (canMove(entity)){
						move(entity);
						collision(entity);
					}
					//This prevents entities from moving, when row or column returns a double.
					//Essentially we move them, in their current direction, until they are in a integer position.
					else {
						if (Math.round(c) != c || Math.round(r) != r){
							move(entity);
							collision(entity);
						}
					}
					collisionTunnel(entity);
            	}	
				

			
			lastUpdate = now;
		}


		//boolean atePellet = maze.consumePellet(pacMan.getRow(), pacMan.getCol());
		if (maze.consumePellet(pacMan.getRow(), pacMan.getCol())){
			score += 10;
			lastPelletRow = (int) pacMan.getRow();
			lastPelletCol = (int) pacMan.getCol();
			if (pelletClip != null && !pelletClip.isRunning()){
				pelletClip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			}
		else if (maze.consumePowerpellet(pacMan.getRow(), pacMan.getCol())){
			score += 50;
			currentGameState = GameStates.POWERUP;
			activateGameState();
			if (powerPelletClip != null) {
				powerPelletClip.setFramePosition(0);
				powerPelletClip.start();
				}
			}
		else if (maze.consumeFruit(pacMan.getRow(), pacMan.getCol())) {
			score += 100; 
		}
		
		if (currentGameState != GameStates.NORMAL && !isActive()) {
        currentGameState = GameStates.NORMAL;
    	}

		if (!(maze.consumePellet(pacMan.getRow(), pacMan.getCol())) && pelletClip != null && pelletClip.isRunning()){
			if (Math.round(pacMan.getRow()) == pacMan.getRow() && Math.round(pacMan.getCol()) == pacMan.getCol()){
					if ((int)pacMan.getRow() != lastPelletRow || (int)pacMan.getCol() != lastPelletCol){
						pelletClip.stop();
					}
			}
		}
		
		notifyObservers();
		
	}

	//Method for checking, wether or not an entity can move.
	//Meaning, entities only move when row and column are a integer, and isWalk returns true.
	public boolean canMove(Entity entity){
		// double r = entity.getRow();
		// double c = entity.getCol();

		// if (Math.round(r) == r && Math.round(c) == c && (maze.isWalk(r, c, entity.getDirection()))){
		// 	Directions entityDirection = (entity).getDirection();
        // 	int entityRow = (int) (entity).getRow();
       	// 	int entityCol = (int) (entity).getCol();
    	// 	switch (entityDirection) {
        //     	case UP -> {
        //             return maze.isWalk(entityRow, entityCol, entityDirection);
        //                 }
        //     	case DOWN -> {
        //             return maze.isWalk(entityRow, entityCol, entityDirection);
        //                 }
        //     	case LEFT -> {
        //             return maze.isWalk(entityRow, entityCol, entityDirection);
        //                 }
        //     	case RIGHT -> {
        //             return maze.isWalk(entityRow, entityCol, entityDirection);
        //                 }
		// 	}
        // }
        // return false;

		// RE-WRITTEN, HAS SAME ARGUMENT IN ALL SWITCH CASES, NO CHECK FOR BOUNDARIES (NEEDED FOR GHOST TUNNEL ISSUES :) )
		double r = entity.getRow();
		double c = entity.getCol();

		// if (r < 0 || r > maze.getRows() || c < 0 || c > maze.getCols()) { // If out of bounds, is in tunnel
		// 	return true;
		// }

		if (Math.round(r) == r && Math.round(c) == c){ // Check if going out of bounds, towards tunnel - skip isWalk check if in tunnel
			if (maze.getCell(r, c) == MazeModel.CellType.TUNNEL_L && entity.getDirection() == Directions.LEFT)  return true;
			if (maze.getCell(r, c) == MazeModel.CellType.TUNNEL_R && entity.getDirection() == Directions.RIGHT) return true;
			return maze.isWalk(entity.getRow(), entity.getCol(), entity.getDirection());
		}

		return false;
    }

	public void move(Entity entity){
			switch (entity.getDirection()) {
            case UP    -> entity.setRow(entity.getRow()-entity.getVelocity());
            case DOWN  -> entity.setRow(entity.getRow()+entity.getVelocity());
            case LEFT  -> entity.setCol(entity.getCol()-entity.getVelocity());
            case RIGHT -> entity.setCol(entity.getCol()+entity.getVelocity());
        }
    }

	public Entity getEntity(Entities entity){
            return switch (entity) {
                case INKY   -> inky;
                case BLINKY -> blinky;
                case PINKY  -> pinky;
                case CLYDE  -> clyde;
                default     -> pacMan;
            };
	}

	public int getScore() {
		return score;
	}

	//Collision check for pacman and ghosts.
	//If collision happens, gamestate => Immune
	//If else in powerup => ghosts respawn
	public void collision(Entity entity){
		if (currentGameState == GameStates.NORMAL){
				if (entity instanceof Ghost){
					if (Math.hypot( entity.getRow() - pacMan.getRow(), entity.getCol() - pacMan.getCol()) < 0.8){
						pacManLives -= 1;
						currentGameState = GameStates.IMMUNE;
						activateGameState();
					}
				}
		}
		else if (currentGameState == GameStates.POWERUP){
				if (entity instanceof Ghost){
					if (Math.hypot(entity.getRow() - pacMan.getRow(), entity.getCol() - pacMan.getCol()) < 0.8){
						if (((Ghost)entity).getIsAlive()) {
							score += 200;
							if (sendGhostHome != null){
								sendGhostHome.setFramePosition(0);
								sendGhostHome.start();
							}
						((Ghost)entity).setIsDead();
						//entity.respawn();
						}
						
				}
			}
		}
	}

	//If any entity collides with a tunnel, they will be "teleported".
	public void collisionTunnel(Entity entity){

        // if (maze.getCell(Math.ceil(entity.getRow()), Math.ceil(entity.getCol())) == CellType.TUNNEL_L){
        //     entity.setRow(maze.getRow(CellType.TUNNEL_R));
        //     entity.setCol(maze.getCol(CellType.TUNNEL_R)-1);
        // }
        // else if (maze.getCell(Math.floor(entity.getRow()), Math.floor(entity.getCol())) == CellType.TUNNEL_R){
        //     entity.setRow(maze.getRow(CellType.TUNNEL_L));
        //     entity.setCol(maze.getCol(CellType.TUNNEL_L)+1);
        // }

		int rowCeil  = (int) Math.min(Math.ceil(entity.getRow()),  maze.getRows() - 1);
		int colCeil  = (int) Math.min(Math.ceil(entity.getCol()),  maze.getCols() - 1);
		int rowFloor = (int) Math.max(Math.floor(entity.getRow()), 0);
		int colFloor = (int) Math.max(Math.floor(entity.getCol()), 0);

		if (maze.getCell(rowCeil, colCeil) == CellType.TUNNEL_L) {
			entity.setRow(maze.getRow(CellType.TUNNEL_R));
			entity.setCol(maze.getCol(CellType.TUNNEL_R) - 1);
		}
		else if (maze.getCell(rowFloor, colFloor) == CellType.TUNNEL_R) {
			entity.setRow(maze.getRow(CellType.TUNNEL_L));
			entity.setCol(maze.getCol(CellType.TUNNEL_L) + 1);
		}
    }

	public GameStates getCurrentGameState() {
		return currentGameState;
	}

	public GameStates getGameState() {
		return currentGameState;
	}

}

