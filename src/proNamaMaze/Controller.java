package proNamaMaze;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static com.apple.eio.FileManager.getResource;

public class Controller implements Initializable {
    @FXML
    Canvas canvas;
    GraphicsContext gc;
    // 自機関連
    Image image;
    int pnX, pnY; // プロ生ちゃんのいるマス目
    int pixelX, pixelY; // プロ生ちゃんのいる座標
    int dir; // 0:下, 1:左, 2:右, 3:上
    String[] dirString = { "DOWN", "LEFT", "RIGHT", "UP" };
    int[] dirDx = { 0, -1, 1, 0 };
    int[] dirDy = { 1, 0, 0, -1 };
    // 迷路関連
    int[][] area;
    static int mapSizeX, mapSizeY;
    Image imageMap;
    // タイマー処理関連
    Timer timer;
    int timerCounter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();
        // マップを準備する
        //imageMap = new Image((Paths.get("res/map.png").toUri().toString()));
        imageMap = new Image(getClass().getResourceAsStream("map.png")); // jarで画像を使う書き方
        //System.out.println(getClass().getResource("map.png").toString());
        initMap(15, 15);

        // プロ生ちゃんの画像を準備する
        //image = new Image(Paths.get("res/98.png").toUri().toString());
        image = new Image(getClass().getResourceAsStream("98.png")); // jarで画像を使う書き方
        initMyChar();
    }

    void initMap(int mapSizeX, int mapSizeY) {
        pnX = 1;        // プロ生ちゃんの位置を初期化する
        pnY = 1;
        area = new int[mapSizeX][mapSizeY];
        Map.create(area);
        gc.setFill(Color.BLACK);
        gc.fillRect(16, 16, 32 * mapSizeX, 32 * mapSizeY);
        drawAroundMap(pnX, pnY);
        drawAroundMap(13, 13);
    }

    void initMyChar() {
        pixelX = 16 + pnX * 32;
        pixelY = 16 + pnY * 32;
        gc.drawImage(image, 0, dir * 32, 32, 32, pixelX, pixelY, 32, 32);
    }

    @FXML
    void keyPressed(KeyEvent event) {
        System.out.print("keyPressed. "
                + event.getCode().toString()
                + " (" + pnX + "," + pnY + ")" );
        if (event.getCode().toString().contentEquals("SPACE")) {
            initMap(15, 15);
            initMyChar();
        }
        boolean walkFlg = false;
        if (timer == null) {
            for( int i = 0; i < 4; ++i ) {
                if (event.getCode().toString().contentEquals(dirString[i])) {
                    dir = i;
                    int pnWX = pnX + dirDx[i];
                    int pnWY = pnY + dirDy[i];
                    if (area[pnWX][pnWY] != Map.WALL) {
                        walkFlg = true;
                        pnX = pnWX;
                        pnY = pnWY;
                        System.out.print(" -> " + " (" + pnX + "," + pnY + ")");
                    }
                }
            }
            if (walkFlg) {
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (timerCounter < 8) {
                                    gc.setFill(Color.WHITE);
                                    gc.fillRect(pixelX, pixelY, 32, 32);
                                    pixelX += 4 * dirDx[dir];
                                    pixelY += 4 * dirDy[dir];
                                    //gc.drawImage(imageWalk[timerCounter % 2], x, y);
                                    gc.drawImage(image, 32 + (timerCounter % 2) * 32, dir * 32, 32, 32, pixelX, pixelY, 32, 32);
                                    timerCounter += 1;
                                } else {
                                    drawAroundMap(pnX, pnY);
                                    gc.setFill(Color.WHITE);
                                    gc.fillRect(pixelX, pixelY, 32, 32);
                                    gc.drawImage(image, 0, dir * 32, 32, 32, pixelX, pixelY, 32, 32);
                                    timerCounter = 0;
                                    timer.cancel();
                                    timer = null;
                                    cancel();
                                }
                            }
                        },
                        0, 25);
            }
        }
        System.out.println();
    }

    private void drawAllMap() {
        for (int x = 0; x < area.length; ++x) {
            for (int y = 0; y < area[0].length; ++y) {
                gc.drawImage(imageMap, area[x][y] * 32, 0, 32, 32, x * 32 + 16, y * 32 + 16, 32, 32);
            }
        }
    }

    private void drawAroundMap(int pX, int pY) {
        final int[] dX = { 0, -1, 0, 1, -1, 1, -1, 0, 1 };
        final int[] dY = { 0, -1, -1, -1, 0, 0, 1, 1, 1 };
        int x, y;
        for (int i = 0; i < 9; ++i) {
            x = pX + dX[i];
            y = pY + dY[i];
            gc.drawImage(imageMap, area[x][y] * 32, 0, 32, 32, x * 32 + 16, y * 32 + 16, 32, 32);
        }
    }
}
