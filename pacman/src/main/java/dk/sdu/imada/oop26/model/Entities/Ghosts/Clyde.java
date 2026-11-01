package dk.sdu.imada.oop26.model.Entities.Ghosts;
import java.awt.Point;

import dk.sdu.imada.oop26.model.Entities.Ghost;
import dk.sdu.imada.oop26.model.GameModel;
import dk.sdu.imada.oop26.model.GameModel.Directions;

public class Clyde extends Ghost {

    public Clyde(double row, double col, Directions direction, double velocity, Point scatterPoint, String path) {
		super(row, col, direction, velocity, scatterPoint, path);
    }
    
    @Override
    public void setTarget(Point target, Directions heading, GameModel.GameStates gameState) {
        if (gameState == GameModel.GameStates.NORMAL) {
            if (Math.hypot( getRow() - target.x, getCol() - target.y) > 8) { // Hypothenus calculation for distance from Clyde to Pacman - if clyde is within 8 tiles of PacMan, his target is set to his scatterPoint (Bottom right corner)
                this.target = new Point(target.x, target.y); // Target is pacman, if outside 8 tiles.
            }
            else {
                this.target = new Point(19, 26); // Bottom-right corner
            }
        }
        else {
            this.target = target;
        }
    }
}