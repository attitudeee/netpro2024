public class NoHolidayException extends Exception {

    public void printCustomMessage() {
        System.err.println("この日は平日だよ！はたらきたくないねー。エラーメッセージです。");
    }
}
