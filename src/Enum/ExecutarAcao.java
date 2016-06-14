package Enum;
public enum ExecutarAcao {
    LOGIN(1), LISTAR_SERVIDOR(2), LISTAR_LOCAL(3), DOWNLOAD(4), UPLOAD(5), LOGOFF(6);
    private final int codigo;

    ExecutarAcao(int codigo) { this.codigo = codigo; }

    int codigo() { return codigo; }

    public static ExecutarAcao porCodigo(int codigo) {
        for (ExecutarAcao acao: ExecutarAcao.values()) {
            if (codigo == acao.codigo()) return acao;
        }
        throw new IllegalArgumentException("codigo invalido");
    }
}