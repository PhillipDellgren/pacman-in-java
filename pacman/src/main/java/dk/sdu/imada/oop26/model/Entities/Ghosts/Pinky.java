package dk.sdu.imada.oop26.model.Entities.Ghosts;
import java.awt.Point;

import dk.sdu.imada.oop26.model.Entities.Ghost;
import dk.sdu.imada.oop26.model.GameModel;
import dk.sdu.imada.oop26.model.GameModel.Directions;

public class Pinky extends Ghost {
    private int rowTarget;
    private int colTarget;

    public Pinky(double row, double col, Directions direction, double velocity, Point scatterPoint, String path) {
		super(row, col, direction, velocity, scatterPoint, path);
    }
    
    @Override
    public void setTarget(Point target, Directions heading, GameModel.GameStates gameState) {
        rowTarget = target.x;
        colTarget = target.y;

        if (gameState == GameModel.GameStates.NORMAL) {
            switch (heading) {
                case UP    -> rowTarget = target.x - 2; // Targets two tiles ahead of pacman, mostly in order to cut off at turning points.
                case DOWN  -> rowTarget = target.x + 2;
                case LEFT  -> colTarget = target.y - 2;
                case RIGHT -> colTarget = target.y + 2;
            }

            int midRow = (target.x + rowTarget) / 2; // Checks the tile between target and pacman, to prevent targeting the other side of a wall.
            int midCol = (target.y + colTarget) / 2;

            if (rowTarget >= 0 && rowTarget < maze.getRows() &&
                colTarget >= 0 && colTarget < maze.getCols() &&
                maze.isWalk(target.x, target.y, heading) &&
                maze.isWalk(midRow, midCol, heading)) {
                this.target = new Point(rowTarget, colTarget);
            } else {
                this.target = new Point(target.x, target.y);
            }
        }
        else {
            this.target = target;
        }
    }
}