package FinalProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WerewolfServer {
    private static final int PORT = 12345;
    private static int minPlayers = 3; // Minimum number of players required, initially set to 3
    private static boolean isPlayerCountSet = false; // Track if the player count has been set
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static Map<String, String> clientNames = new HashMap<>();
    private static Map<String, Integer> votes = new HashMap<>();
    private static String werewolfName = null; // Track the name of the werewolf

    public static void main(String[] args) throws Exception {
        System.out.println("人狼サーバーが起動しました。");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Set the minimum number of players if not already set
                if (!isPlayerCountSet) {
                    out.println("プレイヤー人数を設定してください。");
                    while (true) {
                        String input = in.readLine();
                        if (input != null && input.startsWith("PLAYERCOUNT")) {
                            try {
                                minPlayers = Integer.parseInt(input.substring(12));
                                isPlayerCountSet = true;
                                break;
                            } catch (NumberFormatException e) {
                                out.println("無効な数値です。");
                            }
                        }
                    }
                }

                while (true) {
                    out.println("名前を入力してください。");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (clientNames) {
                        if (!name.isBlank() && !clientNames.containsKey(name)) {
                            clientNames.put(name, name);
                            votes.put(name, 0); // Initialize votes for the player
                            if (clientNames.size() == 1) {
                                werewolfName = name; // Assign the first player as the werewolf
                            }
                            break;
                        }
                    }
                }

                out.println("名前が承認されました: " + name);
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                for (PrintWriter writer : clientWriters) {
                    writer.println("メッセージ: " + name + "が参加しました。");
                }

                // Notify the werewolf of their role
                if (name.equals(werewolfName)) {
                    out.println("役職: あなたは人狼です！");
                } else {
                    out.println("役職: あなたは村人です。");
                }

                // Check if minimum players have joined
                synchronized (clientWriters) {
                    if (clientWriters.size() >= minPlayers) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println("メッセージ: 最低プレイヤー数に達しました。投票を開始できます。");
                            writer.println("投票開始");
                        }
                    }
                }

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    if (input.startsWith("/vote ")) {
                        String voteFor = input.substring(6);
                        System.out.println(name + "は" + voteFor + "に投票しました。"); // Debug output
                        synchronized (votes) {
                            votes.put(voteFor, votes.getOrDefault(voteFor, 0) + 1);
                        }
                        for (PrintWriter writer : clientWriters) {
                            writer.println("メッセージ: " + name + "が" + voteFor + "に投票しました。");
                        }
                    } else if (input.equals("/投票終了")) {
                        System.out.println("投票終了コマンドを受信しました。");
                        showVotingResults(); // Show voting results
                        resetVotes();
                    } else {
                        for (PrintWriter writer : clientWriters) {
                            writer.println("メッセージ: " + name + ": " + input);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (name != null) {
                    clientNames.remove(name);
                    votes.remove(name);
                    if (name.equals(werewolfName)) {
                        werewolfName = null; // Reset werewolf if they disconnect
                    }
                }
                if (out != null) {
                    clientWriters.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void showVotingResults() {
            synchronized (votes) {
                StringBuilder results = new StringBuilder("投票結果:\n");
                for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                    results.append(entry.getKey()).append(": ").append(entry.getValue()).append("票\n");
                }
                System.out.println(results.toString()); // Output to server console
                for (PrintWriter writer : clientWriters) {
                    writer.println("結果 " + results.toString().replace("\n", "\\n")); // Send to clients with escaped new lines
                }
            }
        }

        private void resetVotes() {
            synchronized (votes) {
                for (String key : votes.keySet()) {
                    votes.put(key, 0);
                }
            }
        }
    }
}
