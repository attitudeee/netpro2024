import java.util.Random;

public class NetproLabMember {
    public static final int years = 15;
    public static final int columns = 3;

    public static void main(String[] args) {
        int[][] members = new int[years][columns];
        Random random = new Random();
        double totalRatio = 1.0;

        for (int i = 0; i < years; i++) {
            members[i][0] = 110 + (random.nextInt(20) - 10); // 学生の総数
            members[i][1] = 20 + i; // 女性の割合(%)
            members[i][2] = 10 + (random.nextInt(7) - 3); // 岩井研の人数

            int men = members[i][0] - (int) (members[i][0] * ((float) members[i][1] / 100)); // 男性数を求める
            long cpsRatio = combination(members[i][0], members[i][2]); // 総数から岩井研の人数を取り出す組み合わせ
            long menRatio = combination(men, members[i][2]); // 男性の中から岩井研のメンバーを取り出す組み合わせ

            totalRatio *= (double) menRatio / cpsRatio; // 岩井研の人数に男性しか入らない割合
        }

        System.out.println("15年間岩井研に女性の学生が来ない確率: " + totalRatio);
    }

    public static final long combination(final int n, int r) {
        if (r > n)
            return 0;
        if (r > n / 2)
            r = n - r;
        long result = 1;
        for (int i = 1; i <= r; i++) {
            result = result * (n - i + 1) / i;
        }
        return result;
    }
}