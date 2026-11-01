package dk.sdu.imada.oop26.model.Entities;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import dk.sdu.imada.oop26.model.GameModel;
import dk.sdu.imada.oop26.model.GameModel.Directions;
import dk.sdu.imada.oop26.model.maze.MazeModel;

public class Ghost extends Entity{
    protected MazeModel maze;
    protected Point target;
    private final Point scatterPoint;
    private boolean isAlive = true; 
    private double row; 
    private double col; 

    public Ghost(double row, double col, Directions direction, double velocity, Point scatterPoint, String path) {
		super(row, col, direction, velocity);
        this.maze = new MazeModel();
        try {
            maze.loadFromFile(path); // Loads an instance of the maze exclusively for pathfinding. As this never has to be updated an updating version is not required.
        } catch (IOException e) {
            e.printStackTrace();
        } 
        this.scatterPoint = scatterPoint; 
	}

    public void update (MazeModel Maze){
    }

    public Directions getNextMove(Point startNode, Point nodeToBeFound, Directions previousDirection) { // Breadth-First search algorithm, for calculating next move for each ghost. Only first is stored, as a new path may be discovered on each update.
        Map<Point, Point> parentNodes = new HashMap<>(); 
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visitedNodes = new HashSet<>();
        List<Point> shortestPath = new ArrayList<>();
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Point nextNode = queue.poll();

            if (nextNode.equals(nodeToBeFound)) { // Check if node has been found, if it has, it is added as the last element in the shortestPath.
                Point node = nodeToBeFound;
                while (node != null) {
                    shortestPath.add(node);
                    node = parentNodes.get(node);
                }
                Collections.reverse(shortestPath); // The elements are reversed, as it is easier to always take the argument at index 1, which would be the next move, rather than finding the second-to-last element.
                break;
            }

            if (visitedNodes.contains(nextNode)) continue;
            visitedNodes.add(nextNode);

            for (Directions direction : Directions.values()) {
                Point neighbor = null;
                if (maze.getCell(nextNode.x, nextNode.y) == MazeModel.CellType.TUNNEL_L && direction == Directions.LEFT) {
                    neighbor = new Point(nextNode.x, (int) maze.getCols() -2);
                }
                else if (maze.getCell(nextNode.x, nextNode.y) == MazeModel.CellType.TUNNEL_R && direction == Directions.RIGHT) {
                    neighbor = new Point(nextNode.x, 1);
                }
                else {
                    if (maze.isWalk(nextNode.x, nextNode.y, direction)) { // For the current node, check all four sides, to check whether a ghost can walk onto the tile.
                        neighbor = switch (direction) {
                            case UP    -> new Point(nextNode.x - 1, nextNode.y);
                            case DOWN  -> new Point(nextNode.x + 1, nextNode.y);
                            case LEFT  -> new Point(nextNode.x, nextNode.y - 1);
                            case RIGHT -> new Point(nextNode.x, nextNode.y + 1);
                        };
                    }
                }
                if (neighbor != null && !visitedNodes.contains(neighbor)) {
                    queue.add(neighbor); // The node is added as a neighbor to the original node, if it is walkable by a ghost.
                    parentNodes.put(neighbor, nextNode);
                }
            }
        }
        if (shortestPath.size() < 2) return previousDirection; // In case no path was found, or pacman is on the next tile, fallback to last direction of movement (continue same direction).

        Point nextStep = shortestPath.get(1);
        int offsetX = nextStep.x - startNode.x;
        int offsetY = nextStep.y - startNode.y;

        // Tunnel offset
        if (offsetX == 0 && offsetY > 1)  return Directions.LEFT;  // at TUNNEL_L, going left into tunnel
        if (offsetX == 0 && offsetY < -1) return Directions.RIGHT; // at TUNNEL_R, going right into tunnel

        if (offsetX > 0) return Directions.DOWN;
        if (offsetX < 0) return Directions.UP;
        if (offsetY > 0) return Directions.RIGHT;
        return Directions.LEFT;
    }


    
    public Point getTarget() {
        return target;
    }

    public void setTarget(Point target, Directions heading, GameModel.GameStates gameState){
        this.target = target; // Default fallback for all ghosts. Primary use for Blinky.
    }

    public Point getScatterPoint(){
		return scatterPoint;
	}

    public boolean getIsAlive() {
        return isAlive; 
    }

    public void setIsDead() {
        isAlive = false;
    }

    public Point getRespawnPoint() {
        Point respawnPoint = new Point((int) this.startRow, (int) this.startCol);
        return respawnPoint;
    }

    public void hasRespawned() {
        if (this.startRow == getRow() && this.startCol == getCol()) {
            isAlive = true;
        }
    }
}
