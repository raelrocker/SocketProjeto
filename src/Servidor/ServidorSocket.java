package Servidor;

import Arquivos.Arquivo;
import Arquivos.Transferencia;
import Classes.Util;
import Enum.ExecutarAcao;
import Interfaces.frmPrincipal;
import com.sun.corba.se.impl.ior.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
        ObjectInputStream requisicao = null;
        ObjectOutputStream resposta;
        ExecutarAcao acao;
        String[] parametros;
        String clienteIP;
        Transferencia transferencia;
        
        try {
            this.serverSocket = new ServerSocket(porta);
            this.AdicionarLog("Servidor rodando na porta " + this.porta);
            
            while (!this.pararServidor) {
                try {
                    // Aguarda a conexão
                    this.socket = this.serverSocket.accept();
                    // Pega o IP do cliente
                    clienteIP = this.socket.getRemoteSocketAddress().toString();
                    
                    requisicao = new ObjectInputStream(this.socket.getInputStream());
                    // Pega a requisição e separa a ação@[parêmetros]
                    parametros = requisicao.readUTF().split("@");
                    acao = ExecutarAcao.porCodigo(Integer.parseInt(parametros[0]));
                    switch (acao) {
                        case LOGIN:
                            String[] login = parametros[1].split("_");
                            this.Login(login[0], login[1]);
                            this.AdicionarLog("Cliente conectado. IP:" + clienteIP);
                            break;
                        case LISTAR_SERVIDOR:
                            this.GetListaArquivos();
                            break;
                        case CAMINHO:
                            this.GetCaminhoServidor();
                            break;
                        case DOWNLOAD:
                            break;
                        case UPLOAD:
                            transferencia = new Transferencia(this.socket);
                            transferencia.ReceberArquivo();
                            break;
                        case LOGOFF:
                            this.socket.close();
                            break;
                        default:
                            throw new AssertionError();
                    }
                } catch (IOException ex) {
                } finally {
                    if (requisicao != null) {
                        requisicao.close();
                    }
                }
            }
            
            this.AdicionarLog("Cliente desconetado.");
            
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
    private void Login(String usuario, String senha) throws IOException{
        ObjectOutputStream resposta = null;
        String msg = "Solicitação de login: ";
        String msg2 = "";
        
        try {
            if (!usuario.equals("admin") || !senha.equals("12345")) {
                this.clienteLogado = false;
                msg2 = "Erro - usuário ou senha incorretos.";
            } else {
                this.clienteLogado = true;
                msg2 = "Cliente logado com sucesso.";
            }
            this.AdicionarLog(msg + msg2);
            
            // Responde à requisição
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeBoolean(this.clienteLogado);
            resposta.flush();
        } catch (IOException ex) {
            this.AdicionarLog(msg + "Erro - " + ex.getMessage());
             resposta.writeBoolean(false);
             resposta.flush();
        } finally {
            try {
                resposta.close();
            } catch (IOException ex) {
                this.AdicionarLog(msg + "Erro - " + ex.getMessage());
                throw ex;
            }
        }
    }    
    
    
    private void GetCaminhoServidor() throws IOException {
        ObjectOutputStream resposta = null;
        try {
            socketprojeto.SocketProjeto.AdicionarMensagem("Solicitando caminho do servidor ...");
            String caminho = Util.GetCaminhoAtual();
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeUTF(caminho);
            resposta.flush();
            socketprojeto.SocketProjeto.AdicionarMensagem("Caminho do servidor enviado.");
        } catch (Exception ex) {
            socketprojeto.SocketProjeto.AdicionarMensagem("Erro ao retornar o caminho do servidor.");
        } finally {
            resposta.close();
        }        
    }
    
    private void GetListaArquivos() throws IOException {
        ObjectOutputStream resposta = null;
        try {
            socketprojeto.SocketProjeto.AdicionarMensagem("Solicitando lista de arquivos do servidor ...");
            ArrayList<Arquivo> listaArquivos = Util.GetListaArquivos();
            
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeObject(listaArquivos);
            resposta.flush();
            socketprojeto.SocketProjeto.AdicionarMensagem("Lista de arquivos do servidor enviado.");
            
        } catch (Exception ex) {
            socketprojeto.SocketProjeto.AdicionarMensagem("Erro ao retornar a lista de arquivos do servidor.");
        } finally {
            resposta.close();
        }        
    }
    
}
