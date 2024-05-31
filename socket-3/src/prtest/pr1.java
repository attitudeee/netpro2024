package prtest;
import java.util.Random;
public class pr1 {
    Random r = new Random();
    public Random getR() {
        return r;
    }
    public void setR(Random r) {
        this.r = r;
    }
    {
        System.out.print(r);
    }
}
