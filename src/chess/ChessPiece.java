package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece{
    private Color color;
    private int moveCont;
    
    
    public ChessPiece(Color color, Board board) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getMoveCont() {
        return moveCont;
    } 
    
    public void increaseMoveCount(){
        moveCont++;
    }
    
    public void decreaseMoveCount(){
        moveCont--;
    }
    
    public ChessPosition getChessPosition(){
        return ChessPosition.fromPosition(position);
    }
    
    protected boolean isThereOpponentPiece(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p != null && p.getColor() != color;
    }
}
