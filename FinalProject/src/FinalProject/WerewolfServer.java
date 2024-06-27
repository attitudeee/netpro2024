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
        System.out.println("The werewolf server is running.");
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
                    out.println("SETPLAYERCOUNT");
                    while (true) {
                        String input = in.readLine();
                        if (input != null && input.startsWith("PLAYERCOUNT")) {
                            try {
                                minPlayers = Integer.parseInt(input.substring(12));
                                isPlayerCountSet = true;
                                break;
                            } catch (NumberFormatException e) {
                                out.println("INVALIDNUMBER");
                            }
                        }
                    }
                }

                while (true) {
                    out.println("SUBMITNAME");
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

                out.println("NAMEACCEPTED " + name);
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                for (PrintWriter writer : clientWriters) {
                    writer.println("MESSAGE " + name + " has joined");
                }

                // Notify the werewolf of their role
                if (name.equals(werewolfName)) {
                    out.println("ROLE You are the werewolf!");
                } else {
                    out.println("ROLE You are a villager.");
                }

                // Check if minimum players have joined
                synchronized (clientWriters) {
                    if (clientWriters.size() >= minPlayers) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println("MESSAGE Minimum players reached. You can start voting now.");
                            writer.println("VOTINGSTART");
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
                        System.out.println(name + " voted for " + voteFor); // Debug output
                        synchronized (votes) {
                            votes.put(voteFor, votes.getOrDefault(voteFor, 0) + 1);
                        }
                        for (PrintWriter writer : clientWriters) {
                            writer.println("MESSAGE " + name + " voted for " + voteFor);
                        }
                    } else if (input.equals("/endvote")) {
                        System.out.println("End vote command received");
                        showVotingResults(); // Show voting results
                        resetVotes();
                    } else {
                        for (PrintWriter writer : clientWriters) {
                            writer.println("MESSAGE " + name + ": " + input);
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
                StringBuilder results = new StringBuilder("Voting Results:\n");
                for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                    results.append(entry.getKey()).append(": ").append(entry.getValue()).append(" votes\n");
                }
                System.out.println(results.toString()); // Output to server console
                for (PrintWriter writer : clientWriters) {
                    writer.println("RESULT " + results.toString()); // Send to clients
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
