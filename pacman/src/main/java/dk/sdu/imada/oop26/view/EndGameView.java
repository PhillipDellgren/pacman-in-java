package dk.sdu.imada.oop26.view;

import dk.sdu.imada.oop26.controller.Main;
import dk.sdu.imada.oop26.model.GameModel;
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

public class EndGameView {
    private Scene endGameScene;
    GraphicsContext gc;
    int canvasRows;
    int canvasCols;
    double tileSize;

    public EndGameView(GameModel gameModel, Main app, int canvasCols, int canvasRows){
        this.canvasRows = canvasRows;
        this.canvasCols = canvasCols;
        this.tileSize = GameView.tileSize;
        Group root = new Group();
        endGameScene = new Scene(root, canvasCols * tileSize, canvasRows * tileSize);
        endGameScene.setFill(Color.BLACK);

        Canvas canvas = new Canvas(canvasCols * tileSize, canvasRows * tileSize); 
        root.getChildren().add(canvas);

        // adds the logo in the center
        gc = canvas.getGraphicsContext2D();
        Image logo = new Image(
            "https://cdn.freebiesupply.com/logos/large/2x/pac-man-logo-png-transparent.png",
         canvasCols * tileSize*0.8, canvasRows * tileSize *0.8, true, false);
        
        double canvasWidth = canvas.getWidth();
        double imageWidth = logo.getWidth();
        double x = (canvasWidth - imageWidth) / 2;
        
        gc.drawImage(logo, x, -25);
        Font fontBig = Font.font("Press Start 2P", FontWeight.BOLD, 32);
        Font fontMed = Font.font("Press Start 2P", FontWeight.BOLD, 24);
        Font fontSmall = Font.font("Press Start 2P", FontWeight.BOLD, 16);
        gc.setFont(fontBig);

        double textPosX = (canvas.getWidth() / 2);
        double textPosY = (canvas.getHeight() * 0.75);
        
        Text endText = new Text("GAME ENDED!");
        Text score = new Text("Your final score: " + gameModel.getScore());
        Text restartText = new Text("Press any key to restart the game");
        root.getChildren().add(endText);
        root.getChildren().add(score);
        root.getChildren().add(restartText);
        endText.setFill(Color.WHITE);
        endText.setFont(fontBig);
        score.setFill(Color.WHITE);
        score.setFont(fontMed);
        restartText.setFill(Color.WHITE);
        restartText.setFont(fontSmall);

        endText.setX(textPosX - endText.getLayoutBounds().getWidth() / 2);
        endText.setY(textPosY-60);
        score.setX((textPosX - score.getLayoutBounds().getWidth() / 2));
        score.setY(textPosY + 45);
        restartText.setX((textPosX - restartText.getLayoutBounds().getWidth() / 2));
        restartText.setY(textPosY + 90);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), evt -> restartText.setVisible(false)),
                                         new KeyFrame(Duration.seconds( 1), evt -> restartText.setVisible(true)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();



        endGameScene.setOnKeyPressed(evt -> app.nextLevel(gameModel.getScore(),gameModel.getLives() ));
        endGameScene.setOnMouseClicked(evt -> app.nextLevel(gameModel.getScore(), gameModel.getLives()));
        
        
    }

    public void clear(){
        gc.clearRect(canvasCols, canvasRows, canvasCols, canvasRows);
    }

    

    public Scene getScene() {
        // TODO Auto-generated method stub
        return endGameScene;
    }

}
