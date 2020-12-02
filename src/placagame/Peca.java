package placagame;

//Declarei a posição como protegido porque ela pode ser acessível às classes do mesmo pacote ou através de herança, seus membros herdados não são acessíveis a outras classes fora do pacote em que foram declarados.
public abstract class Peca {
    protected Posicao posicao;
    private Placa placa;
    
    public Peca(Placa placa) {
        this.placa = placa;
        posicao = null;
    }

    protected Placa getPlaca() {
        return placa;
    }

    //metodo movimentos Possivel como boolean  usado em todas as peças 
    public abstract boolean[][] movimentoPossiveis();

    public boolean possivelMovimento(Posicao posicao) {
        return movimentoPossiveis()[posicao.getLinha()][posicao.getColuna()];
    }
    
    public boolean existeQualquerMovimentoPossivel() {
        boolean[][] mat = movimentoPossiveis();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}

