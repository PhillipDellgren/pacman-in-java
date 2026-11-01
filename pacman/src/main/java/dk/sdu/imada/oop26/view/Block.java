package dk.sdu.imada.oop26.view;
import java.util.Objects;
import dk.sdu.imada.oop26.model.maze.MazeModel.CellType;
import javafx.scene.image.Image;

public class Block {
    double x;
    double y;
    double width;
    double height;
    Image image;

    double startX;
    double startY;
    CellType type;

    Block(Image image, double x2, double y2, double tileSize, double tileSize2){
        this.image = image;
        this.x = x2;
        this.y = y2;
        this.width = tileSize;
        this.height = tileSize2;
        this.startX = x2;
        this.startY = y2;
    }

    Block(Image image, double x2, double y2, double tileSize, double tileSize2, CellType type){
        this.image = image;
        this.x = x2;
        this.y = y2;
        this.width = tileSize;
        this.height = tileSize2;
        this.startX = x2;
        this.startY = y2;
        this.type = type;
    }

    

    @Override
    public boolean equals(Object b) {
        if (this == b)
            return true;

        if (!(b instanceof Block))
            return false;

        Block block = (Block) b;

        return x == block.x && y == block.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }
}
