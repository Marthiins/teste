package xadrez.peca;

import placagame.Placa;

import placagame.Posicao;
import xadrez.PecaXadrez;
import xadrez.Color;

  public class Bispo extends PecaXadrez{

    public Bispo(Placa placa, Color color ) {
        super(color, placa);
    }

    
   @Override
    public String toString(){
        return "B";
    }
    
    @Override
    public boolean[][] movimentoPossiveis() {
        boolean [][] mat = new boolean[getPlaca().getLinhas()][getPlaca().getColunas()];
        
        Posicao p = new Posicao(0,0);
        
        //nw
        p.definirValores(posicao.getLinha() - 1,posicao.getColuna() - 1);
        while(getPlaca().posicaoExistente(p) && !getPlaca().haUmaPeca(p)){
            mat[p.getLinha()][p.getColuna()] = true;
            p.definirValores(p.getLinha() - 1, p.getColuna() - 1);
        }
        if(getPlaca().posicaoExistente(p) && existePecaOponente(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //ne
        p.definirValores(posicao.getLinha() - 1,posicao.getColuna() + 1);
        while(getPlaca().posicaoExistente(p) && !getPlaca().haUmaPeca(p)){
            mat[p.getLinha()][p.getColuna()] = true;
            p.definirValores(p.getLinha() - 1, p.getColuna() + 1);
        }
        if(getPlaca().posicaoExistente(p) && existePecaOponente(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //se
        p.definirValores(posicao.getLinha() + 1,posicao.getColuna() + 1);
        while(getPlaca().posicaoExistente(p) && !getPlaca().haUmaPeca(p)){
            mat[p.getLinha()][p.getColuna()] = true;
             p.definirValores(p.getLinha() + 1, p.getColuna() + 1);
        }
        if(getPlaca().posicaoExistente(p) && existePecaOponente(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        //sw
        p.definirValores(posicao.getLinha() + 1,posicao.getColuna() - 1);
        while(getPlaca().posicaoExistente(p) && !getPlaca().haUmaPeca(p)){
            mat[p.getLinha()][p.getColuna()] = true;
             p.definirValores(p.getLinha() + 1, p.getColuna() - 1);
        }
        if(getPlaca().posicaoExistente(p) && existePecaOponente(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        
        return mat;
    }
    
}
