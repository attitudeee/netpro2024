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
    private JFrame frame = new JFrame("人狼ゲーム");
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
                    messageArea.append("最低プレイヤー数が集まるまでお待ちください...\n");
                }
            }
        });
    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "サーバーのIPアドレスを入力してください:",
            "人狼ゲームへようこそ",
            JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "スクリーンネームを選んでください:",
            "スクリーンネーム選択",
            JOptionPane.PLAIN_MESSAGE);
    }

    private int getPlayerCount() {
        while (true) {
            String count = JOptionPane.showInputDialog(
                frame,
                "ゲームを開始するプレイヤー数を入力してください:",
                "プレイヤー数選択",
                JOptionPane.PLAIN_MESSAGE);
            try {
                return Integer.parseInt(count);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "無効な数値です。有効な数値を入力してください。");
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
            System.out.println(line); // Debug output for received messages
            if (line.startsWith("プレイヤー人数を設定してください。")) {
                int playerCount = getPlayerCount();
                out.println("PLAYERCOUNT " + playerCount);
            } else if (line.startsWith("名前を入力してください。")) {
                out.println(getName());
            } else if (line.startsWith("名前が承認されました")) {
                textField.setEditable(true);
                messageArea.append("名前が承認されました。\n");
            } else if (line.startsWith("メッセージ")) {
                messageArea.append(line.substring(5) + "\n");
            } else if (line.startsWith("結果 投票結果:")) {
                String resultMessage = line.substring(8).replace("\\n", "\n"); // Handle escaped new lines
                messageArea.append("投票結果:\n" + resultMessage); // Show results in message area without additional newline
            } else if (line.startsWith("役職")) {
                messageArea.append(line.substring(4) + "\n"); // Show role information in message area
            } else if (line.startsWith("投票開始")) {
                canVote = true;
                messageArea.append("投票を開始できます。\n");
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
