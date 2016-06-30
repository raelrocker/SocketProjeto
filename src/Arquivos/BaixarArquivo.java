package Arquivos;

import Classes.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import socketprojeto.SocketProjeto;

/**
 * Classe responsável pór baixar um arquivo para a máquina local
 */
public class BaixarArquivo {
    
    private Socket socket;
    private ObjectInputStream objectIn; // Objeto que recebe mensagens do socket
    private ObjectOutputStream objectOut; // Objeto que envia mensagens do socket
    private int pacotesArquivo = 51200; // Tamanho do pacote para transmissão do arquivo
    private boolean servidor; // Se TRUE, a aplicação está rodando como servidor
   
    public BaixarArquivo(Socket socket, boolean servidor) {
        this.socket = socket;
        this.objectIn = null;
        this.objectOut = null;
        this.servidor = servidor;
    }
    
    public BaixarArquivo(Socket socket, boolean servidor, ObjectInputStream in) {
        this.socket = socket;
        this.objectIn = in;
        this.objectOut = null;
        this.servidor = servidor;
    }
    
    /**
     * Baixa um arquivo para a máquina local.
     * Usado pelo cliente.
     * @param arq - Informações do arquivo que deseja baixar
     * @return TRUE se o arquivo foi baixado com sucesso
     */
    public boolean Baixar(Arquivo arq) {
        Arquivo arquivo;
        ByteArrayOutputStream byteArrayStream;
        try { 
            
            // Se for uma aplicação CLIENTE, informa para o servidor qual arquivo deve ser baixado
            if (!this.servidor) {
                if (!this.Requisitar(arq)) {
                    return false;
                }
            }
            arquivo = this.ReceberInformacoesArquivo();
            if (arquivo == null) {
                return false;
            }            
            byteArrayStream = this.ReceberArquivoBytes();
            if (byteArrayStream == null) {
                return false;
            }
            arquivo.setConteudo(byteArrayStream.toByteArray());
            if (!arquivo.VerificarArquivo()) {
                return false;
            }
            arquivo.setDiretorioDestino(Util.GetCaminhoAtual());
            //Salva arquivo na pasta de destino
            if (!arquivo.SalvarArquivo()) {
                return false;
            }
            
            return true;
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
            return false;
        } finally {
            try {
                this.objectIn.close();
                this.objectOut.close();
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * Baixa um arquivo para a máquina local.
     * Usado pelo servidor
     * @return TRUE se o arquivo foi baixado com sucesso
     */
    public boolean Baixar() {
        Arquivo arquivo;
        ByteArrayOutputStream byteArrayStream;
        try { 
            
            this.EscreverLog("Solicitação de upload de arquivo");
            this.EscreverLog("Recebendi informação do arquivo");
            arquivo = this.ReceberInformacoesArquivo();
            if (arquivo == null) {
                return false;
            }            
            this.EscreverLog("Recebendo arquivo ...");
            byteArrayStream = this.ReceberArquivoBytes();
            if (byteArrayStream == null) {
                return false;
            }
            arquivo.setConteudo(byteArrayStream.toByteArray());
            if (!arquivo.VerificarArquivo()) {
                return false;
            }
            arquivo.setDiretorioDestino(Util.GetCaminhoAtual());
            //Salva arquivo na pasta de destino
            if (!arquivo.SalvarArquivo()) {
                return false;
            }
            
            if (this.servidor) {
                this.ResponderRequisicao(true);
            }
            this.EscreverLog("Arquivo recebido com sucesso");
            return true;
        } catch (Exception ex) {
            System.err.println("Erro: " + ex.getMessage());
            try {
                this.ResponderRequisicao(false);
            } catch (Exception ex1) {}
            return false;
        } finally {
            try {
                this.objectIn.close();
                this.objectOut.close();
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * Recebe informações do arquivo que deve ser baixado.
     * @return TRUE se foi recebido com sucesso.
     * @throws Exception 
     */
    private Arquivo ReceberInformacoesArquivo() throws Exception {
        boolean resposta = false;
        if (objectIn == null) {
            objectIn = new ObjectInputStream(this.socket.getInputStream());
        }
        Arquivo arquivo = (Arquivo)objectIn.readObject();
        return arquivo;
    }
    
    /**
     * Requisita um arquivo ao cliente/servidor
     * @param arq
     * @return TRUE se operação for executada com sucesso
     * @throws Exception 
     */
    private boolean Requisitar(Arquivo arq) throws Exception {
        if (objectOut == null) {
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        }
        objectOut.writeUTF("4@" + arq.getCaminhoNome());
        objectOut.flush();
        return true;
    }
    
    /**
     * Recebe o arquivo em pacotes de bytes
     * @return TRUE se o conteudo do arquivo foi recebido com sucesso
     * @throws Exception 
     */
    private ByteArrayOutputStream ReceberArquivoBytes() throws Exception {
        byte[] buffer;
        ByteArrayOutputStream byteArrayOS;
        int bytesLidos = 0;
        
        if (objectIn == null) {
            objectIn = new ObjectInputStream(this.socket.getInputStream());
        }
        buffer = new byte[this.pacotesArquivo];
        byteArrayOS = new ByteArrayOutputStream();
        while ((bytesLidos = objectIn.read(buffer)) != -1) {
            // Verifica se os 5 primeiros bytes correspondem à string que indica o término do envio: @FIM@
            if (buffer[0] == 64 && buffer[1] == 70 && buffer[2] == 73 && buffer[3] == 77 && buffer[4] == 64)
            {
                break;
            }
            byteArrayOS.write(buffer, 0, bytesLidos);
            byteArrayOS.flush();                
        }
        return byteArrayOS;
    }
    
    /**
     * Adiciona log se a aplicação for SERVIDOR
     * @param msg 
     */
    private void EscreverLog(String msg) {
        if (this.servidor)
            SocketProjeto.AdicionarMensagem(msg);
    }
    
    /**
     * Responde ao CLIENTE
     * @param resposta
     * @return
     * @throws Exception 
     */
    private boolean ResponderRequisicao(boolean resposta) throws Exception {
        
        if (objectOut == null) {
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        }
        objectOut.writeBoolean(resposta);
        objectOut.flush();
        return true;        
    }
}
