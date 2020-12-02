package xadrez.peca;

import placagame.Placa;
import placagame.Posicao;
import xadrez.PecaXadrez;
import xadrez.Color;

public class Cavalo extends PecaXadrez{

    public Cavalo(Placa placa, Color color) {
        super(color, placa);
    }

    @Override
    public String toString(){
        return "C";
    }
    
    private boolean canMove(Posicao posicao){
        PecaXadrez p = (PecaXadrez)getPlaca().piece(posicao);
        return p == null || p.getColor() != getColor();
    }
    
    @Override
    public boolean[][] movimentoPossiveis() {
        boolean [][] mat = new boolean[getPlaca().getLinhas()][getPlaca().getColunas()];
        
        Posicao p = new Posicao(0,0);
        
        p.definirValores(posicao.getLinha() - 1, posicao.getColuna() - 2);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        p.definirValores(posicao.getLinha() - 2 , posicao.getColuna() - 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        p.definirValores(posicao.getLinha() - 2, posicao.getColuna() + 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        p.definirValores(posicao.getLinha() - 1, posicao.getColuna() + 2);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        p.definirValores(posicao.getLinha() + 1, posicao.getColuna() + 2);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        p.definirValores(posicao.getLinha() + 2, posicao.getColuna() + 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        p.definirValores(posicao.getLinha() + 2, posicao.getColuna() - 1);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        p.definirValores(posicao.getLinha() + 1, posicao.getColuna() - 2);
        if(getPlaca().posicaoExistente(p) && canMove(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        return mat;
    }
    
}
