package xadrez;

import placagame.Placa;
import placagame.Peca;
import placagame.Posicao;

public abstract class PecaXadrez extends Peca{
    private Color color;
    private int ContagemMovimento;
    
    
    public PecaXadrez(Color color, Placa placa) {
        super(placa);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getMoveCont() {
        return ContagemMovimento;
    } 
    
    public void aumentarContagemMovimento(){
        ContagemMovimento++;
    }
    
    public void diminuirContagemMovimento(){
        ContagemMovimento--;
    }
    
    public PosicaoXadrez getPosicaoXadrez(){
        return PosicaoXadrez.toPosicao(posicao);
    }
    
    protected boolean existePecaOponente(Posicao posicao){
        PecaXadrez p = (PecaXadrez)getPlaca().piece(posicao);
        return p != null && p.getColor() != color;
    }
}
