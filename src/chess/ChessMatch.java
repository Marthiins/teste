package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.piece.Bispo;
import chess.piece.Cavalo;
import chess.piece.Peao;
import chess.piece.Rainha;
import chess.piece.Rei;
import chess.piece.Torre;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    
    private int turn;
    private Color currentPlayer;
    private Board tabuleiro;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;
    
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces  = new ArrayList<>();
    
    public ChessMatch() {
        tabuleiro = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        check = false;
        initialSetup();
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }
                   
    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public int getTurn() {
        return turn;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }
    
    public ChessPiece[][] getPieces() {

        ChessPiece[][] mat = new ChessPiece[tabuleiro.getRows()][tabuleiro.getColumns()];
        
        for (int i = 0; i < tabuleiro.getRows(); i++) {
            for (int j = 0; j < tabuleiro.getColumns(); j++) {
                mat[i][j] = (ChessPiece) tabuleiro.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] movimentosPossiveis(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition();
        validarPosicaoOrigem(position);
        return tabuleiro.piece(position).possibleMoves();
    }
    
    public ChessPiece realizarMovimentoXadrez(ChessPosition sourcePosition, ChessPosition targetPosition) {
       
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validarPosicaoOrigem(source);
        validarpPosicaoAlvo(source,target);
        Piece pecaCapturada = makeMove(source, target);
        
        if (testCheck(currentPlayer)){
            undoMove(source, target, pecaCapturada);
            throw new ChessException("Você não pode se colocar em xeque");
        }
        
        ChessPiece movedPiece = (ChessPiece)tabuleiro.piece(target);
        
        //#Special move promotion
        promoted = null;
        if(movedPiece instanceof Peao){
            if(movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || movedPiece.getColor() == Color.BLACK && target.getRow() == 7){
                promoted = (ChessPiece)tabuleiro.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }
        
        check = (testCheck(opponent(currentPlayer))) ? true : false;
        
        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }
        else{
            nextTurn();
        }
        
        //#SpecialMove en passant
        if(movedPiece instanceof Peao && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)){
            enPassantVulnerable = movedPiece;
        }
        else{
            enPassantVulnerable = null;
        }
        
        return (ChessPiece) pecaCapturada;
    }

    public ChessPiece replacePromotedPiece (String type){
        if(promoted == null){
            throw new IllegalStateException("There is no piece to be promoted");
        }
        if(!type.equals("Q") && !type.equals("C") && !type.equals("T") && !type.equals("B")){
            throw new InvalidParameterException("Invalid type for promotion");
        }
        
        Position pos = promoted.getChessPosition().toPosition();
        Piece p = tabuleiro.removePiece(pos);
        piecesOnTheBoard.remove(p);
        
        ChessPiece newPiece = novaPeca(type,promoted.getColor());
        tabuleiro.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);
        
        return newPiece;
    }
    
    private ChessPiece novaPeca(String type,Color color){
        if(type.equals("B")) return new Bispo(tabuleiro, color);
        if(type.equals("Q")) return new Rainha(tabuleiro, color);
        if(type.equals("T")) return new Torre(tabuleiro, color);
        
        return new Cavalo(tabuleiro, color);
    }
    
    private Piece makeMove(Position source, Position target) {
       
        ChessPiece p = (ChessPiece) tabuleiro.removePiece(source);
        p.increaseMoveCount();
        
        Piece pecaCapturada = tabuleiro.removePiece(target);
        tabuleiro.placePiece(p, target);
        
        if(pecaCapturada != null){
            piecesOnTheBoard.remove(pecaCapturada);
            capturedPieces.add(pecaCapturada);
        }
        
        //#Special move castling Kinsside rook
        if(p instanceof Rei && target.getColumn() == source.getColumn()+ 2){
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece torre = (ChessPiece)tabuleiro.removePiece(sourceT);
            tabuleiro.placePiece(torre, targetT);
            torre.increaseMoveCount();
        }
        
        //#Special move castling Kinsside rook
        if(p instanceof Rei && target.getColumn() == source.getColumn()- 2){
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece torre = (ChessPiece)tabuleiro.removePiece(sourceT);
            tabuleiro.placePiece(torre, targetT);
            torre.increaseMoveCount();
        }
        
        //#Special move en passant
        if(p  instanceof Peao){
            if(source.getColumn() != target.getColumn() && pecaCapturada == null){
                Position peaoPosicao;
                if(p.getColor() == Color.WHITE){
                    peaoPosicao = new Position(target.getRow() + 1,target.getColumn());
                }
                else{
                    peaoPosicao = new Position(target.getRow() - 1,target.getColumn());
                }
                pecaCapturada = tabuleiro.removePiece(peaoPosicao);
                capturedPieces.add(pecaCapturada);
                piecesOnTheBoard.remove(pecaCapturada);
            }
        }              
                
        return pecaCapturada;
    }
    
    private void undoMove(Position source,Position target, Piece pecaCapturada){
        ChessPiece p = (ChessPiece) tabuleiro.removePiece(target);
        p.decreaseMoveCount();
        tabuleiro.placePiece(p, source);
        
        if(pecaCapturada != null){
            tabuleiro.placePiece(p, target);
            capturedPieces.remove(pecaCapturada);
            piecesOnTheBoard.add(pecaCapturada);
        }
        
        //#Special move castling Kinsside rook
        if(p instanceof Rei && target.getColumn() == source.getColumn()+ 2){
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece torre = (ChessPiece)tabuleiro.removePiece(targetT);
            tabuleiro.placePiece(torre, sourceT);
            torre.decreaseMoveCount();
        }
        
        //#Special move castling Kinsside rook
        if(p instanceof Rei && target.getColumn() == source.getColumn()- 2){
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece torre = (ChessPiece)tabuleiro.removePiece(targetT);
            tabuleiro.placePiece(torre, sourceT);
            torre.decreaseMoveCount();        
        }
        
        //#Special move en passant
        if(p  instanceof Peao){
            if(source.getColumn() != target.getColumn() && pecaCapturada == enPassantVulnerable){
                ChessPiece pawn = (ChessPiece)tabuleiro.removePiece(target);
                Position peaoPosicao;
                if(p.getColor() == Color.WHITE){
                    peaoPosicao = new Position(3,target.getColumn());
                }
                else{
                    peaoPosicao = new Position(4,target.getColumn());
                }
                
                tabuleiro.placePiece(pawn, peaoPosicao);
            }
        }
    }

    private void validarPosicaoOrigem(Position position) {
        if (!tabuleiro.thereIsAPiece(position)) {
            throw new ChessException("Não há nenhuma peça sobre a posição de origem");
        }
        
        if(currentPlayer != ((ChessPiece)tabuleiro.piece(position)).getColor()){
            throw new ChessException("A peça escolhida não é sua.");
        }
        
        if (!tabuleiro.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("Não há movimentos possíveis para a peça escolhida");
        }
    }
    
    private void validarpPosicaoAlvo(Position source, Position target){
        if(!tabuleiro.piece(source).possibleMove(target)){
            throw new ChessException("A peça escolhida não pode se mover para a posição alvo");
        }
        
    }
    
    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
    
    private ChessPiece rei(Color color){
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p : list){
            if(p instanceof Rei){
                return (ChessPiece)p;
            }
        }
        throw new IllegalStateException("Não há " + color + " rei no conselho");
    }
    
    private boolean testCheck(Color color){
        Position reiPosicao = rei(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for(Piece p : opponentPieces){
            boolean[][] mat = p.possibleMoves();
            if(mat[reiPosicao.getRow()][reiPosicao.getColumn()]){
                return true;
            }
        }
        return false;
    }
    
    public boolean testCheckMate(Color color){
        if(!testCheck(color)){
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p : list){
            boolean[][] mat = p.possibleMoves();
            for(int i = 0; i<tabuleiro.getRows();i++){
                for(int j = 0; j<tabuleiro.getColumns();j++){
                    if(mat[i][j]){
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i,j);
                        Piece capturedPiece = makeMove(source,target);
                        boolean testCheck = testCheck(color);
                        undoMove(source,target,capturedPiece);
                        if(!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        tabuleiro.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Torre(tabuleiro, Color.WHITE));
        placeNewPiece('b', 1, new Cavalo(tabuleiro, Color.WHITE));
        placeNewPiece('c', 1, new Bispo(tabuleiro, Color.WHITE));
        placeNewPiece('d', 1, new Rainha(tabuleiro, Color.WHITE));
        placeNewPiece('e', 1, new Rei(tabuleiro, Color.WHITE, this));
        placeNewPiece('f', 1, new Bispo(tabuleiro, Color.WHITE));
        placeNewPiece('g', 1, new Cavalo(tabuleiro, Color.WHITE));
        placeNewPiece('h', 1, new Torre(tabuleiro, Color.WHITE));
        placeNewPiece('a', 2, new Peao(tabuleiro, Color.WHITE, this));
        placeNewPiece('b', 2, new Peao(tabuleiro, Color.WHITE, this));
        placeNewPiece('c', 2, new Peao(tabuleiro, Color.WHITE, this));
        placeNewPiece('d', 2, new Peao(tabuleiro, Color.WHITE, this));
        placeNewPiece('e', 2, new Peao(tabuleiro, Color.WHITE, this));
        placeNewPiece('f', 2, new Peao(tabuleiro, Color.WHITE, this));
        placeNewPiece('g', 2, new Peao(tabuleiro, Color.WHITE, this));
        placeNewPiece('h', 2, new Peao(tabuleiro, Color.WHITE, this));

        placeNewPiece('a', 8, new Torre(tabuleiro, Color.BLACK));
        placeNewPiece('b', 8, new Cavalo(tabuleiro, Color.BLACK));
        placeNewPiece('c', 8, new Bispo(tabuleiro, Color.BLACK));
        placeNewPiece('d', 8, new Rainha(tabuleiro, Color.BLACK));
        placeNewPiece('e', 8, new Rei(tabuleiro, Color.BLACK, this));
        placeNewPiece('f', 8, new Bispo(tabuleiro, Color.BLACK));
        placeNewPiece('g', 8, new Cavalo(tabuleiro, Color.BLACK));
        placeNewPiece('h', 8, new Torre(tabuleiro, Color.BLACK));
        placeNewPiece('a', 7, new Peao(tabuleiro, Color.BLACK, this));
        placeNewPiece('b', 7, new Peao(tabuleiro, Color.BLACK, this));
        placeNewPiece('c', 7, new Peao(tabuleiro, Color.BLACK, this));
        placeNewPiece('d', 7, new Peao(tabuleiro, Color.BLACK, this));
        placeNewPiece('e', 7, new Peao(tabuleiro, Color.BLACK, this));
        placeNewPiece('f', 7, new Peao(tabuleiro, Color.BLACK, this));
        placeNewPiece('g', 7, new Peao(tabuleiro, Color.BLACK, this));
        placeNewPiece('h', 7, new Peao(tabuleiro, Color.BLACK, this));
    }

}
