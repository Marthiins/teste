package aplication;

import boardgame.BoardException;
import java.util.InputMismatchException;
import java.util.Scanner;
import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class ProgramaLpoo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> capturada = new ArrayList<>();
        
        while (!chessMatch.getCheckMate()){
            try {
                InterfaceTabuleiro.clearScreen();
                InterfaceTabuleiro.printMatch(chessMatch,capturada);
                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = InterfaceTabuleiro.readChessPosition(sc);
                
                boolean[][] movimentosPossiveis = chessMatch.movimentosPossiveis(source);
                InterfaceTabuleiro.clearScreen();
                InterfaceTabuleiro.bordaImpressao(chessMatch.getPieces(), movimentosPossiveis);
                
                System.out.println();
                System.out.print("Alvo: ");
                ChessPosition target = InterfaceTabuleiro.readChessPosition(sc);
                ChessPiece capturaPeca = chessMatch.realizarMovimentoXadrez(source, target);
                
                if(capturaPeca != null){
                    capturada.add(capturaPeca);
                }
                
                if(chessMatch.getPromoted() != null){
                    System.out.print("Insira peça para cargo (T/Q/C/B) : ");
                    String type = sc.nextLine();
                    chessMatch.replacePromotedPiece(type);
                }
            } 
            catch (ChessException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            } 
            catch (BoardException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch(InvalidParameterException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        InterfaceTabuleiro.clearScreen();
        InterfaceTabuleiro.printMatch(chessMatch, capturada);
    }
}
