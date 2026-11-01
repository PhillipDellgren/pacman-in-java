package dk.sdu.imada.oop26.model.maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import dk.sdu.imada.oop26.model.GameModel.Directions;


public class MazeModel {
    	public enum CellType {
		EMPTY,
		WALL,
		DOT,
		POWER_PELLET,
		GHOST_HOUSE,
		TUNNEL,
		PACMAN,
		INKY,
		CLYDE,
		BLINKY,
		PINKY,
        GATE,
        TUNNEL_L,
        TUNNEL_R,
        FRUIT 
	}

    private final double tileSize = 32;
    private CellType[][] grid;
    private double rows;
    private double cols;
    private int pelletsLeft;
    private int emptyFields; 

    public MazeModel() {  
        this.grid = new CellType[(int) rows][(int) cols];
    }

    public double getRows() { 
        return this.rows; }
        
    public double getCols() { 
        return this.cols; }


    public CellType getCell(double row, double col) {
        return grid[(int) row][(int) col];
    }

    public int getpelletsleft () {
        return pelletsLeft;
    }

    public double getRow(CellType celltype){
        double row = 0;
        for (double i = 0; i < grid.length; i++) {
            for (double j = 0; j < grid[(int) i].length; j++) {
                if (grid[(int)i][(int)j] == celltype) {
                row = ((double) i);
                break;
                }

        }
    }
        return row;
    }

     public double getCol(CellType celltype){
        double col = 0;
        for (double i = 0; i < grid.length; i++) {
            for (double j = 0; j < grid[(int) i].length; j++) {
                if (grid[(int)i][(int)j] == celltype) {
                col = ((double) j);
                break;
            }
        }
    }
        return col;
    }

    public CellType getCol(double row, double col) {
        return grid[(int) row][(int) col];
    }

    public void setCell(double row, double col, CellType type) {
        grid[(int) row][(int) col] = type;
    }

    public boolean isWalk(double row, double col, Directions direction){
        switch (direction) { // In blocks due to return statements
            case UP -> {
                return grid[(int) row-1][(int) col] != CellType.WALL;
                    }
            case DOWN -> {
                return grid[(int) row+1][(int) col] != CellType.WALL;
                    }
            case RIGHT -> {
                return grid[(int) row][(int) col+1] != CellType.WALL;
                    }
            case LEFT -> {
                return grid[(int) row][(int) col-1] != CellType.WALL;
                    }
        }
        return grid[(int) row][(int) col] != CellType.WALL;
    }

    public int getEmptyFields(){
        return emptyFields;
    }

    public boolean ghostHouseArea (int row, int col) {
        return row >= 9 && row <= 13 && col >= 10 && col <= 17;
    } 

    public void addFruit () {
        if (emptyFields <= 0){
            return;
        }

        Random random = new Random();
        int randomNumber = random.nextInt(emptyFields-14); // Deducted amount of empty field inside ghostHouseArea
        int benchNumber = 0;
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j< grid[i].length; j++){
                if (grid[i][j] == CellType.EMPTY && !ghostHouseArea(i, j)){
                    if (benchNumber == randomNumber) {
                        grid[i][j] = CellType.FRUIT;
                        emptyFields--;
                    }
                    benchNumber++;
                }
            }
        }
    }

    public boolean isGate(double row, double col, Directions direction){
        switch (direction) {
            case UP -> {
                return grid[(int) row-1][(int) col] != CellType.GATE;
                    }
            case DOWN -> {
                return grid[(int) row+1][(int) col] != CellType.GATE;
                    }
            case RIGHT -> {
                return grid[(int) row][(int) col+1] != CellType.GATE;
                    }
            case LEFT -> {
                return grid[(int) row][(int) col-1] != CellType.GATE;
                    }
        }
        return grid[(int) row][(int) col] != CellType.GATE;
    }

    public boolean consumePellet(double row, double col){
    if (Math.floor(row) == row && Math.floor(col) == col){
        if (grid[(int) row][(int) col] == CellType.DOT){
            grid[(int) row][(int) col] = CellType.EMPTY;
            pelletsLeft -= 1;
            emptyFields += 1; 
            return true;
        }
    }
        return false;
    }

    public boolean consumeFruit(double row, double col){
    if (Math.floor(row) == row && Math.floor(col) == col){
        if (grid[(int) row][(int) col] == CellType.FRUIT) {
            grid [(int)row][(int)col] = CellType.EMPTY;
            emptyFields +=1; 
            return true; 
        }
    }
        return false;
    }

    public boolean consumePowerpellet (double row, double col){
    if (Math.floor(row) == row && Math.floor(col) == col){
        if (grid[(int) row][(int) col] == CellType.POWER_PELLET){
            grid[(int) row][(int) col] = CellType.EMPTY;
            emptyFields += 1;
            return true;
        }
    }
        return false;
    }

    //Reads from the maze.txt file and converts each symbol into a CellType and assigns them on the grid.
    public void loadFromFile(String path) throws IOException {
        pelletsLeft = 0; 
        emptyFields = 0;

        var stream = getClass().getResourceAsStream(path);
        List<String> lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();

        this.rows = lines.size();
        this.cols = lines.get(0).length();
        this.grid = new CellType[(int) rows][(int) cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                switch (lines.get(r).charAt(c)) {
                    case '#': 
                        grid[r][c]=CellType.WALL;
                        break;
                    case '.': 
                        grid[r][c]=CellType.DOT;
                        pelletsLeft +=1;
                        break;
                    case 'O': 
                        grid[r][c]=CellType.POWER_PELLET;
                        break;
                    case ' ':
                        grid[r][c]=CellType.EMPTY;
                        emptyFields += 1;
                        break;
                    case 'W': 
                        grid[r][c]=CellType.PACMAN;
                        break;
                    case 'I': 
                        grid[r][c]=CellType.INKY;
                        break;
                    case 'C': 
                        grid[r][c]=CellType.CLYDE;
                        break;
                    case 'B': 
                        grid[r][c]=CellType.BLINKY;
                        break;
                    case 'P': 
                        grid[r][c]=CellType.PINKY;
                        break;
                    case 'G': 
                        grid[r][c]=CellType.GATE;
                        break;
                    case 'L': 
                        grid[r][c]=CellType.TUNNEL_L;
                        break;
                    case 'R': 
                        grid[r][c]=CellType.TUNNEL_R;
                        break;
                    default: 
                        grid[r][c]=CellType.EMPTY;
                        emptyFields += 1;
                        break; 
                }
            }
        }
	}

    public double getTileSize() {
        return tileSize;
    }

    public boolean hasPellets() {
        for (double i = 0; i < grid.length; i++) {
            for (double j = 0; j < grid[(int) i].length; j++) {
                if (grid[(int) i][(int) j] == CellType.DOT || grid[(int) i][(int) j] == CellType.POWER_PELLET)
                    return true;
            }
        }
        return false;
    }
}



