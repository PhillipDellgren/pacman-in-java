package dk.sdu.imada.oop26.model.Entities;

import dk.sdu.imada.oop26.model.GameModel.Directions;

public abstract class Entity implements EntityInterface {
    private double row;
    private double col;
    private Directions direction;
    protected final double startCol;
    protected final double startRow;
    private double velocity;

    public Entity(double row, double col, Directions direction, double velocity){
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.startCol = col;
        this.startRow = row;
        this.velocity = velocity;
    }
    
    public double getRow() {
        return row;
    }

    public double getCol(){
        return col;
    }

    public Directions getDirection(){
        return direction;
    }

    public void setDirection(Directions direction){
        this.direction = direction;
    }

    public void setRow(double row) {
          this.row = row;
    }

    public void setCol(double col) {
        this.col = col;
    }

   @Override
    public void respawn() {
        this.row = startRow;
        this.col = startCol; 
        this.direction = Directions.LEFT; 
    }

    public double getVelocity(){
        return velocity;
    }

   public void setVelocity(double velocity) {
        this.velocity = velocity;  
    }

}