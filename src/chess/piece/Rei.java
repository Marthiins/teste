package chess.piece;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Rei extends ChessPiece{
    
    private ChessMatch chessMatch;
    
    public Rei(Board board, Color color, ChessMatch chessMatch) {
        super(color, board);
        this.chessMatch = chessMatch;
    }
    
    @Override
    public String toString(){
        return "R";
    }

    private boolean canMove(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }
    
    private boolean testCastling(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p != null && p instanceof Torre  && p.getColor() == getColor() && p.getMoveCont() == 0;
    }
    
    @Override
    public boolean[][] possibleMoves() {
        boolean [][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        
        Position p = new Position(0,0);
        
        //above
        p.setValues(position.getRow() - 1, position.getColumn());
        if(getBoard().positionExistent(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
        //below
        p.setValues(position.getRow() + 1, position.getColumn());
        if(getBoard().positionExistent(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
        //left
        p.setValues(position.getRow(), position.getColumn() - 1);
        if(getBoard().positionExistent(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
        //right
        p.setValues(position.getRow(), position.getColumn() + 1);
        if(getBoard().positionExistent(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
        //nw
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        if(getBoard().positionExistent(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
        //ne
        p.setValues(position.getRow() - 1, position.getColumn() + 1);
        if(getBoard().positionExistent(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
        //sw
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        if(getBoard().positionExistent(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
        //se
        p.setValues(position.getRow() + 1, position.getColumn() + 1);
        if(getBoard().positionExistent(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        
        //#specialMoce Castling
        if(getMoveCont() == 0 && !chessMatch.getCheck()){
            //#SpecialMove castling kingside rook
            Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
            if(testCastling(posT1)){
                Position p1 = new Position(position.getRow(), position.getColumn() + 1);
                Position p2 = new Position(position.getRow(), position.getColumn() + 2);
                if(getBoard().piece(p1) == null && getBoard().piece(p2) == null){
                    mat[position.getRow()][position.getColumn() + 2] = true;
                }
            }
            //#SpecialMove castling queenside rook
            Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
            if(testCastling(posT2)){
                Position p1 = new Position(position.getRow(), position.getColumn() - 1);
                Position p2 = new Position(position.getRow(), position.getColumn() - 2);
                Position p3 = new Position(position.getRow(), position.getColumn() - 3);
                if(getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null){
                    mat[position.getRow()][position.getColumn() - 2] = true;
                }    
            }
        }
        return mat;
    }
}
