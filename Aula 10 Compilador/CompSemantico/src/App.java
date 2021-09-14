public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("primeiro compilador");


        Sintatico S = new Sintatico("input.txt");
        S.Analisador();
    }
}
