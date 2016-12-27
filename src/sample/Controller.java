package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {
    @FXML
    Canvas canvas;
    GraphicsContext gc;
    Image image;
    int x, y;
    int dir; // 0:下, 1:左, 2:右, 3:上
    String[] dirString = { "DOWN", "LEFT", "RIGHT", "UP" };
    int[] dirDx = { 0, -1, 1, 0 };
    int[] dirDy = { 1, 0, 0, -1 };
    Timer timer;
    int timerCounter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();
        image = new Image(Paths.get("98.png").toUri().toString());
        x = 200;
        y = 100;
        gc.drawImage(image, 0, dir * 32, 32, 32, x, y, 32, 32);
    }

    @FXML
    void keyPressed(KeyEvent event) {
        System.out.println("keyPressed. " + event.getCode().toString());
        if (timer == null) {
            for( int i = 0; i < 4; ++i ) {
                if (event.getCode().toString().contentEquals(dirString[i])) {
                    dir = i;
                }
            }
            timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            if (timerCounter < 10) {
                                gc.setFill(Color.WHITE);
                                gc.fillRect(x, y, 32, 32);
                                x += 4 * dirDx[dir];
                                y += 4 * dirDy[dir];
                                //gc.drawImage(imageWalk[timerCounter % 2], x, y);
                                gc.drawImage(image, 32 + (timerCounter % 2) * 32, dir * 32, 32, 32, x, y, 32, 32);
                                timerCounter += 1;
                            } else {
                                gc.setFill(Color.WHITE);
                                gc.fillRect(x, y, 32, 64);
                                gc.drawImage(image, 0, dir * 32, 32, 32, x, y, 32, 32);
                                timerCounter = 0;
                                timer.cancel();
                                timer = null;
                                cancel();
                            }
                        }
                    },
                    0, 100);
        }

    }
}
