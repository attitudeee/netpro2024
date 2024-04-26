import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReaderExample {
    public static void main(String[] args) {
        String csvFile = "C:/Users/mutsumi/Box/ネットワークプログラミング/jusho.csv"; // ファイルのパスを指定してください
        String line = "";
        String csvSplitBy = ","; // CSVファイルの区切り文字を指定してください

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // 1行をカンマで分割して単語の配列を取得
                String[] addressParts = line.split(csvSplitBy);

                // 住所情報が入っている部分を抽出して、「区」だけを表示
                for (String part : addressParts) {
                    if (part.contains("区")) {
                        int kuIndex = part.indexOf("区");
                        // 「区」を含む文字列の部分だけを切り出して出力
                        String ku = part.substring(0, kuIndex + 1);
                        System.out.println(ku);
                        break; // 1つ見つかればその行の処理を終える
                    }
                }
            } // while end
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// main end
}// class end
