package kadai7;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FacesAWT {
    public static void main(String[] args) {
        new FacesAWT();
    }

    FacesAWT() {
        FaceFrame f = new FaceFrame();
        f.setSize(800, 800);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setVisible(true);
    }

    class FaceFrame extends Frame {
        private int faceWidth = 150;
        private int faceHeight = 150;
        private int padding = 25;

        public void paint(Graphics g) {
            int xStart = padding;
            int yStart = padding;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    drawFace(g, xStart + col * (faceWidth + padding), yStart + row * (faceHeight + padding), col, row);
                }
            }
        }

        public void drawFace(Graphics g, int x, int y, int col, int row) {
            // 顔の色を設定
            Color faceColor = new Color(255 - 20 * row, 255 - 30 * col, 200 + 10 * row);
            g.setColor(faceColor);
            g.fillOval(x, y, faceWidth, faceHeight); // 顔に色を塗る

            // 顔の輪郭を描画
            g.setColor(Color.BLACK);
            g.drawOval(x, y, faceWidth, faceHeight);

            // 目を描画
            int eyeWidth = 30;
            int eyeHeight = 20;
            Color eyeColor = new Color(255, 255, 255);
            g.setColor(eyeColor);
            g.fillOval(x + 35, y + 50, eyeWidth, eyeHeight); // 左目
            g.fillOval(x + 85, y + 50, eyeWidth, eyeHeight); // 右目

            g.setColor(Color.BLACK);
            g.drawOval(x + 35, y + 50, eyeWidth, eyeHeight); // 左目
            g.drawOval(x + 85, y + 50, eyeWidth, eyeHeight); // 右目

            // 瞳を描画
            g.fillOval(x + 45, y + 55, 10, 10);
            g.fillOval(x + 95, y + 55, 10, 10);

            // 鼻を描画
            g.drawLine(x + 75, y + 60, x + 75, y + 100);

            // 口を描画
            if ((col + row) % 3 == 0) {
                g.drawArc(x + 50, y + 100, 50, 40, 0, -180); // 悲しい口
            } else if ((col + row) % 3 == 1) {
                g.drawArc(x + 50, y + 110, 50, 20, 0, 180); // 幸せな口
            } else {
                g.drawLine(x + 50, y + 120, x + 100, y + 120); // 真っ直ぐな口
            }
        }
    }
}
