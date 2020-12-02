package chess;

import boardgame.Position;

public class ChessPosition {
    private char coluna;
    private int row;

    public ChessPosition(char coluna, int row) {
        if(coluna < 'a' && coluna > 'h' && row < 1 && row > 8){
            throw new ChessException("Esta classe so aceita posições de a1 a h8");
        }
        this.coluna = coluna;
        this.row = row;
    }

    public char getColuna() {
        return coluna;
    }

    public int getRow() {
        return row;
    }
    
    protected Position toPosition(){
        return new Position(8 - row, coluna - 'a');
    }
    
    public static ChessPosition fromPosition(Position position){
        return new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow());
    }
    
    @Override
    public String toString(){
        return "" + coluna + row;
    }
}
