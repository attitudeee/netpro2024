package thread;

// Runnable インターフェースを実装するクラスです。
public class ExThreadsMain {
    public static void main(String[] args) {
        // スレッドを生成し、異なるタスクを実行します。
        Thread threadA = new Thread(new TaskPrintAlphabet());
        Thread threadB = new Thread(new TaskCountNumbers());
        Thread threadC = new Thread(new TaskPrintWords());

        // スレッドを開始します。
        threadA.start();
        threadB.start();
        threadC.start();
    }
}

// アルファベットを出力するタスクです。
class TaskPrintAlphabet implements Runnable {
    @Override
    public void run() {
        for (char c = 'a'; c <= 'z'; c++) {
            System.out.println("Alphabet: " + c);
            try {
                Thread.sleep(400); // スレッドを400ミリ秒間停止します。
            } catch (InterruptedException e) {
                System.err.println("TaskPrintAlphabet interrupted: " + e.getMessage());
            }
        }
    }
}

// 数字をカウントするタスクです。
class TaskCountNumbers implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 50; i++) {
            System.out.println("Count: " + i);
            try {
                Thread.sleep(200); // スレッドを200ミリ秒間停止します。
            } catch (InterruptedException e) {
                System.err.println("TaskCountNumbers interrupted: " + e.getMessage());
            }
        }
    }
}

// "Hello World!" を出力するタスクです。
class TaskPrintWords implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Hello World!");
            try {
                Thread.sleep(500); // スレッドを500ミリ秒間停止します。
            } catch (InterruptedException e) {
                System.err.println("TaskPrintWords interrupted: " + e.getMessage());
            }
        }
    }
}
