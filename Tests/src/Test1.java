import java.util.Random;

public class Test1 {
    public static void main(String[] args) {
        int [] number = new int [32];
        for(int counter = 0;counter < 32;counter ++){
            number [counter] = counter;
        }
        Random random = new Random();
        for (int counter = 0;counter < 32;counter++){
            int p = random.nextInt(32);
            int tmp = number [counter];
            number [counter] = number[p];
            number[p] = tmp;
        }
        for(int counter = 0;counter < 32;counter ++){
            System.out.print(number[counter] + " ");
        }
    }
}
