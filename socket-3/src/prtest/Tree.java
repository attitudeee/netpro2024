package prtest;

public class Tree {

    public static void main(String[] args) {
        /*
         * while(i<10) {
         * Scanner scan = new Scanner(System.in);
         *
         * String str = scan.next();
         * System.out.println("最初のトークンは: " + str);
         * System.out.println("文字を分割すると・・・");
         * String[] word = str.split(",");//splitメソッドの引数に、どこの文字で分割するか指定します。この場合は空白文字。
         * for(String w:word) {
         * System.out.println(w);
         * }
         * System.out.println("word[0]"+word[0]);
         * System.out.println("word[1]"+word[1]);
         * System.out.println("word[2]"+word[2]);
         * System.out.println("word[3]"+word[3]);
         * //19,3,7,c
         * //first = Integer.parseInt(str);
         * System.out.println("----次の数字の入力をお願いします ");
         * }
         */
        System.out.println("---------");
        int N = 10;
        int countN = 0;
        int countA = 0;
        for (int j = 0; j < N; j++) {
            if (j % 2 == 0) {
                if (j != 0) {
                    for (int i = 0; i <= N - j + (countN - 1); i++) {
                        System.out.print("; ");
                    }
                    countN++;
                } else {
                    for (int i = 0; i <= N - j - 1; i++) {
                        System.out.print("; ");
                    }
                    countN++;
                }
            } else {
                if (j != 1) {
                    for (int i = 0; i <= N - j + (countA - 1); i++) {
                        System.out.print(" ;");
                    }
                    countA++;
                } else {
                    for (int i = 0; i <= N - j - 1; i++) {
                        System.out.print(" ;");
                    }
                    countA++;
                }
                System.out.print(" ");
            }
            for (int i = 0; i <= 2 * j; i++) {
                System.out.print("*");
            }
            if (j % 2 == 0) {
                for (int i = 0; i <= N - j; i++) {
                    System.out.print(" ;");
                }
            } else {
                for (int i = 0; i <= N - j; i++) {
                    System.out.print(" ;");
                }
            }
            System.out.print("\n");
        }
        System.out.println("countN : " + countN);
        System.out.println("countA : " + countA);

        System.out.println("---------");
    }
}
