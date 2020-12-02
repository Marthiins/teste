package xadrez.peca;

import placagame.Placa;
import placagame.Posicao;
import xadrez.JogoXadrez;
import xadrez.PecaXadrez;
import xadrez.Color;

public class Peao extends PecaXadrez{

    private JogoXadrez chessMatch;
    
    public Peao(Placa placa,Color color, JogoXadrez chessMatch) {
        super(color, placa);
        this.chessMatch = chessMatch;
    }

    @Override
    public boolean[][] movimentoPossiveis() {
        boolean [][] mat = new boolean[getPlaca().getLinhas()][getPlaca().getColunas()];
        
        Posicao p = new Posicao(0,0);
        
        if(getColor() == Color.WHITE){
            p.definirValores(posicao.getLinha() - 1, posicao.getColuna());
            if(getPlaca().posicaoExistente(p) && !getPlaca().haUmaPeca(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            
            p.definirValores(posicao.getLinha() - 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha()-1, posicao.getColuna());
            if(getPlaca().posicaoExistente(p) && !getPlaca().haUmaPeca(p)&& getPlaca().posicaoExistente(p2) && !getPlaca().haUmaPeca(p2) && getMoveCont() == 0){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            
            p.definirValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if(getPlaca().posicaoExistente(p) && existePecaOponente(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            
            p.definirValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if(getPlaca().posicaoExistente(p) && existePecaOponente(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            
            //#Special move el passant white
            if(posicao.getLinha() == 3){
                Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if(getPlaca().posicaoExistente(left) && existePecaOponente(left) && getPlaca().piece(left) == chessMatch.getEnPassantVulnerable()){
                    mat[left.getLinha() - 1][left.getColuna()] = true;
                }
                Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if(getPlaca().posicaoExistente(right) && existePecaOponente(right) && getPlaca().piece(right) == chessMatch.getEnPassantVulnerable()){
                    mat[right.getLinha() - 1][right.getColuna()] = true;
                }
            }
        }
        else{
            p.definirValores(posicao.getLinha() + 1, posicao.getColuna());
            if(getPlaca().posicaoExistente(p) && !getPlaca().haUmaPeca(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            
            p.definirValores(posicao.getLinha() + 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
            if(getPlaca().posicaoExistente(p) && !getPlaca().haUmaPeca(p)&& getPlaca().posicaoExistente(p2) && !getPlaca().haUmaPeca(p2) && getMoveCont() == 0){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            
            p.definirValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if(getPlaca().posicaoExistente(p) && existePecaOponente(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            
            p.definirValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if(getPlaca().posicaoExistente(p) && existePecaOponente(p)){
                mat[p.getLinha()][p.getColuna()] = true;
            }
            
            //#Special move el passant black
            if(posicao.getLinha() == 4){
                Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if(getPlaca().posicaoExistente(left) && existePecaOponente(left) && getPlaca().piece(left) == chessMatch.getEnPassantVulnerable()){
                    mat[left.getLinha() + 1][left.getColuna()] = true;
                }
                Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if(getPlaca().posicaoExistente(right) && existePecaOponente(right) && getPlaca().piece(right) == chessMatch.getEnPassantVulnerable()){
                    mat[right.getLinha() + 1][right.getColuna()] = true;
                }
            }
        }        
            
        return mat;
    }
    
    @Override
    public String toString(){
        return "P";
    }
}
