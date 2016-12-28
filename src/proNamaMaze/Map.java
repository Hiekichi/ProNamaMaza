package proNamaMaze;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

/**
 * Created by hiekichi on 2016/12/27.
 */
public class Map {
    public static int AISLE = 0;
    public static int WALL = 1;
    public static int GOAL = 2;

    public static void create(int[][] area) {
        // 外周を全て壁にします
        for(int x = 0; x < area.length; ++x) {
            area[x][0] = WALL;
            area[x][area[0].length - 1] = WALL;
        }
        for(int y = 0; y < area[0].length; ++y) {
            area[0][y] = WALL;
            area[area.length - 1][y] = WALL;
        }
        // 外周以外全て通路に初期化します
        for(int x = 1; x < area.length - 1; ++x) {
            for (int y = 1; y < area[0].length - 1; ++y) {
                area[x][y] = AISLE;
            }
        }
        // 一つ置きに柱を立てます
        for (int x = 2; x < area.length - 2; x += 2) {
            for (int y = 2; y < area[0].length - 2; y += 2) {
                area[x][y] = WALL;
            }
        }
        // 柱から上下左右どこか１方向に壁を作ります
        for (int x = 2; x < area.length - 2; x += 2) {
            for (int y = 2; y < area[0].length - 2; y += 2) {
                final int[] dx = { 0, -1, 1, 0 };
                final int[] dy = { 1, 0, 0, -1 };
                int r = (int)(Math.random() * 4);
                area[x + dx[r]][y + dy[r]] = WALL;
            }
        }
        // ゴールを設定します
        area[area.length - 2][area[0].length -2] = GOAL;

        // マップ情報を標準出力に印字します
        for (int x = 0; x < area.length; ++x) {
            for (int y = 0; y < area[0].length; ++y) {
                System.out.print( area[x][y] );
            }
            System.out.println();
        }
    }
}
