class Renshu {

    // xを2倍にして返す関数
    public int doubleValue(int x) {
        return x * 2;
    }

    // ここに続きを実装していく。

    public int sumUpToN(int x) {
        int sum = 0;
        for (int i = 1; i <= x; i++) {
            sum += i;
        }
        return sum;
    }

    public int sumFromPtoQ(int x, int y) {
        int sum = 0;
        if (x > y) {
            return -1;
        }
        for (int i = x; i <= y; i++) {
            sum += i;
        }
        return sum;
    }

    public int sumFromArrayIndex(int[] x, int y) {
        if (x.length <= y) {
            return -1;
        }
        int sum = 0;
        for (int i = y; i < x.length; i++) {
            sum += x[i];
        }
        return sum;
    }

    public int selectMaxValue(int[] x) {
        int max = x[0];
        for (int i = 1; i < x.length; i++) {
            if (max < x[i]) {
                max = x[i];
            }
        }
        return max;
    }

    public int selectMinValue(int[] x) {
        int min = x[0];
        for (int i = 1; i < x.length; i++) {
            if (min > x[i]) {
                min = x[i];
            }
        }
        return min;
    }

    public int selectMaxIndex(int[] x) {
        int maxind = 0;
        for (int i = 1; i < x.length; i++) {
            if (x[maxind] < x[i]) {
                maxind = i;
            }
        }
        return maxind;
    }

    public int selectMinIndex(int[] x) {
        int minind = 0;
        for (int i = 1; i < x.length; i++) {
            if (x[minind] > x[i]) {
                minind = i;
            }
        }
        return minind;
    }

    public static void swapArrayElements(int[] array, int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static boolean swapTwoArrays(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            int temp = a[i];
            a[i] = b[i];
            b[i] = temp;
        }
        return true;
    }
}