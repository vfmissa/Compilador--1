import java.util.HashMap;
import java.util.Map;

public class SintaticoPrograma {

    private Lexico lexico;
    private Token simbolo;
    private Map<String, Simbolo> tabelaSimbolo = new HashMap<>();
    private int tipo;

    public SintaticoPrograma(String arq) {
        lexico = new Lexico(arq);
    }

    private void obtemSimbolo() {
        simbolo = lexico.proxToken();
    }

    private boolean verificaSimbolo(String termo) {
        return (simbolo != null && simbolo.getValor().equals(termo));
    }




    private void Prog(){
        System.out.println("prog");
            if(!verificaSimbolo("program")){
                throw new RuntimeException("Erro sintático Programa não declarado: " + simbolo.getValor());
            }
            else{
                Corpo();
            }
            
    }

    private void Corpo() {
        System.out.println("corpo");
        obtemSimbolo();
        if(simbolo.getTipo()!=Token.INDENTIFICADOR){
            throw new RuntimeException("nome programa não pode ser: " + simbolo.getValor());
        }
        else{
            obtemSimbolo();
            DC();
            
        }


    }

    
    private void DC() {
        if (!verificaSimbolo("integer") && !verificaSimbolo("real")) {
            throw new RuntimeException("Erro sintático esperado integer/real encontrado: " + simbolo.getValor());
          }
          if (simbolo.getValor().equals("integer")) {
            this.tipo = Token.INTEIRO;
            System.out.println("declarei int");
          }else {
            this.tipo = Token.REAL;
            System.out.println("declarei real");
          }
        obtemSimbolo();
        if(!verificaSimbolo(":")){
            throw new RuntimeException("esperado : encontrado " + simbolo.getValor());

        }else{
            obtemSimbolo();
            mais_DC();
        }
          
    }

    private void mais_DC() {
        System.out.println("+DC");
        if (simbolo == null || simbolo.getTipo() != Token.INDENTIFICADOR) {
          throw new RuntimeException("Erro sintático esperado identificador encontrado: " + simbolo.getValor());
        }
    
        if (tabelaSimbolo.containsKey(simbolo.getValor())) {
          throw new RuntimeException("Erro semântico identificador já encontrado: " + simbolo.getValor());
        } else {
          tabelaSimbolo.put(simbolo.getValor(), new Simbolo(this.tipo, simbolo.getValor()));
        }
    
        obtemSimbolo();
        Llinha();
    }



    private void Llinha() {
        System.out.println("Llinha'");
        if (verificaSimbolo(",")) {
          obtemSimbolo();
          mais_DC();
        }if(verificaSimbolo(";")){
            System.out.println("simbolo antes de comando>>"+simbolo.getValor());
            Comandos();
        }
    }



      

    private void Comandos() {
        obtemSimbolo();
        System.out.println("comandos");
        if(!verificaSimbolo("begin")){
            DC();
        }else{
            read();
        }
      
    }




    private void read() {
        obtemSimbolo();
        if(verificaSimbolo("read")){
            obtemSimbolo();

        }
    }




    public void Analisador(){
        obtemSimbolo();
        Prog();
        if (simbolo == null) {
        System.out.println("Tudo Certo!");
        } else {
        throw new RuntimeException("Erro sintático esperado fim de cadeia encontrado: " + simbolo.getValor());
        }
    }

  
}