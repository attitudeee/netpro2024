//FacesAWTMain.java
//FacesAWTMain 目標 インナークラスのFaceObjクラスを作ってみよう。描画処理を移譲してください。
//3x3の顔を描画してください。色などもぬってオリジナルな楽しい顔にしてください。

package guibasic;


import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FacesAWTMain {

	public static void main(String[] args){
		new FacesAWTMain();
	}

    FacesAWTMain(){
		FaceFrame f = new FaceFrame();
		f.setSize(800,800);
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
			System.exit(0);}});
		f.setVisible(true);
	}

	// インナークラスを定義
	class FaceFrame extends Frame{


    private FaceObj fobj1;
	int xStart0=50;
	int yStart0=50;

	FaceObj[] fobjs= new FaceObj[9];

	FaceFrame(){
		fobjs[0]= new FaceObj();
		fobjs[1]= new FaceObj();
	}

	public void paint(Graphics g) {
	// この中には、g.drawLine というのは入ってこない
	// Graphicsクラス(型のようなもの---今は--)のgという変数はメソッドに渡す


		
		fobjs[0].setPosition(xStart0,yStart0);
		fobjs[0].drawFace(g);

		
		fobjs[1].setPosition(xStart0+200,yStart0+300);
		fobjs[1].drawFace(g);

	}
	
}//FaceFrame end

	//Faceクラスを作ってみよう。
	private class FaceObj{
		private int w=200;
		private int h=200;
		private int xStart=350;
		private int yStart=350;

		public void drawRim(Graphics g) {  // wが横幅、hが縦幅
			g.drawLine(xStart, yStart, xStart+w, yStart);
			g.drawLine(xStart,yStart, xStart, yStart+h);
			g.drawLine(xStart,yStart+h, xStart+w, yStart+h);
			g.drawLine(xStart+w, yStart, xStart+w,yStart+h);
		}
	
		public void setPosition(int xStart0, int yStart0) {
			this.xStart=xStart0;
			this.yStart=yStart0;
		}

		public void drawFace(Graphics g) {
			drawRim(g);
			drawBrow(g, 10);
			drawEye(g, 35);
			drawNose(g, 40);
			drawMouth(g, 100);
		}

		public void drawBrow(Graphics g,int updown) { // xは目の幅 呼ばれる方(=定義する方)
			int wx1 = xStart + w*2/8;
			int wx2 = xStart + w*5/8;
			int wy = yStart + h/5;
			g.drawLine(wx1, wy+updown, wx1+w*1/8, wy);
			g.drawLine(wx2, wy, wx2+w*1/8, wy+updown);
		}
	
		public void drawNose(Graphics g, int nlen) { // xは鼻の長さ
			int zx = xStart + w/2;
			int zy = yStart + h*2/5;
			g.drawLine(zx, zy, zx, zy+nlen);
	
			
		}
	
		public void drawEye(Graphics g, int r) { // rは目の半径
			g.drawOval(xStart+45,yStart+65,r,r);
			g.drawOval(xStart+125,yStart+65,r,r);
			
		}
	
		public void drawMouth(Graphics g, int mx) { // xは口の幅
			int xMiddle = xStart + w/2;
			int yMiddle = yStart + h - 30;
			g.drawLine(xMiddle - mx/2, yMiddle, xMiddle + mx/2, yMiddle);
		}
	
	}

}//Faces class end