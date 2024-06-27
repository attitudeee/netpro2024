package FinalProject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WerewolfClient {
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Werewolf");
    private JTextField textField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 40);
    private boolean canVote = false; // Track if voting is allowed

    public WerewolfClient() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (canVote) {
                    out.println(textField.getText());
                    textField.setText("");
                } else {
                    messageArea.append("Waiting for minimum players to join...\n");
                }
            }
        });
    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Werewolf Game",
            JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

    private int getPlayerCount() {
        while (true) {
            String count = JOptionPane.showInputDialog(
                frame,
                "Enter the number of players to start the game:",
                "Player Count Selection",
                JOptionPane.PLAIN_MESSAGE);
            try {
                return Integer.parseInt(count);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid number. Please enter a valid number.");
            }
        }
    }

    private void run() throws IOException {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 12345);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            System.out.println("Received: " + line); // Debug output for received messages
            if (line.startsWith("SETPLAYERCOUNT")) {
                int playerCount = getPlayerCount();
                out.println("PLAYERCOUNT " + playerCount);
            } else if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
                messageArea.append("Name accepted.\n");
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            } else if (line.startsWith("RESULT")) {
                String resultMessage = line.substring(7);
                messageArea.append(resultMessage + "\n"); // Show results in message area
            } else if (line.startsWith("ROLE")) {
                messageArea.append(line.substring(5) + "\n"); // Show role information in message area
            } else if (line.startsWith("VOTINGSTART")) {
                canVote = true;
                messageArea.append("Voting can now start.\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        WerewolfClient client = new WerewolfClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
