package xadrez;

import placagame.Placa;
import placagame.Peca;
import placagame.Posicao;
import xadrez.peca.Bispo;
import xadrez.peca.Cavalo;
import xadrez.peca.Peao;
import xadrez.peca.Rainha;
import xadrez.peca.Rei;
import xadrez.peca.Torre;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class JogoXadrez {
    
    private int turno;
    private Color atualPlayer;
    private Placa tabuleiro;
    private boolean check;
    private boolean checkMate;
    private PecaXadrez passandoVuneravel;
    private PecaXadrez promovido;
    
    private List<Peca> piecesOnThePlaca = new ArrayList<>();
    private List<Peca> capturedPecas  = new ArrayList<>();
    
    public JogoXadrez() {
        tabuleiro = new Placa(8, 8);
        turno = 1;
        atualPlayer = Color.WHITE;
        check = false;
        initialSetup();
    }

    public PecaXadrez getEnPassantVulnerable() {
        return passandoVuneravel;
    }

    public PecaXadrez getPromovido() {
        return promovido;
    }
                   
    public Color getAtualPlayer() {
        return atualPlayer;
    }

    public int getTurno() {
        return turno;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }
    
    public PecaXadrez[][] getPecas() {

        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        
        for (int i = 0; i < tabuleiro.getLinhas(); i++) {
            for (int j = 0; j < tabuleiro.getColunas(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] movimentosPossiveis(PosicaoXadrez sourcePosicao){
        Posicao posicao = sourcePosicao.toPosicao();
        validarPosicaoOrigem(posicao);
        return tabuleiro.piece(posicao).movimentoPossiveis();
    }
    
    public PecaXadrez realizarMovimentoXadrez(PosicaoXadrez sourcePosicao, PosicaoXadrez targetPosicao) {
       
        Posicao source = sourcePosicao.toPosicao();
        Posicao target = targetPosicao.toPosicao();
        validarPosicaoOrigem(source);
        validarpPosicaoAlvo(source,target);
        Peca pecaCapturada = makeMove(source, target);

        if (testCheck(atualPlayer)){
            undoMove(source, target, pecaCapturada);
            throw new XadrezException("Você não pode se colocar em xeque");
        }
        
        PecaXadrez movedPeca = (PecaXadrez)tabuleiro.piece(target);


        //# Promoção de movimento especial
        promovido = null;
        if(movedPeca instanceof Peao){
            if(movedPeca.getColor() == Color.WHITE && target.getLinha() == 0 || movedPeca.getColor() == Color.BLACK && target.getLinha() == 7){
                promovido = (PecaXadrez)tabuleiro.piece(target);
                promovido = substituirPecaPromovida("Q");
            }
        }
        
        check = (testCheck(opponent(atualPlayer))) ? true : false;
        
        if(testCheckMate(opponent(atualPlayer))){
            checkMate = true;
        }
        else{
            nextTurno();
        }
        
        //#SpecialMove en passant
        if(movedPeca instanceof Peao && (target.getLinha() == source.getLinha() - 2 || target.getLinha() == source.getLinha() + 2)){
            passandoVuneravel = movedPeca;
        }
        else{
            passandoVuneravel = null;
        }
        
        return (PecaXadrez) pecaCapturada;
    }

    public PecaXadrez substituirPecaPromovida (String type){
        if(promovido == null){
            throw new IllegalStateException("Não há nenhuma peça a ser promovida");
        }
        if(!type.equals("Q") && !type.equals("C") && !type.equals("T") && !type.equals("B")){
            throw new InvalidParameterException("Tipo inválido para promoção");
        }
        
        Posicao pos = promovido.getPosicaoXadrez().toPosicao();
        Peca p = tabuleiro.removePeca(pos);
        piecesOnThePlaca.remove(p);
        
        PecaXadrez newPeca = novaPeca(type,promovido.getColor());
        tabuleiro.placePeca(newPeca, pos);
        piecesOnThePlaca.add(newPeca);
        
        return newPeca;
    }
    
    private PecaXadrez novaPeca(String type,Color color){
        if(type.equals("B")) return new Bispo(tabuleiro, color);
        if(type.equals("Q")) return new Rainha(tabuleiro, color);
        if(type.equals("T")) return new Torre(tabuleiro, color);
        
        return new Cavalo(tabuleiro, color);
    }
    
    private Peca makeMove(Posicao source, Posicao target) {
       
        PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(source);
        p.aumentarContagemMovimento();
        
        Peca pecaCapturada = tabuleiro.removePeca(target);
        tabuleiro.placePeca(p, target);
        
        if(pecaCapturada != null){
            piecesOnThePlaca.remove(pecaCapturada);
            capturedPecas.add(pecaCapturada);
        }
        
        //#Special move castling Kinsside rook
        if(p instanceof Rei && target.getColuna() == source.getColuna()+ 2){
            Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() + 3);
            Posicao targetT = new Posicao(source.getLinha(), source.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez)tabuleiro.removePeca(sourceT);
            tabuleiro.placePeca(torre, targetT);
            torre.aumentarContagemMovimento();
        }
        
        //#Special move castling Kinsside rook
        if(p instanceof Rei && target.getColuna() == source.getColuna()- 2){
            Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() - 4);
            Posicao targetT = new Posicao(source.getLinha(), source.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez)tabuleiro.removePeca(sourceT);
            tabuleiro.placePeca(torre, targetT);
            torre.aumentarContagemMovimento();
        }
        
        //#Special move en passant
        if(p  instanceof Peao){
            if(source.getColuna() != target.getColuna() && pecaCapturada == null){
                Posicao peaoPosicao;
                if(p.getColor() == Color.WHITE){
                    peaoPosicao = new Posicao(target.getLinha() + 1,target.getColuna());
                }
                else{
                    peaoPosicao = new Posicao(target.getLinha() - 1,target.getColuna());
                }
                pecaCapturada = tabuleiro.removePeca(peaoPosicao);
                capturedPecas.add(pecaCapturada);
                piecesOnThePlaca.remove(pecaCapturada);
            }
        }              
                
        return pecaCapturada;
    }
    
    private void undoMove(Posicao source,Posicao target, Peca pecaCapturada){
        PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(target);
        p.diminuirContagemMovimento();
        tabuleiro.placePeca(p, source);
        
        if(pecaCapturada != null){
            tabuleiro.placePeca(p, target);
            capturedPecas.remove(pecaCapturada);
            piecesOnThePlaca.add(pecaCapturada);
        }
        
        //#Special move castling Kinsside rook
        if(p instanceof Rei && target.getColuna() == source.getColuna()+ 2){
            Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() + 3);
            Posicao targetT = new Posicao(source.getLinha(), source.getColuna() + 1);
            PecaXadrez torre = (PecaXadrez)tabuleiro.removePeca(targetT);
            tabuleiro.placePeca(torre, sourceT);
            torre.diminuirContagemMovimento();
        }
        
        //#Special move castling Kinsside rook
        if(p instanceof Rei && target.getColuna() == source.getColuna()- 2){
            Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() - 4);
            Posicao targetT = new Posicao(source.getLinha(), source.getColuna() - 1);
            PecaXadrez torre = (PecaXadrez)tabuleiro.removePeca(targetT);
            tabuleiro.placePeca(torre, sourceT);
            torre.diminuirContagemMovimento();        
        }
        
        //#Special move en passant
        if(p  instanceof Peao){
            if(source.getColuna() != target.getColuna() && pecaCapturada == passandoVuneravel){
                PecaXadrez pawn = (PecaXadrez)tabuleiro.removePeca(target);
                Posicao peaoPosicao;
                if(p.getColor() == Color.WHITE){
                    peaoPosicao = new Posicao(3,target.getColuna());
                }
                else{
                    peaoPosicao = new Posicao(4,target.getColuna());
                }
                
                tabuleiro.placePeca(pawn, peaoPosicao);
            }
        }
    }

    private void validarPosicaoOrigem(Posicao posicao) {
        if (!tabuleiro.haUmaPeca(posicao)) {
            throw new XadrezException("Não há nenhuma peça sobre a posição de origem");
        }
        
        if(atualPlayer != ((PecaXadrez)tabuleiro.piece(posicao)).getColor()){
            throw new XadrezException("A peça escolhida não é sua.");
        }
        
        if (!tabuleiro.piece(posicao).existeQualquerMovimentoPossivel()) {
            throw new XadrezException("Não há movimentos possíveis para a peça escolhida");
        }
    }
    
    private void validarpPosicaoAlvo(Posicao source, Posicao target){
        if(!tabuleiro.piece(source).possivelMovimento(target)){
            throw new XadrezException("A peça escolhida não pode se mover para a posição alvo");
        }
        
    }
    
    private void nextTurno(){
        turno++;
        atualPlayer = (atualPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
        //JOptionPane.showMessageDialog(null, "Jogador:" + atualPlayer,"Alerta", JOptionPane.ERROR_MESSAGE);
    }

    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
    
    private PecaXadrez rei(Color color){
        List<Peca> list = piecesOnThePlaca.stream().filter(x -> ((PecaXadrez)x).getColor() == color).collect(Collectors.toList());
        for(Peca p : list){
            if(p instanceof Rei){
                return (PecaXadrez)p;
            }
        }
        throw new IllegalStateException("Não há " + color + " rei no conselho");
    }
    
    private boolean testCheck(Color color){
        Posicao reiPosicao = rei(color).getPosicaoXadrez().toPosicao();
        List<Peca> opponentPecas = piecesOnThePlaca.stream().filter(x -> ((PecaXadrez)x).getColor() == opponent(color)).collect(Collectors.toList());
        for(Peca p : opponentPecas){
            boolean[][] mat = p.movimentoPossiveis();
            if(mat[reiPosicao.getLinha()][reiPosicao.getColuna()]){
                return true;
            }
        }
        return false;
    }
    
    public boolean testCheckMate(Color color){
        if(!testCheck(color)){
            return false;
        }
        List<Peca> list = piecesOnThePlaca.stream().filter(x -> ((PecaXadrez)x).getColor() == color).collect(Collectors.toList());
        for(Peca p : list){
            boolean[][] mat = p.movimentoPossiveis();
            for(int i = 0; i<tabuleiro.getLinhas();i++){
                for(int j = 0; j<tabuleiro.getColunas();j++){
                    if(mat[i][j]){
                        Posicao source = ((PecaXadrez)p).getPosicaoXadrez().toPosicao();
                        Posicao target = new Posicao(i,j);
                        Peca capturedPeca = makeMove(source,target);
                        boolean testCheck = testCheck(color);
                        undoMove(source,target,capturedPeca);
                        if(!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private void localPecaTabuleiroInicio(char coluna, int linha, PecaXadrez piece) {
        tabuleiro.placePeca(piece, new PosicaoXadrez(coluna, linha).toPosicao());
        piecesOnThePlaca.add(piece);
    }

    private void initialSetup() {
       localPecaTabuleiroInicio('a', 1, new Torre(tabuleiro, Color.WHITE));
        localPecaTabuleiroInicio('b', 1, new Cavalo(tabuleiro, Color.WHITE));
        localPecaTabuleiroInicio('c', 1, new Bispo(tabuleiro, Color.WHITE));
        localPecaTabuleiroInicio('d', 1, new Rainha(tabuleiro, Color.WHITE));
        localPecaTabuleiroInicio('e', 1, new Rei(tabuleiro, Color.WHITE, this));
        localPecaTabuleiroInicio('f', 1, new Bispo(tabuleiro, Color.WHITE));
        localPecaTabuleiroInicio('g', 1, new Cavalo(tabuleiro, Color.WHITE));
        localPecaTabuleiroInicio('h', 1, new Torre(tabuleiro, Color.WHITE));
        localPecaTabuleiroInicio('a', 2, new Peao(tabuleiro, Color.WHITE, this));
        localPecaTabuleiroInicio('b', 2, new Peao(tabuleiro, Color.WHITE, this));
        localPecaTabuleiroInicio('c', 2, new Peao(tabuleiro, Color.WHITE, this));
        localPecaTabuleiroInicio('d', 2, new Peao(tabuleiro, Color.WHITE, this));
        localPecaTabuleiroInicio('e', 2, new Peao(tabuleiro, Color.WHITE, this));
        localPecaTabuleiroInicio('f', 2, new Peao(tabuleiro, Color.WHITE, this));
        localPecaTabuleiroInicio('g', 2, new Peao(tabuleiro, Color.WHITE, this));
        localPecaTabuleiroInicio('h', 2, new Peao(tabuleiro, Color.WHITE, this));

        localPecaTabuleiroInicio('a', 8, new Torre(tabuleiro, Color.BLACK));
        localPecaTabuleiroInicio('b', 8, new Cavalo(tabuleiro, Color.BLACK));
        localPecaTabuleiroInicio('c', 8, new Bispo(tabuleiro, Color.BLACK));
        localPecaTabuleiroInicio('d', 8, new Rainha(tabuleiro, Color.BLACK));
        localPecaTabuleiroInicio('e', 8, new Rei(tabuleiro, Color.BLACK, this));
        localPecaTabuleiroInicio('f', 8, new Bispo(tabuleiro, Color.BLACK));
        localPecaTabuleiroInicio('g', 8, new Cavalo(tabuleiro, Color.BLACK));
        localPecaTabuleiroInicio('h', 8, new Torre(tabuleiro, Color.BLACK));
        localPecaTabuleiroInicio('a', 7, new Peao(tabuleiro, Color.BLACK, this));
        localPecaTabuleiroInicio('b', 7, new Peao(tabuleiro, Color.BLACK, this));
        localPecaTabuleiroInicio('c', 7, new Peao(tabuleiro, Color.BLACK, this));
        localPecaTabuleiroInicio('d', 7, new Peao(tabuleiro, Color.BLACK, this));
        localPecaTabuleiroInicio('e', 7, new Peao(tabuleiro, Color.BLACK, this));
        localPecaTabuleiroInicio('f', 7, new Peao(tabuleiro, Color.BLACK, this));
        localPecaTabuleiroInicio('g', 7, new Peao(tabuleiro, Color.BLACK, this));
        localPecaTabuleiroInicio('h', 7, new Peao(tabuleiro, Color.BLACK, this));
    }

}
