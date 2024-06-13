package guibasic;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

// 配列で5つのボールを動かしてみよう

public class MovingBallAWT {
    public static void main(String[] args) {
        FFrame f = new FFrame();
        f.setSize(500, 500);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setVisible(true);
    }

    static class FFrame extends Frame implements Runnable {

        Thread th;
        Ball[] balls;
        private boolean enable = true;
        private int counter = 0;

        FFrame() {
            th = new Thread(this);
            th.start();
        }

        public void run() {

            balls = new Ball[5];
            balls[0] = new Ball(200, 150, 10, Color.RED);
            balls[1] = new Ball(50, 250, 20, Color.GREEN);
            balls[2] = new Ball(300, 100, 15, Color.BLUE);
            balls[3] = new Ball(100, 300, 25, Color.YELLOW);
            balls[4] = new Ball(150, 200, 30, Color.MAGENTA);

            while (enable) {

                try {
                    Thread.sleep(100);
                    counter++;
                    if (counter >= 200) enable = false;
                } catch (InterruptedException e) {
                }

                for (Ball ball : balls) {
                    ball.move();
                }

                repaint();  // paint()メソッドが呼び出される
            }
        }

        public void paint(Graphics g) {
            for (Ball ball : balls) {
                ball.draw(g);
            }
        }

        // Ball というインナークラスを作る
        class Ball {
            int x;
            int y;
            int r; // 半径
            Color c;
            Random random = new Random();

            int xDir = 1;  // 1:+方向  -1: -方向
            int yDir = 1;

            Ball(int x, int y, int r, Color c) {
                this.x = x;
                this.y = y;
                this.r = r;
                this.c = c;
            }

            void move() {

                if ((xDir == 1) && (x >= 300)) {
                    xDir = -1;
                    changeColor();
                }
                if ((xDir == -1) && (x <= 100)) {
                    xDir = 1;
                    changeColor();
                }

                if (xDir == 1) {
                    x = x + 10;
                } else {
                    x = x - 10;
                }

                if ((yDir == 1) && (y >= 300)) {
                    yDir = -1;
                    changeColor();
                }
                if ((yDir == -1) && (y <= 100)) {
                    yDir = 1;
                    changeColor();
                }

                if (yDir == 1) {
                    y = y + 10;
                } else {
                    y = y - 10;
                }
            }

            void changeColor() {
                c = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            }

            void draw(Graphics g) {
                g.setColor(c);
                g.fillOval(x, y, 2 * r, 2 * r);  // rは半径なので2倍にする
            }

        }// innner class Ball end

    }

}
