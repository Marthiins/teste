package placagame;

// classe PlacaException esta estendida para mostrar os erros Exceção de tempo de execução no interface tabuleiro
public class PlacaException extends RuntimeException{ 
    
    public PlacaException(String msg){
        super(msg);
    }
}
