package placagame;


public class Placa { //tabuleiro
     private int linhas;
     private int colunas;
     private Peca[][]pieces;

    public Placa(int linhas, int colunas) {
        if(linhas < 1 || colunas < 1){
            throw new PlacaException("Erro na criação do tabuleiro: È necessario pelo menos 1 coluna e 1 linha para criação do tabuleiro");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        pieces = new Peca[linhas][colunas];
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public Peca piece(int linha,int coluna){
        if(!posicaoExistente(linha,coluna)){
            throw new PlacaException("Posição não existente no tabuleiro");
        }
        return pieces[linha][coluna];
    }
    
    public Peca piece(Posicao posicao){
        if(!posicaoExistente(posicao)){
            throw new PlacaException("Posição não existente no tabuleiro");
        }
        return pieces[posicao.getLinha()][posicao.getColuna()];
    }
    
    public void placePeca (Peca piece, Posicao posicao){
        if(haUmaPeca(posicao)){
            throw new PlacaException("There already a piece on posicao" + posicao);
        }
        pieces[posicao.getLinha()][posicao.getColuna()] = piece;
        piece.posicao = posicao;
    } 
    
    public Peca removePeca(Posicao posicao){
        if(!posicaoExistente(posicao)){
            throw new PlacaException("Posição não existente no tabuleiro");
        }
        if(piece(posicao) == null){
            return null;
        }
        Peca aux = piece(posicao);
        aux.posicao = null;
        pieces[posicao.getLinha()][posicao.getColuna()] = null;
        return aux;
    }
    
    public boolean posicaoExistente(int linha, int coluna){
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }
    
    public boolean posicaoExistente(Posicao posicao){
        return posicaoExistente(posicao.getLinha(), posicao.getColuna());
    }
    
    public boolean haUmaPeca (Posicao posicao){
        if(!posicaoExistente(posicao)){
            throw new PlacaException("Posição não existente no tabuleiro");
        }
        return piece(posicao) != null;
    }
}
