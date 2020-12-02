package xadrez.peca;

import placagame.Placa;
import placagame.Posicao;
import xadrez.JogoXadrez;
import xadrez.PecaXadrez;
import xadrez.Color;

public class Rei extends PecaXadrez{
    
    private JogoXadrez partidaXadrez;
    
    public Rei(Placa placa, Color color, JogoXadrez partidaXadrez) {
        super(color, placa);
        this.partidaXadrez = partidaXadrez;
    }
    
    @Override
    public String toString(){
        return "R";
    }

    private boolean canMove(Posicao posicao){
        PecaXadrez p = (PecaXadrez)getPlaca().piece(posicao);
        return p == null || p.getColor() != getColor();
    }
    
    private boolean testCastling(Posicao posicao){
        PecaXadrez p = (PecaXadrez)getPlaca().piece(posicao);
        return p != null && p instanceof Torre  && p.getColor() == getColor() && p.getMoveCont() == 0;
    }
    
    @Override
    public boolean[][] movimentoPossiveis() {
        boolean [][] mat = new boolean[getPlaca().getLinhas()][getPlaca().getColunas()];
        
        Posicao p = new Posicao(0,0);
        
        //above
        p.definirValores(posicao.getLinha() - 1, posicao.getColuna());
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //below
        p.definirValores(posicao.getLinha() + 1, posicao.getColuna());
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //left
        p.definirValores(posicao.getLinha(), posicao.getColuna() - 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //right
        p.definirValores(posicao.getLinha(), posicao.getColuna() + 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //nw
        p.definirValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //ne
        p.definirValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //sw
        p.definirValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //se
        p.definirValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //#specialMoce Castling
        if(getMoveCont() == 0 && !partidaXadrez.getCheck()){
            //#SpecialMove castling kingside rook
            Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
            if(testCastling(posT1)){
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
                if(getPlaca().piece(p1) == null && getPlaca().piece(p2) == null){
                    mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
                }
            }
            //#SpecialMove castling queenside rook
            Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
            if(testCastling(posT2)){
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
                Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
                if(getPlaca().piece(p1) == null && getPlaca().piece(p2) == null && getPlaca().piece(p3) == null){
                    mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
                }    
            }
        }
        return mat;
    }
}
