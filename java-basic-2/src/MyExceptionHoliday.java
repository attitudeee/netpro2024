import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyExceptionHoliday {

    public static void main(String[] args) {
        MyExceptionHoliday myE = new MyExceptionHoliday();
    }

    MyExceptionHoliday() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.println("何日ですか? (終了するには0を入力)");
                String line = reader.readLine();
                int theday;

                try {
                    theday = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("有効な数字を入力してください。");
                    continue;
                }

                if (theday < 0 || theday > 31) {
                    System.out.println("0から31以内の範囲で値を入力してください。");
                    continue;
                }

                if (theday == 0) {
                    System.out.println("終了します。");
                    break;
                }

                System.out.println("日付" + theday + "日ですね。");

                try {
                    test(theday);
                    System.out.println(theday + "日は休日または土日です。");
                } catch (NoHolidayException e) {
                    e.printCustomMessage();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    void test(int theday) throws NoHolidayException {
        // 土日と祝日をチェックする
        if (!(theday == 3 || theday == 4 || theday == 5 || theday == 6 || theday == 11 ||
                theday == 12 || theday == 18 || theday == 19 || theday == 25 || theday == 26)) {
            throw new NoHolidayException();
        }
    }
}
