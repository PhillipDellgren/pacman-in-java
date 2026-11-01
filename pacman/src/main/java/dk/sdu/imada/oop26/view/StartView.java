package dk.sdu.imada.oop26.view;

import java.util.LinkedList;
import java.util.List;

import dk.sdu.imada.oop26.controller.Main;
import dk.sdu.imada.oop26.model.GameObserver;
import dk.sdu.imada.oop26.model.ViewObserver;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import dk.sdu.imada.oop26.model.ViewSubject;
public class StartView implements ViewSubject{

    private final Scene primaryScene;
    private final double tileSize;
    private final List<ViewObserver> observers = new LinkedList<>();

    public StartView(int canvasCols, int canvasRows){
		// create view
        this.tileSize = GameView.tileSize;
        Group root = new Group();
        primaryScene = new Scene(root, canvasCols*tileSize, canvasRows*tileSize);
        primaryScene.setFill(Color.BLACK);
        Canvas canvas = new Canvas(canvasCols*tileSize, canvasRows*tileSize);
        root.getChildren().add(canvas);

        // adds the logo in the center
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image logo = new Image(
            "https://cdn.freebiesupply.com/logos/large/2x/pac-man-logo-png-transparent.png",
         canvasCols * tileSize *0.8, canvasRows * tileSize*0.8, true, false);

        double canvasWidth = canvas.getWidth();
        double imageWidth = logo.getWidth();
        double x = (canvasWidth - imageWidth) / 2;
        gc.drawImage(logo, x, -25);

        Font usedFont = Font.font("Press Start 2P", FontWeight.BOLD, 32);
        gc.setFont(usedFont);
        double textPosX = (canvas.getWidth() / 2);
        double textPosY = (canvas.getHeight() * 0.75);
        Text startText = new Text("PRESS TO START");

        root.getChildren().add(startText);
        startText.setFill(Color.WHITE);
        startText.setFont(usedFont);
        startText.setX(textPosX - startText.getLayoutBounds().getWidth() / 2);
        startText.setY(textPosY);
        
        // Source - https://stackoverflow.com/a/43085069
        // Posted by fabian
        // Retrieved 2026-04-18, License - CC BY-SA 3.0
        //Makes starting text "blink".
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), evt -> startText.setVisible(false)),
                                         new KeyFrame(Duration.seconds( 1), evt -> startText.setVisible(true)));
                                         
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        primaryScene.setOnKeyPressed(evt -> notifyViewObservers());
        primaryScene.setOnMouseClicked(evt -> notifyViewObservers());
    }

    public Scene getScene(){
        return primaryScene;
    }

	@Override
	public boolean registerViewObserver(final ViewObserver observer) {
		return observers.add(observer);
	}

	@Override
	public boolean deregisterViewObserver(final ViewObserver observer) {
		return observers.remove(observer);
	}

	@Override
	public void notifyViewObservers() {
		observers.forEach(e -> e.viewStart());
	}
    
}
