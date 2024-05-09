import java.util.Arrays;
import java.util.Random;

public class HeikinCKadai {
    public static final int N = 100;
    Kamoku[] kamoku = new Kamoku[N];
    static String kamokuname = "数学";

    public static void main(String[] args) {
        HeikinCKadai heikin = new HeikinCKadai(kamokuname);
        heikin.initalizeScores();
        heikin.printAverage();
        heikin.gokakusha();
    }

    HeikinCKadai(String s) {
        this.kamokuname = s;
    }

    void initalizeScores() {
        Random r = new Random();

        for (int i = 0; i < N; i++) {
            int score = r.nextInt(101); // 0から100までのランダムな点数
            kamoku[i] = new Kamoku(i + 1, score); // 学生番号を1から100まで設定
        }
    }

    void printAverage() {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            sum += kamoku[i].getScore();
        }
        System.out.println("全員の平均点は " + (sum / N) + " です。");
    }

    void gokakusha() {
        // 合格者を抽出
        Kamoku[] passingStudents = Arrays.stream(kamoku)
                .filter(k -> k.getScore() >= 80)
                .toArray(Kamoku[]::new);

        // 合格者を点数の昇順で表示
        Arrays.sort(passingStudents, (a, b) -> a.getScore() - b.getScore());

        // 合格者を出力
        System.out.println("合格者一覧（80点以上）：");
        for (int i = 0; i < passingStudents.length; i++) {
            System.out.println(
                    "学生番号 " + passingStudents[i].getStudentId() + " の点数は " + passingStudents[i].getScore() + " 点");
        }
    }
}
