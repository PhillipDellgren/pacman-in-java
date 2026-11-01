package dk.sdu.imada.oop26.model.Entities.Ghosts;
import java.awt.Point;

import dk.sdu.imada.oop26.model.Entities.Ghost;
import dk.sdu.imada.oop26.model.GameModel;
import dk.sdu.imada.oop26.model.GameModel.Directions;

public class Inky extends Ghost {
    private int rowTarget;
    private int colTarget;

    public Inky(double row, double col, Directions direction, double velocity, Point scatterPoint, String path) {
		super(row, col, direction, velocity, scatterPoint, path);
    }


     @Override
    public void setTarget(Point target, Directions heading, GameModel.GameStates gameState) { // Same basic targeting as Pinky, however has an extended target of 8 tiles in front, in order to promote "flanking" on longer straights.
        rowTarget = target.x;
        colTarget = target.y;

        if (gameState == GameModel.GameStates.NORMAL) {
            switch (heading) {
                case UP    -> rowTarget = target.x - 8;
                case DOWN  -> rowTarget = target.x + 8;
                case LEFT  -> colTarget = target.y - 8;
                case RIGHT -> colTarget = target.y + 8;
            }
            // Intended to block paths, therefore there is no check for walls between pacman and the target location.
            if (rowTarget >= 0 && rowTarget < maze.getRows() &&
                colTarget >= 0 && colTarget < maze.getCols() &&
                maze.isWalk(target.x, target.y, heading)) {
                this.target = new Point(rowTarget, colTarget);
            }
            else {
                this.target = new Point(target.x, target.y);
            }
        }
        else {
            this.target = target;
        }
    }
}