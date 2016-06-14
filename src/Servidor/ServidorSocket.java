package Servidor;

import Enum.ExecutarAcao;
import Interfaces.frmPrincipal;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * Essa classe é responsável por rodar o servidor socket em background.
 */
public class ServidorSocket implements Runnable {

    private int porta;
    private ServerSocket serverSocket;
    private JTextArea txtServidorLog;
    private boolean pararServidor;
    private Socket socket;
    private boolean clienteLogado = false;
    
    /**
     * Método construtor.
     * @param porta
     * @param txtServidorLog 
     */
    public ServidorSocket(int porta, JTextArea txtServidorLog) {
        this.porta = porta;
        this.txtServidorLog = txtServidorLog;
        this.pararServidor = false;
    }
    
    /**
     * Método que inicia e responde às requisições do servidor
     */
    @Override
    public void run() {
        ObjectInputStream requisicao;
        ObjectOutputStream resposta;
        ExecutarAcao acao;
        String[] parametros;
        String clienteIP;
        
        try {
            this.serverSocket = new ServerSocket(porta);
            this.AdicionarLog("Servidor rodando na porta " + this.porta);
            
            while (!this.pararServidor) {
                // Aguarda a conexão
                this.socket = this.serverSocket.accept();
                // Pega o IP do cliente
                clienteIP = this.socket.getRemoteSocketAddress().toString();
                this.AdicionarLog("Cliente conectado. IP:" + clienteIP);
                
                requisicao = new ObjectInputStream(this.socket.getInputStream());
                // Pega a requisição e separa a ação@[parêmetros]
                parametros = requisicao.readUTF().split("@");
                acao = ExecutarAcao.porCodigo(Integer.parseInt(parametros[0]));
                switch (acao) {
                    case LOGIN:
                        String[] login = parametros[1].split("_");
                        this.Login(login[0], login[1]);
                        break;
                    case LISTAR_SERVIDOR:
                        break;
                    case LISTAR_LOCAL:
                        break;
                    case DOWNLOAD:
                        break;
                    case UPLOAD:
                        break;
                    case LOGOFF:
                        break;
                    default:
                        throw new AssertionError();
                }
                requisicao.close();
            }
            
        } catch (Exception ex) {
            if (ex.getMessage().contains("closed")) {
                this.AdicionarLog("Servidor parado");
            } else {
                this.AdicionarLog("ERRO: " + ex.getMessage());
            }
        }
        finally {
            try {
                this.serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Adiciona as mensagens na área de LOG
     * @param msg 
     */
    public void AdicionarLog(String msg) {
        this.txtServidorLog.setText(txtServidorLog.getText() + msg + "\n");
    }
    
    /**
     * Para o servidor socket.
     * @throws IOException 
     */
    public void PararServidor() throws IOException {
        this.pararServidor = true;
        this.serverSocket.close();
    }
    
    /**
     * Verifica se login
     * @param usuario
     * @param senha
     * @return 
     */
    private void Login(String usuario, String senha){
        ObjectOutputStream resposta = null;
        String msg = "Solicitação de login: ";
        String msg2 = "";
        
        try {
            if (!usuario.equals("admin") || !senha.equals("12345")) {
                this.clienteLogado = false;
                msg2 = "Erro - usuário ou senha incorretos.";
            } else {
                this.clienteLogado = true;
                msg2 = "Sucesso - cliente logado com sucesso.";
            }
            this.AdicionarLog(msg + msg2);
            
            // Responde à requisição
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeBoolean(this.clienteLogado);
            resposta.flush();
        } catch (IOException ex) {
            this.AdicionarLog(msg + "Erro - " + ex.getMessage());
        } finally {
            try {
                resposta.close();
            } catch (IOException ex) {
                this.AdicionarLog(msg + "Erro - " + ex.getMessage());
            }
        }
    }
    
    private void Upload(){
        ObjectOutputStream resposta = null;
        String msg = "Solicitação de upload: ";
        if (!this.clienteLogado) {
            this.AdicionarLog(msg + "Erro - cliente não autenticado");
            return;
        }
        
        try {
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            // Recebe as informações do arquivo
            // Responde com ok
            // Recebe o arquivo
            // Salva arquivo em disco
            // Responde com ok
        } catch (IOException ex) {
            this.AdicionarLog(msg + "Erro - " + ex.getMessage());
        } finally {
            try {
                resposta.close();
            } catch (IOException ex) {
                this.AdicionarLog(msg + "Erro - " + ex.getMessage());
            }
        }
    }
    
}
