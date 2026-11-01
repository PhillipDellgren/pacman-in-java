package dk.sdu.imada.oop26.model.Entities;

import dk.sdu.imada.oop26.model.GameModel.Directions;

public class PacMan extends Entity {

    private Directions pendingDirection;


    public PacMan(double row, double col, Directions direction, double velocity) {
        super(row, col, direction, velocity);
        this.pendingDirection = direction; // Default direction for pacMan move
    }

    public Directions getPendingDirection() {
        return pendingDirection;
    }

    public void setPendingDirection(Directions direction){
        this.pendingDirection = direction;
    }
    
}
