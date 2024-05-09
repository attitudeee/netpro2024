public class XmasTreeKadai {

    public static void main(String[] args) {
        printTree(10, 5, 3, '*');
    }

    public static void printTree(int maxWidth, int trunkWidth, int trunkHeight, char leafChar) {
        int N = maxWidth;

        // ツリーの葉部分と雪を描画
        for (int j = 0; j <= N; j++) {
            for (int i = 0; i < N - j; i++) {
                System.out.print(i % 2 == 0 ? " " : "!");
            }

            for (int i = 0; i < j * 2 + 1; i++) {
                System.out.print(leafChar);
            }

            for (int i = 0; i < N - j; i++) {
                System.out.print(i % 2 == 0 ? " " : "!");
            }

            System.out.print("\n");
        }

        // ツリーの幹部分を描画
        for (int j = 0; j < trunkHeight; j++) {
            for (int i = 0; i < N - trunkWidth / 2; i++) {
                System.out.print(" ");
            }
            for (int i = 0; i < trunkWidth; i++) {
                System.out.print(leafChar);
            }
            for (int i = 0; i < N - trunkWidth / 2; i++) {
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
}
