import java.io.Serializable;

public class TaskObject implements Serializable, ITask {

    private int x;
    private int result;

    @Override
    public void setExecNumber(int x) {
        this.x = x;
    }

    @Override
    public void exec() {
        result = findMaxPrime(x);

    }

    @Override
    public int getResult() {
        return result;
    }

    private int findMaxPrime(int n) {
        for (int i = n; i > 1; i--) {
            if (isPrime(i)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n <= 3) {
            return true;
        }

        // 2と3の倍数以外の奇数を確認
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }

        // 6k ± 1の形の数のみを確認
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }

        return true;
    }

    public boolean numJ() {
        if (x <= 1) {
            return false;
        }
        return true;
    }

}