package dk.sdu.imada.oop26.model.Entities.Ghosts;
import java.awt.Point;

import dk.sdu.imada.oop26.model.Entities.Ghost;
import dk.sdu.imada.oop26.model.GameModel.Directions;

public class Blinky extends Ghost {

    public Blinky(double row, double col, Directions direction, double velocity, Point scatterPoint, String path) {
		super(row, col, direction, velocity, scatterPoint, path);
	}

	// No implementation of targeting - default targeting from ghost class

}
