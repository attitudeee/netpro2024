import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Calc2 {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("一つ目の数字は?: ");
            String line = reader.readLine();  // ユーザーからの入力を一行読み込む
            int first = Integer.parseInt(line);  // 文字列を整数に変換

            System.out.print("二つ目の数字は?: ");
            line = reader.readLine();  // ユーザーからの次の入力を一行読み込む
            int second = Integer.parseInt(line);  // 文字列を整数に変換

            System.out.println("二つの数字の和: " + (first + second));  // 二つの整数の和を出力
        } catch(IOException e) {
            System.out.println("入力エラーが発生しました: " + e.getMessage());
        } catch(NumberFormatException e) {
            System.out.println("入力が整数ではありません。正しい数字を入力してください: " + e.getMessage());
        }
    }
}
