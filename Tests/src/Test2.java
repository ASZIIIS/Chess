public class Test2 {
    public static void main(String[] args) {
        char c1 = '5';
        char c2 = '0';
        String c3 = String.valueOf(c1 + c2);
        System.out.println(c3);
        int c4 = Integer.parseInt(String.valueOf(c1 + c2));
        System.out.println(c4);
    }
}
