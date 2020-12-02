package boardgame;

public class Board {
     private int rows;
     private int columns;
     private Piece[][]pieces;

    public Board(int rows, int columns) {
        if(rows < 1 || columns < 1){
            throw new BoardException("Erro na criação do tabuleiro: È necessario pelo menos 1 coluna e 1 linha para criação do tabuleiro");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row,int column){
        if(!positionExistent(row,column)){
            throw new BoardException("Posição não existente no tabuleiro");
        }
        return pieces[row][column];
    }
    
    public Piece piece(Position position){
        if(!positionExistent(position)){
            throw new BoardException("Posição não existente no tabuleiro");
        }
        return pieces[position.getRow()][position.getColumn()];
    }
    
    public void placePiece (Piece piece, Position position){
        if(thereIsAPiece(position)){
            throw new BoardException("There already a piece on position" + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    } 
    
    public Piece removePiece(Position position){
        if(!positionExistent(position)){
            throw new BoardException("Posição não existente no tabuleiro");
        }
        if(piece(position) == null){
            return null;
        }
        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }
    
    public boolean positionExistent(int row, int column){
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }
    
    public boolean positionExistent(Position position){
        return positionExistent(position.getRow(), position.getColumn());
    }
    
    public boolean thereIsAPiece (Position position){
        if(!positionExistent(position)){
            throw new BoardException("Posição não existente no tabuleiro");
        }
        return piece(position) != null;
    }
}
