package socketprojeto;

import Interfaces.frmPrincipal;

public class SocketProjeto {

    public static frmPrincipal principal;
    public static byte[] byteTerminal;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        principal = new frmPrincipal();
        principal.setVisible(true);
        SocketProjeto.byteTerminal = new byte[2];
        SocketProjeto.byteTerminal[0] = 64;
        SocketProjeto.byteTerminal[1] = 74;
    }
    
    public static void AdicionarMensagem(String mensagem) {
        principal.AdicionarMensagem(mensagem);
    }
    
}
