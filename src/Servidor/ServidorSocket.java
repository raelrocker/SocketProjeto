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
                            break;
                        case LISTAR_LOCAL:
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
    
    private void Upload() throws IOException{
        ObjectOutputStream resposta = null;
        InputStream inputStream = null;
        int tamanhoPacote = 1024;
        int tamanhoPacoteArquivo = 4096;
        int bytesLidos;
        int byteIndex;
        ByteBuffer bytes;
        ByteArrayOutputStream byteArrayOS;
        String msg = "Solicitação de upload: ";
        /*
        if (!this.clienteLogado) {
            this.AdicionarLog(msg + "Erro - cliente não autenticado");
            return;
        }
        */
        
        try {
            // Responde à requisição para proceguir com o envio dos dados do arquivo
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeBoolean(true);
            resposta.flush();
            
            // Recebe as informações do arquivo
            bytes = new ByteBuffer(tamanhoPacote, tamanhoPacote);
            byteArrayOS = new ByteArrayOutputStream();
            bytesLidos = 0;
            byteIndex = 0;
            this.AdicionarLog(msg + "Recebendo informações do arquivo ...");
            
            inputStream = socket.getInputStream();                        
            byte[] cbuffer = new byte[1024];
            String s;
            while ((bytesLidos = inputStream.read(cbuffer)) != -1) {
                if (cbuffer[0] == 64 && cbuffer[1] == 65)
                {
                    break;
                }
                byteArrayOS.write(cbuffer, 0, bytesLidos);
                byteArrayOS.flush();
                cbuffer = new byte[1024];
            }
            byteArrayOS.close();
            // Converte para a classe Arquivo
            byte[] b = byteArrayOS.toByteArray();
            Arquivo arquivo = (Arquivo)Util.getObjectFromByte(b);
            
            // Responde para o cliente enviar o arquivo
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeBoolean(true);
            resposta.flush();
            
            
            /*
            // Recebe o arquivo
            this.AdicionarLog(msg + "Recebendo arquivo ...");
            // Salva arquivo em disco
            byte[] cbuffer2 = new byte[tamanhoPacoteArquivo];
            String s2;
            byteArrayOS = new ByteArrayOutputStream();
            while ((bytesLidos = inputStream.read(cbuffer2)) != -1) {
                byteArrayOS.write(cbuffer2, 0, bytesLidos);
                byteArrayOS.flush();
            }
            arquivo.setConteudo(byteArrayOS.toByteArray());
            inputStream.close();
            
            // Salva arquivo na pasta de destino
            FileOutputStream fos = new FileOutputStream(arquivo.getDiretorioDestino() + arquivo.getNome());
            fos.write(arquivo.getConteudo());
            fos.close();
            
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeBoolean(true);
            resposta.flush();
            resposta.close();
            this.AdicionarLog(msg + "Arquivo recebido com sucesso.");
            */
            
        } catch (IOException ex) {
            this.AdicionarLog(msg + "Erro - " + ex.getMessage());
            resposta.writeBoolean(false);
            resposta.flush();
        } finally {
            
            resposta.close();
        }
    }
    
}
