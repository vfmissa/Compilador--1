public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("primeiro compilador");


        SintaticoPrograma S = new SintaticoPrograma("input.txt");
        S.Analisador();
    }
}
