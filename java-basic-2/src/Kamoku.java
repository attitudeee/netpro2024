public class Kamoku {
    String name;
    int score;
    private int studentid;

    Kamoku(int id, int s) { // コンストラクタ
        studentid = id;
        score = s;
    }

    // setScore メソッドを定義
    public void setScore(int num) {
        score = num;
    }

    // getScore メソッドを定義
    public int getScore() {
        return score;
    }

    // getStudentId メソッドを定義
    public int getStudentId() {
        return studentid;
    }
}
