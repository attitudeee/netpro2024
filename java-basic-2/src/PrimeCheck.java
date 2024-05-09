import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimeCheck {
    public static void main(String[] args) {
        int limit = 100000;
        List<Integer> primes = sieveOfEratosthenes(limit);
        Map<Integer, List<Integer>> digitGroups = new HashMap<>();
        int[] lastDigitPairsCount = new int[100]; // 出現回数を保持する配列

        // 素数を下一桁でグループ分け
        for (int prime : primes) {
            int lastDigit = prime % 10;
            if (lastDigit != 5) {
                digitGroups.putIfAbsent(lastDigit, new ArrayList<>());
                digitGroups.get(lastDigit).add(prime);
            }
        }

        // 連続する素数の下一桁ペアをカウント
        for (int i = 0; i < primes.size() - 1; i++) {
            int currentLastDigit = primes.get(i) % 10;
            int nextLastDigit = primes.get(i + 1) % 10;
            if (currentLastDigit != 5 && nextLastDigit != 5) {
                lastDigitPairsCount[currentLastDigit * 10 + nextLastDigit]++;
            }
        }

        // 各下一桁グループの素数を表示
        for (Map.Entry<Integer, List<Integer>> entry : digitGroups.entrySet()) {
            System.out.println("下一桁が " + entry.getKey() + " の素数: " + entry.getValue());
        }

        // 各下一桁ペアの出現回数を表示
        System.out.println("\n連続する素数の下一桁の出現回数:");
        for (int i = 1; i <= 9; i += 2) {
            for (int j = 1; j <= 9; j += 2) {
                if (i != 5 && j != 5) {
                    System.out.println(i + " -> " + j + ": " + lastDigitPairsCount[i * 10 + j]);
                }
            }
        }

        // 出現回数を降順に並べ替えて表示
        System.out.println("\n連続する素数の下一桁の出現回数のランキング:");
        List<PairCount> pairCounts = new ArrayList<>();
        for (int i = 1; i <= 9; i += 2) {
            for (int j = 1; j <= 9; j += 2) {
                if (i != 5 && j != 5) {
                    int count = lastDigitPairsCount[i * 10 + j];
                    pairCounts.add(new PairCount(i, j, count));
                }
            }
        }
        pairCounts.sort((a, b) -> Integer.compare(b.count, a.count));

        for (PairCount pairCount : pairCounts) {
            System.out.println(pairCount);
        }
    }

    // 素数を生成
    public static List<Integer> sieveOfEratosthenes(int limit) {
        boolean[] isPrime = new boolean[limit + 1];
        List<Integer> primes = new ArrayList<>();

        for (int i = 2; i <= limit; i++) {
            isPrime[i] = true;
        }

        for (int p = 2; p * p <= limit; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i <= limit; i += p) {
                    isPrime[i] = false;
                }
            }
        }

        for (int i = 2; i <= limit; i++) {
            if (isPrime[i] && i > 2) { // 2より大きい素数のみを含む
                primes.add(i);
            }
        }
        return primes;
    }

    // ペアのカウントを保持するクラス
    static class PairCount {
        int firstDigit;
        int secondDigit;
        int count;

        PairCount(int firstDigit, int secondDigit, int count) {
            this.firstDigit = firstDigit;
            this.secondDigit = secondDigit;
            this.count = count;
        }

        @Override
        public String toString() {
            return firstDigit + " -> " + secondDigit + ": " + count;
        }
    }
}
