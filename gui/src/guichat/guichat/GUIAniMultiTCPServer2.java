package guichat;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class GUIAniMultiTCPServer2 {

	GUIAnimationMain animation;

	public GUIAniMultiTCPServer2(GUIAnimationMain animation) {
		this.animation = animation;
		System.out.println("server started:" + this.animation);
		System.out.println("creating srv socket");

		ServerSocket serverSoc = null;
		try {
			serverSoc = new ServerSocket(5000);
			boolean flag = true;
			System.out.println("Waiting for Connection. ");
			int thcounter = 0;
			while (flag) {
				Socket socket = null;
				socket = serverSoc.accept();
				new SrvWorkerThread(socket, thcounter++).start();
			}
		} catch (IOException e) {
			System.out.println("IOException!");
			e.printStackTrace();
		} finally {
			try {
				if (serverSoc != null) {
					serverSoc.close();
				}
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}
	}

	class SrvWorkerThread extends Thread {
		private Socket soc;

		public SrvWorkerThread(Socket sct, int thcounter) {
			soc = sct;
			System.out.println("Thread " + thcounter + " is Generated.  Connect to " + soc.getInetAddress());
		}

		public void run() {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				PrintWriter sendout = new PrintWriter(soc.getOutputStream(), true);
				String line;
				line = reader.readLine();
				System.out.println("Message from client :" + line);

				if (line.equals("end")) {
					System.exit(1);
				}

				if (line.startsWith("face,color") && animation != null) {
					String[] sline = line.split(",");
					int which = Integer.parseInt(sline[2]);
					String color = sline[3];
					if (color.equals("yellow")) {
						animation.setFaceColor(which, Color.yellow);
					} else if (color.equals("red")) {
						animation.setFaceColor(which, Color.red);
					}
				} else if (line.startsWith("face,place") && animation != null) {
					String[] sline = line.split(",");
					int which = Integer.parseInt(sline[2]);
					int x = Integer.parseInt(sline[3]);
					int y = Integer.parseInt(sline[4]);
					animation.setFacePlace(which, x, y, line);
				} else if (line.startsWith("face,emotion") && animation != null) {
					String[] sline = line.split(",");
					int which = Integer.parseInt(sline[2]);
					String emotion = sline[3];
					animation.setFaceEmotion(which, emotion);
				}

				sendout.println("Message is received at Server. Thank you! your message is [" + line + "]");

			} catch (IOException ioex) {
				ioex.printStackTrace();
			} finally {
				try {
					if (soc != null) {
						soc.close();
					}
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}
		}
	}
}
