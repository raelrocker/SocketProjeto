package socketprojeto;

import Interfaces.frmPrincipal;

public class SocketProjeto {

    public static frmPrincipal principal;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        principal = new frmPrincipal();
        principal.setVisible(true);
    }
    
    public static void AdicionarMensagem(String mensagem) {
        principal.AdicionarMensagem(mensagem);
    }
    
}
