import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.Socket;
import java.util.Scanner;

public class BirthdayTCPClient {

    public static void main(String[] args) {
        System.out.print("接続するサーバーのポート番号を入力してください（例: 5000）→ ");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        scanner.nextLine(); // 数字入力後の改行文字を消費する

        try (Socket socket = new Socket("localhost", port);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("サーバーに接続しました。");

            while (true) {
                System.out.print("メッセージを入力してください(例:HappyBirthday!): ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("exit")) {
                    System.out.println("通信を終了します。");
                    break;
                }

                System.out.print("プレゼントの内容を入力してください(例:cake): ");
                String content = scanner.nextLine();

                BirthdayPresent present = new BirthdayPresent();
                present.setMessage(message);
                present.setContent(content);

                oos.writeObject(present);
                oos.flush();

                BirthdayPresent response = (BirthdayPresent) ois.readObject();
                System.out.println("サーバーからの返信メッセージ: " + response.getMessage());
                System.out.println("サーバーからの返信プレゼント: " + response.getContent());
            }
        } catch (BindException be) {
            be.printStackTrace();
            System.err.println("ポート番号が不正か、サーバが起動していません");
            System.err.println("サーバが起動しているか確認してください");
            System.err.println("別のポート番号を指定してください(6000など)");
        } catch (Exception e) {
            System.err.println("エラーが発生したのでプログラムを終了します");
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
    }
}
