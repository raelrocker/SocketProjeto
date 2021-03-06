package Servidor;

import Arquivos.Arquivo;
import Arquivos.EnviarArquivo;
import Arquivos.BaixarArquivo;
import Classes.Util;
import Enum.ExecutarAcao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
        this.serverSocket = null;
    }
    
    /**
     * Método que inicia e responde às requisições do servidor
     */
    @Override
    public void run() {
        ObjectInputStream in = null;
        ExecutarAcao acao;
        String[] parametros;
        String clienteIP;
        BaixarArquivo transferencia;
        
        try {
            this.serverSocket = new ServerSocket(porta);
            this.AdicionarLog("Servidor rodando na porta " + this.porta);
        } catch (Exception ex) {
            this.AdicionarLog("Erro: " + ex.getMessage());
        }
            
        while (!this.pararServidor) {
            try {
                // Aguarda a conexão
                this.socket = this.serverSocket.accept();
                // Pega o IP do cliente
                clienteIP = this.socket.getRemoteSocketAddress().toString();

                in = new ObjectInputStream(this.socket.getInputStream());
                // Pega a requisição e separa a ação@[parêmetros]
                parametros = in.readUTF().split("@");
                acao = ExecutarAcao.porCodigo(Integer.parseInt(parametros[0]));
                switch (acao) {
                    case LOGIN:
                        String[] login = parametros[1].split("_");
                        this.Login(login[0], login[1]);
                        this.AdicionarLog("Cliente conectado. IP:" + clienteIP);
                        break;
                    case LISTAR_SERVIDOR:
                        if (this.VerificarLogin()) {
                            this.GetListaArquivos();
                        }
                        break;
                    case CAMINHO:
                        if (this.VerificarLogin()) {
                            this.GetCaminhoServidor();
                        }
                        break;
                    case DOWNLOAD:
                        if (this.VerificarLogin()) {
                            EnviarArquivo enviarArquivo = new EnviarArquivo(this.socket, true);
                            enviarArquivo.Enviar(parametros[1]);
                        }
                        break;
                    case UPLOAD:
                        if (this.VerificarLogin()) {
                            transferencia = new BaixarArquivo(this.socket, true, in);
                            transferencia.Baixar();
                        }
                        break;
                    case LOGOFF:
                        this.Logoff();
                        break;
                    default:
                        throw new AssertionError();
                }
            } catch (Exception ex) {
                if (ex.getMessage().length() > 0 && ex.getMessage().contains("closed")) {
                    this.AdicionarLog("Servidor parado");
                    break;
                } else {
                    this.AdicionarLog("ERRO: " + ex.getMessage());
                }
                System.err.println("Err: " + ex.getMessage());
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }
        this.AdicionarLog("Cliente desconetado."); 
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
     * Verifica o login
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
            }
        }
    }

    /**
     * Executa o logoff
     * @throws IOException 
     */
    private void Logoff() throws IOException{
        ObjectOutputStream resposta = null;
        String msg = "Solicitação de logoff: cliente desconectado ";
        try {
            this.clienteLogado = false;
            this.AdicionarLog(msg);
            
            // Responde à requisição
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeBoolean(true);
            resposta.flush();
        } catch (IOException ex) {
            this.AdicionarLog(msg + "Erro - " + ex.getMessage());
             resposta.writeBoolean(false);
             resposta.flush();
        } finally {
            try {
                resposta.close();
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * Retorna o caminho do SERVIDOR para o CLIENTE
     * @throws IOException 
     */
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
    
    /**
     * Retorna a lista de arquivos do SERVIDOR para o CLIENTE
     * @throws IOException 
     */
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
    
    /**
     * Verifica se o CLIENTE está logado
     * @return
     * @throws IOException 
     */
    private boolean VerificarLogin() throws IOException {
        ObjectOutputStream resposta = null;
        String msg = "cliente não está logado";
        
        if (this.clienteLogado) {
            return true;
        }
        
        try {
            this.AdicionarLog(msg);            
            // Responde à requisição
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeBoolean(false);
            resposta.flush();
            return false;
        } catch (IOException ex) {
            this.AdicionarLog(msg + "Erro - " + ex.getMessage());
             resposta.writeBoolean(false);
             resposta.flush();
             return false;
        } finally {
            try {
                resposta.close();
            } catch (IOException ex) {
            }
        }
    }
    
}
