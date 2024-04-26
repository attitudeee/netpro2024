
// C言語では、#include に相当する
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HowOldAreYou {

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // BufferedReader というのは、データ読み込みのクラス(型)
        // クラスの変数を作るには、new を使う。

        // readLine() は、入出力エラーの可能性がある。エラー処理がないとコンパイルできない。
        // Java では、 try{ XXXXXXXX } catch(エラーの型 変数) { XXXXXXXXXXXXXXXXXX} と書く
        try {
            while (true) {
                System.out.println("何歳ですか?");
                String line = reader.readLine();
                if (line.equals("q") || line.equals("e")) {
                    System.out.println("繰り返しを終了します。");
                    break;
                }
                int age = Integer.parseInt(line);
                if (age < 0 || age >= 120) {
                    System.out.println("年齢は0以上120未満の範囲で入力して下さい。");
                } else {
                    System.out.println("あなたは" + age + "歳ですね。");
                    System.out.println("2030年のあなたの年齢は、" + (age + 6) + "歳ですね。");
                    if ((157 - age) < 45 & (157 - age) > 0) {
                        System.out.println("あなたの生まれた元号は、明治" + (157 - age) + "年ですね。");
                        System.out.println("あなたの生まれた西暦は、" + (2024 - age) + "年ですね。");
                    }
                    if ((113 - age) < 15 & (113 - age) > 0) {
                        System.out.println("あなたの生まれた元号は、大正" + (113 - age) + "年ですね。");
                        System.out.println("あなたの生まれた西暦は、" + (2024 - age) + "年ですね。");
                    }
                    if ((99 - age) < 64 & (99 - age) > 0) {
                        System.out.println("あなたの生まれた元号は、昭和" + (99 - age) + "年ですね。");
                        System.out.println("あなたの生まれた西暦は、" + (2024 - age) + "年ですね。");
                    }
                    if ((36 - age) < 31 & (36 - age) > 0) {
                        System.out.println("あなたの生まれた元号は、平成" + (36 - age) + "年ですね。");
                        System.out.println("あなたの生まれた西暦は、" + (2024 - age) + "年ですね。");
                    }
                    if ((6 - age) < 7 & (6 - age) > 0) {
                        System.out.println("あなたの生まれた元号は、令和" + (6 - age) + "年ですね。");
                        System.out.println("あなたの生まれた西暦は、" + (2024 - age) + "年ですね。");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}

// 課題 キーボードから数字を打ち込む
// その結果、 あなたは、???歳ですね、と画面に表示させる。
// その後、あなたは10年後、????歳ですね、と画面に表示させる。
