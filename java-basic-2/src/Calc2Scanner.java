import java.util.Scanner;

public class Calc2Scanner {

    public static void main(String[] args) {
        int i = 0;
        int first, second;
        while (i < 10) {
            Scanner scan = new Scanner(System.in);

            String str = scan.next();
            System.out.println("最初のトークンは: " + str);
            first = Integer.parseInt(str);

            str = scan.next();
            second = Integer.parseInt(str);
            System.out.println("次のトークンは  : " + str);
            i++;
            System.out.println(i + "回目の合計は  : " + (first + second));
            System.out.println("----次の数字の入力をお願いします ");
        }

    }
}

// 課題 キーボードから2つの数字を打ち込む
// その足し算結果を、出力する。