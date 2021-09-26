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
        while(!verificaSimbolo(".")){
            if(!verificaSimbolo("program")){
                throw new RuntimeException("Erro sintático Programa não declarado: " + simbolo.getValor());
            }
            else{
                Corpo();
            }
        }
    }

    private void Corpo() {
        System.out.println("corpo");
        obtemSimbolo();
        if(simbolo.getTipo()!=Token.INDENTIFICADOR || (verificaSimbolo("begin"))){
            throw new RuntimeException("nome programa não pode ser: " + simbolo.getValor());
        }
        else{
            obtemSimbolo();
            DC();
        }


    }

    
    private void DC() {
        
        while(!verificaSimbolo("begin")){
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
        obtemSimbolo();
        Comandos();
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
            System.out.println("simbolo antes de dc>>"+simbolo.getValor());
            obtemSimbolo();
            DC();
        }
    }



      

    private void Comandos() {
        System.out.println("simbolo dentro de comando >> "+simbolo.getValor());
        while(!verificaSimbolo("end")){
            if (tabelaSimbolo.containsKey(simbolo.getValor())) {
                obtemSimbolo();
                if (simbolo.getTipo() != Token.SIMBOLO){
                    throw new RuntimeException("Erro sintático esperado := encontrado: " + simbolo.getValor());
                }
                obtemSimbolo();
                expressao();
            }
            else if(verificaSimbolo("read"))
            Read();
            else if(verificaSimbolo("write")){
                write();
            }
            else{
                pfalsa();
                condicional();
            }
        }obtemSimbolo();
        if(verificaSimbolo(".")){
            System.out.println("fim programa");
        }
    }
    
    private void condicional() {
        
       while(!verificaSimbolo("$")){
            obtemSimbolo();
            System.out.println("condicional>> "+simbolo.getValor());
            expressao();
        } System.out.println("fim condicional");
        obtemSimbolo();
        Comandos();
    }


    private void pfalsa() {
        if(verificaSimbolo("else")){
            System.out.println("condicional falsa");
            obtemSimbolo();
            Comandos();
        }
    }

    private void expressao() {
        
        System.out.println("expressão");
        System.out.println("simbolo dentro de expressão >> "+simbolo.getValor());
        termo();
       
    }

    private void termo() {
        System.out.println("termo expressão >> "+simbolo.getValor());
        if (simbolo.getTipo() != Token.INDENTIFICADOR & simbolo.getTipo() != Token.INTEIRO & simbolo.getTipo() != Token.REAL){
            throw new RuntimeException("variavel esperado int float or identficador encontrado: "+simbolo.getValor());
        }if((!tabelaSimbolo.containsKey(simbolo.getValor()) & simbolo.getTipo() != Token.INTEIRO & simbolo.getTipo() != Token.REAL)){
            throw new RuntimeException("variavel: " + simbolo.getValor()+" não declarada");
          } else {
            obtemSimbolo();
            maistermo();
          }


    }
   
    private void maistermo() {
        while(!verificaSimbolo(";") && !relacao() && !verificaSimbolo("then")){            
            System.out.println("operador >> "+simbolo.getValor());
            if (simbolo.getTipo() != Token.SIMBOLO){
                throw new RuntimeException("esperado expressão aritmietica antes de : "+simbolo.getValor());
            
            }else{
                obtemSimbolo();
                termo();
            }
        }
        if(relacao()){
            //obtemSimbolo();
            System.out.println("fim expressão volta a condicional");
            condicional();
        }
        System.out.println("fim expressão volta a comandos");
        obtemSimbolo();
        Comandos();

    }

    private boolean relacao(){
        if(verificaSimbolo(">")||verificaSimbolo("<")){
            return true;
        }
        else if(verificaSimbolo(">=")||verificaSimbolo("<=")){
            return true;
        }else if(verificaSimbolo("=")||verificaSimbolo("<>")){
            return true;
        }else{
            return false;
        }
        
    }



    private void Read() {
        System.out.print("read");
        if(verificaSimbolo("read")){
            obtemSimbolo();
            abreparentesis();
                        
        }else{
            throw new RuntimeException("esperado Read antes de " + simbolo.getValor());
        }

    }
    private void write() {
        System.out.print("write");
        if(verificaSimbolo("write")){
            obtemSimbolo();
            abreparentesis2();
                        
        }else{
            throw new RuntimeException("esperado Read antes de " + simbolo.getValor());
        }

    }
    

    private void abreparentesis() {
        System.out.print("(");
        if(verificaSimbolo("(")){
            obtemSimbolo();
            if (!tabelaSimbolo.containsKey(simbolo.getValor())) {
                throw new RuntimeException("variavel : " + simbolo.getValor()+" não declarada");
              } else {
                System.out.print(simbolo.getValor());
                obtemSimbolo();
                fechaparentesis();
              }

        }
    }



    private void fechaparentesis() {
        System.out.print(");\n");
        if(verificaSimbolo(")")){
            obtemSimbolo();
            if (!verificaSimbolo(";")) {
                throw new RuntimeException("esperado ; encontrado: " + simbolo.getValor());
              } else {
                  obtemSimbolo();
                  Comandos();
                }

        }
    }


    private void abreparentesis2() {
        System.out.print("(");
        if(verificaSimbolo("(")){
            obtemSimbolo();
            if (!tabelaSimbolo.containsKey(simbolo.getValor())) {
                throw new RuntimeException("variavel : " + simbolo.getValor()+" não declarada");
              } else {
                System.out.print(simbolo.getValor());
                obtemSimbolo();
                fechaparentesis2();
              }

        }
    }

    private void fechaparentesis2() {
        System.out.print(")\n");
        if(verificaSimbolo(")")){
            obtemSimbolo();
            Comandos();
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