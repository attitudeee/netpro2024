import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class BirthdayTCPServ {

    public static void main(String[] args) {
        System.out.print("使用するポート番号を入力してください（例: 5000）→ ");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        scanner.close();

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("サーバーがポート " + port + " で待機中...");

            while (true) { // 無限ループで新しい接続を待ち受ける
                try (Socket socket = server.accept();
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

                    System.out.println("クライアントと接続しました。");

                    while (true) { // 接続中のクライアントからのメッセージを継続的に受け取る
                        BirthdayPresent present = (BirthdayPresent) ois.readObject();
                        String message = present.getMessage();
                        if (message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("exit")) {
                            System.out.println("クライアントから終了コマンドを受信しました。");
                            break; // 接続を終了する
                        }
                        String content = present.getContent();
                        System.out.println("受信メッセージ: " + message);
                        System.out.println("受信プレゼント: " + content);

                        // プレゼントの内容を加工
                        String modifiedContent = content;
                        BirthdayPresent response = new BirthdayPresent();
                        response.setMessage("サーバーです。嬉しい！" + content + "をありがとう！プレゼントのお返しをたのしみにしていてね！");
                        response.setContent("あなたが好きな味の" + modifiedContent + "だよ！");
                        oos.writeObject(response);
                        oos.flush();
                    }
                } catch (Exception e) {
                    System.err.println("通信中にエラーが発生しました: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("サーバーの設定に失敗しました: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
