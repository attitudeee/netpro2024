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
    private boolean canVote = false; // 投票が許可されているかどうかを追跡
    private boolean isAlive = true; // プレイヤーが生存しているかどうかを追跡
    private boolean isWerewolf = false; // プレイヤーが人狼かどうかを追跡
    private boolean isWerewolfTurn = false; // 人狼が殺すターンかどうかを追跡
    private boolean gameOver = false; // ゲームが終了したかどうかを追跡
    private String playerName;

    public WerewolfClient() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameOver) {
                    out.println(textField.getText());
                    textField.setText("");
                } else if (isAlive) {
                    if (canVote || isWerewolfTurn) {
                        out.println(textField.getText());
                        textField.setText("");
                    } else {
                        messageArea.append("最低プレイヤー数が集まるか、人狼が決定を下すのを待っています...\n");
                    }
                } else {
                    messageArea.append("あなたは排除されているので、参加できません。\n");
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
        playerName = JOptionPane.showInputDialog(
            frame,
            "スクリーンネームを選んでください:",
            "スクリーンネーム選択",
            JOptionPane.PLAIN_MESSAGE);
        return playerName;
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
            if (line != null) {
                System.out.println(line); // 受信メッセージのデバッグ出力
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
                } else if (line.startsWith("結果")) {
                    String resultMessage = line.substring(4);
                    messageArea.append(resultMessage + "\n"); // 結果をメッセージエリアに表示
                    if (resultMessage.contains("排除されたプレイヤー: ")) {
                        String eliminatedPlayer = resultMessage.substring(resultMessage.indexOf("排除されたプレイヤー: ") + 19).trim();
                        if (eliminatedPlayer.equals(playerName)) {
                            isAlive = false; // プレイヤーのステータスを死に設定
                            textField.setEditable(false); // それ以上のコメントを防ぐ
                        }
                    }
                } else if (line.startsWith("役職")) {
                    String roleMessage = line.substring(4);
                    messageArea.append(roleMessage + "\n"); // 役職情報をメッセージエリアに表示
                    if (roleMessage.contains("人狼")) {
                        isWerewolf = true;
                    }
                } else if (line.startsWith("投票開始")) {
                    canVote = true;
                    messageArea.append("投票を開始できます。\n");
                } else if (line.startsWith("KILL")) {
                    if (isWerewolf && isAlive) {
                        isWerewolfTurn = true;
                        messageArea.append("殺す村人を選択してください: /kill <名前>\n");
                        textField.setEditable(true);
                    }
                } else if (line.startsWith("GAMEOVER")) {
                    gameOver = true;
                    messageArea.append(line.substring(9) + "\n"); // ゲームオーバーメッセージを表示
                    textField.setEditable(true); // それ以上のコメントを許可
                }
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
