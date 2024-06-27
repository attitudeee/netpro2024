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
    private static int expectedPlayers = 3; // Number of players required to start the game
    private static boolean isPlayerCountSet = false; // Track if the player count has been set
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static Map<String, String> clientNames = new HashMap<>();
    private static Map<String, Integer> votes = new HashMap<>();
    private static Map<String, Boolean> hasVoted = new HashMap<>(); // Track if a player has voted
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

                // Set the number of players if not already set
                if (!isPlayerCountSet) {
                    out.println("プレイヤー人数を設定してください。");
                    while (true) {
                        String input = in.readLine();
                        if (input != null && input.startsWith("PLAYERCOUNT")) {
                            try {
                                expectedPlayers = Integer.parseInt(input.substring(12));
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
                            hasVoted.put(name, false); // Initialize voting status for the player
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
                    writer.println(name + "が参加しました。");
                }

                // Notify the werewolf of their role
                if (name.equals(werewolfName)) {
                    out.println("役職: あなたは人狼です！");
                } else {
                    out.println("役職: あなたは村人です。");
                }

                // Check if the expected number of players have joined
                synchronized (clientWriters) {
                    if (clientWriters.size() == expectedPlayers) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println("指定されたプレイヤー数に達しました。投票を開始できます。");
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
                            hasVoted.put(name, true); // Mark the player as having voted
                        }
                        for (PrintWriter writer : clientWriters) {
                            writer.println(name + "が" + voteFor + "に投票しました。");
                        }
                        checkAllVotesIn(); // Check if all players have voted
                    } else {
                        for (PrintWriter writer : clientWriters) {
                            writer.println(name + ": " + input);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (name != null) {
                    clientNames.remove(name);
                    votes.remove(name);
                    hasVoted.remove(name);
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

        private void checkAllVotesIn() {
            synchronized (hasVoted) {
                for (boolean voted : hasVoted.values()) {
                    if (!voted) {
                        return; // Not all players have voted yet
                    }
                }
                showVotingResults(); // All players have voted, show results
                resetVotes(); // Reset votes for next round
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
                for (String key : hasVoted.keySet()) {
                    hasVoted.put(key, false); // Reset voting status for the next round
                }
            }
        }
    }
}
