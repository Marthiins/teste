package xadrez;

import placagame.Posicao;

public class PosicaoXadrez {
    private char coluna; //classe usuario
    private int linha;

    //Metodo para determinar as possição do Xadrez com a coluna de A ate H e da linha de 1 a 8
    public PosicaoXadrez(char coluna, int linha) {
        if(coluna < 'a' && coluna > 'h' && linha < 1 && linha > 8){
            throw new XadrezException("Esta classe so aceita posições de a1 a h8");
        }
        this.coluna = coluna;
        this.linha = linha;
    }

    public char getColuna() {
        return coluna;
    }

    public int getLinha() {
        return linha;
    }
    
    protected Posicao toPosicao(){
        return new Posicao(8 - linha, coluna - 'a');
    }
    
    public static PosicaoXadrez toPosicao(Posicao posicao){
        return new PosicaoXadrez((char)('a' + posicao.getColuna()), 8 - posicao.getLinha());
    }
    
    @Override
    public String toString(){
        return "" + coluna + linha;
    }
}
