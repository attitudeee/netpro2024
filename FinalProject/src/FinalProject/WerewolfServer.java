package FinalProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WerewolfServer {
    private static final int PORT = 12345;
    private static int startPlayers = 3; // ゲーム開始に必要なプレイヤー数
    private static boolean isPlayerCountSet = false; // プレイヤー数が設定されたかどうかを追跡
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static Map<PrintWriter, String> clientNames = new HashMap<>();
    private static Map<String, Integer> votes = new HashMap<>();
    private static Set<String> deadPlayers = new HashSet<>(); // 死んだプレイヤーを追跡
    private static Set<String> votedPlayers = new HashSet<>(); // 投票したプレイヤーを追跡
    private static String werewolfName = null; // 人狼の名前を追跡
    private static String seerName = null; // 占い師の名前を追跡
    private static boolean gameOver = false; // ゲームが終了したかどうかを追跡

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

                // プレイヤー数を設定
                if (!isPlayerCountSet) {
                    out.println("プレイヤー人数を設定してください。");
                    while (true) {
                        String input = in.readLine();
                        if (input != null && input.startsWith("PLAYERCOUNT")) {
                            try {
                                startPlayers = Integer.parseInt(input.substring(12));
                                isPlayerCountSet = true;
                                break;
                            } catch (NumberFormatException e) {
                                out.println("無効な数値です。");
                            }
                        }
                    }
                }

                // 名前を入力
                while (true) {
                    out.println("名前を入力してください。");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (clientNames) {
                        if (!name.isBlank() && !clientNames.containsValue(name)) {
                            clientNames.put(out, name);
                            votes.put(name, 0); // プレイヤーの票を初期化
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

                // ゲーム開始に必要なプレイヤー数が揃ったか確認
                synchronized (clientWriters) {
                    if (clientWriters.size() >= startPlayers) {
                        assignRoles(); // 役割を割り当て
                        for (PrintWriter writer : clientWriters) {
                            writer.println("メッセージ: " + startPlayers + "人のプレイヤーでゲームを開始します。");
                            writer.println("投票開始");
                        }
                    }
                }

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    if (input.startsWith("/vote ") && !deadPlayers.contains(name) && !gameOver) {
                        String voteFor = input.substring(6);
                        System.out.println(name + "は" + voteFor + "に投票しました。"); // デバッグ出力
                        synchronized (votes) {
                            votes.put(voteFor, votes.getOrDefault(voteFor, 0) + 1);
                            votedPlayers.add(name);
                        }
                        for (PrintWriter writer : clientWriters) {
                            writer.println("メッセージ: " + name + "が" + voteFor + "に投票しました。");
                        }
                        // すべてのプレイヤーが投票したか確認
                        if (votedPlayers.size() == (clientNames.size() - deadPlayers.size())) {
                            handleVotingResults(); // 投票結果を処理し、プレイヤーを排除
                            resetVotes();
                            if (!gameOver) {
                                startSeerTurn();
                            }
                        }
                    } else if (input.startsWith("/kill ") && name.equals(werewolfName) && !gameOver) {
                        String killName = input.substring(6);
                        werewolfKill(killName);
                    } else if (input.startsWith("/seer ") && name.equals(seerName) && !gameOver) {
                        String checkName = input.substring(6);
                        seerCheck(checkName);
                    } else if (!deadPlayers.contains(name) || gameOver) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println("メッセージ: " + name + ": " + input);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (name != null) {
                    clientNames.remove(out);
                    votes.remove(name);
                    if (name.equals(werewolfName)) {
                        werewolfName = null; // 切断した場合は人狼をリセット
                    }
                    if (name.equals(seerName)) {
                        seerName = null; // 切断した場合は占い師をリセット
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

        private void assignRoles() {
            List<String> playerNames = new ArrayList<>(clientNames.values());
            Collections.shuffle(playerNames); // 役割をランダムに割り当てるためにリストをシャッフル
            werewolfName = playerNames.get(0); // シャッフルされたリストの最初のプレイヤーを人狼に割り当て
            if (playerNames.size() > 1) {
                seerName = playerNames.get(1); // シャッフルされたリストの二番目のプレイヤーを占い師に割り当て
            }

            for (Map.Entry<PrintWriter, String> entry : clientNames.entrySet()) {
                if (entry.getValue().equals(werewolfName)) {
                    entry.getKey().println("役職: あなたは人狼です！");
                } else if (entry.getValue().equals(seerName)) {
                    entry.getKey().println("役職: あなたは占い師です！");
                } else {
                    entry.getKey().println("役職: あなたは村人です。");
                }
            }
        }

        private void handleVotingResults() {
            synchronized (votes) {
                String eliminatedPlayer = Collections.max(votes.entrySet(), Map.Entry.comparingByValue()).getKey();
                deadPlayers.add(eliminatedPlayer);

                StringBuilder results = new StringBuilder("投票結果:\n");
                for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                    results.append(entry.getKey()).append(": ").append(entry.getValue()).append("票\n");
                }
                results.append("排除されたプレイヤー: ").append(eliminatedPlayer).append("\n");

                System.out.println(results.toString()); // サーバーコンソールに出力
                for (PrintWriter writer : clientWriters) {
                    writer.println("結果 " + results.toString()); // クライアントに送信
                }

                // 勝利条件を確認
                checkWinConditions();
            }
        }

        private void startSeerTurn() {
            PrintWriter seerWriter = getWriterByName(seerName);
            if (seerWriter != null) {
                seerWriter.println("SEER: 一人を選んで /seer <名前> と入力してください。");
            }
        }

        private void startWerewolfKillTurn() {
            PrintWriter werewolfWriter = getWriterByName(werewolfName);
            if (werewolfWriter != null) {
                werewolfWriter.println("KILL: 村人を選択してください: /kill <名前>で村人を殺します。");
            }
        }

        private void checkWinConditions() {
            int aliveVillagers = (int) clientNames.values().stream().filter(name -> !name.equals(werewolfName) && !deadPlayers.contains(name)).count();
            int aliveWerewolves = werewolfName != null && !deadPlayers.contains(werewolfName) ? 1 : 0;

            if (aliveWerewolves == 0) {
                for (PrintWriter writer : clientWriters) {
                    writer.println("GAMEOVER: 村人の勝利です！人狼が排除されました。");
                }
                gameOver = true;
            } else if (aliveWerewolves >= aliveVillagers) {
                for (PrintWriter writer : clientWriters) {
                    writer.println("GAMEOVER: 人狼の勝利です！人狼が村人と同数またはそれ以上になりました。");
                }
                gameOver = true;
            }
        }

        private void resetVotes() {
            synchronized (votes) {
                for (String key : votes.keySet()) {
                    votes.put(key, 0);
                }
            }
            votedPlayers.clear();
        }

        private PrintWriter getWriterByName(String name) {
            for (Map.Entry<PrintWriter, String> entry : clientNames.entrySet()) {
                if (entry.getValue().equals(name)) {
                    return entry.getKey();
                }
            }
            return null;
        }

        private void werewolfKill(String killName) {
            if (!clientNames.containsValue(killName) || deadPlayers.contains(killName)) {
                PrintWriter werewolfWriter = getWriterByName(werewolfName);
                if (werewolfWriter != null) {
                    werewolfWriter.println("メッセージ: 無効な名前です。もう一度試してください。");
                }
                return;
            }

            deadPlayers.add(killName);
            StringBuilder killMessage = new StringBuilder("人狼が殺しました: ").append(killName).append("\n");

            System.out.println(killMessage.toString()); // サーバーコンソールに出力
            for (PrintWriter writer : clientWriters) {
                writer.println("結果 " + killMessage.toString()); // クライアントに送信
            }

            checkWinConditions();
        }

        private void seerCheck(String checkName) {
            if (!clientNames.containsValue(checkName) || deadPlayers.contains(checkName)) {
                PrintWriter seerWriter = getWriterByName(seerName);
                if (seerWriter != null) {
                    seerWriter.println("メッセージ: 無効な名前です。もう一度試してください。");
                }
                return;
            }

            StringBuilder seerMessage = new StringBuilder("占い師の結果: ").append(checkName).append("は");
            if (checkName.equals(werewolfName)) {
                seerMessage.append("人狼です。\n");
            } else {
                seerMessage.append("人狼ではありません。\n");
            }

            System.out.println(seerMessage.toString()); // サーバーコンソールに出力
            PrintWriter seerWriter = getWriterByName(seerName);
            if (seerWriter != null) {
                seerWriter.println("結果 " + seerMessage.toString()); // 占い師にのみ結果を送信
            }

            startWerewolfKillTurn(); // 次に人狼のターンを開始
        }
    }
}
