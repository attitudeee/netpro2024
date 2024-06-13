package guichat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class GUIAniMultiTCPClient2 {
	public static void main(String[] args) {
		new GUIAniMultiTCPClient2();
	}

	GUIAniMultiTCPClient2() {
		String hostname = "localhost";

		doClientAccess(hostname, "face,place,0,100,200");
		sleep5();
		doClientAccess(hostname, "face,color,1,yellow");
		sleep5();
		doClientAccess(hostname, "face,emotion,0,angry");
		sleep5();

		while (true) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("input command:");
				System.out.println("face,place,0,100,200");
				System.out.println("face,color,1,yellow");
				System.out.println("face,emotion,0,angry");
				System.out.println(":");

				String commandfromClient = reader.readLine();
				if (commandfromClient.equals("end") || commandfromClient.equals("1")) {
					System.out.println("end");
					System.exit(1);
				}

				doClientAccess(hostname, commandfromClient);

			} catch (IOException e) {
				e.printStackTrace();
			}
			sleep5();
		}
	}

	void sleep5() {
		System.out.println("5s wait..");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void doClientAccess(String hostname, String msg) {
		try {
			InetSocketAddress socketAddress = new InetSocketAddress(hostname, 5000);
			Socket socket = new Socket();
			socket.connect(socketAddress, 10000);

			InetAddress inadr;
			if ((inadr = socket.getInetAddress()) != null) {
				System.out.println("Connect to " + inadr);
			} else {
				System.out.println("Connection failed.");
				return;
			}

			String message = msg;

			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			System.out.println("Send message: " + message);

			writer.println(message);

			String getline = rd.readLine();
			System.out.println("Message from Server:" + getline);

			rd.close();
			writer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
