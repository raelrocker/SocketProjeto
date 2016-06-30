package Arquivos;

import Classes.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import socketprojeto.SocketProjeto;

/**
 * Classe responsável por enviar um arquivo para uma máquina remota
 */
public class EnviarArquivo {
    private Socket socket;
    private ObjectInputStream objectIn; // Objeto que recebe mensagens do socket
    private ObjectOutputStream objectOut; // Objeto que envia mensagens do socket
    private int pacotesArquivo = 51200; // Tamanho do pacote para transmissão do arquivo
    private boolean servidor; // Se TRUE, a aplicação está rodando como servidor

    public EnviarArquivo(Socket socket, boolean servidor){
        this.socket = socket;
        this.objectIn = null;
        this.objectOut = null;
        this.servidor = servidor;
    }
    
    /**
     * Envia um arquivo para uma máquina remota.
     * @param nomeArquivo
     * @return TRUE se o arquivo foi enviado com sucesso
     * @throws Exception 
     */
    public boolean Enviar(String nomeArquivo) throws Exception {
        Arquivo arquivo;
        try {
            if (!this.servidor) {            
                if (!this.Requisitar()) {
                    return false;
                }
            }
            arquivo = this.GetArquivo(nomeArquivo);
            this.EscreverLog("Solicitação de download de arquivo");
            this.EscreverLog("Enviando informações do arquivo");
            if (!this.EnviarInformacoesArquivo(arquivo)) {
                this.EscreverLog("Erro ao enviar informações do arquivo");
                return false;
            }
            this.EscreverLog("Enviando arquivo ... ");
            if (!this.EnviarArquivoBytes(arquivo)){
                this.EscreverLog("Erro ao enviar arquivo");
                return false;
            }
            this.EscreverLog("Arquivo enviado com sucesso");
            return true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                objectOut.close();
                objectIn.close();
            } catch (Exception ex) {
            }
        }
    }
    
    /**
     * Envia um arquivo para uma máquina remota.
     * @param arquivo
     * @return TRUE se o arquivo foi enviado com sucesso
     * @throws Exception 
     */
    public boolean Enviar(Arquivo arquivo) throws Exception {
        try {
            
            this.EscreverLog("Solicitação de download de arquivo");
            if (!this.servidor) {            
                if (!this.Requisitar()) {
                    return false;
                }
            }
            this.EscreverLog("Enviando informações do arquivo");
            if (!this.EnviarInformacoesArquivo(arquivo)) {
                this.EscreverLog("Erro ao enviar informações do arquivo");
                return false;
            }
            this.EscreverLog("Enviando arquivo ... ");
            if (!this.EnviarArquivoBytes(arquivo)){
                this.EscreverLog("Erro ao enviar arquivo");
                return false;
            }
            this.EscreverLog("Arquivo enviado com sucesso");
            
            if (!this.servidor) {            
                if (!this.AguardarResposta()) {
                    return false;
                }
            }
            
            return true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                objectOut.close();
                objectIn.close();
            } catch (Exception ex) {
            }
        }
    }
    
    /**
     * Retorna as informações do arquivo
     * @param nomeArquivo caminho/nome do arquivo
     * @return Arquivo - objeto com as informações do arquivo
     * @throws Exception 
     */
    private Arquivo GetArquivo(String nomeArquivo) throws Exception {
        File file;
        Arquivo arquivo;
        try {
            
            // Pegas as informações de qual arquivo deve ser baixado
            file = new File(nomeArquivo);
            if (file == null) {
                return null;
            }
            
            arquivo = new Arquivo();
            arquivo.setNome(file.getName());
            arquivo.setDataHoraUpload(new Date());
            arquivo.setDiretorioDestino("d:/");
            arquivo.setDiretorioAtual(Util.GetCaminhoAtual());
            arquivo.setTamanho(file.length());
            return arquivo;
            
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    /**
     * Envia as informações do arquivo para a máquina remota.
     * @param arquivo
     * @return TRUE se as informações foram enviadas com sucesso
     * @throws Exception 
     */
    private boolean EnviarInformacoesArquivo(Arquivo arquivo) throws Exception {
        try {
            if (objectOut == null) {
                objectOut = new ObjectOutputStream(this.socket.getOutputStream());
            }
            objectOut.writeObject(arquivo);
            objectOut.flush();
            
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Envia o arquivo em pacotes de bytes.
     * @param arquivo
     * @return TRUE se o arquivo foi enviado com sucesso
     * @throws Exception 
     */
    private boolean EnviarArquivoBytes(Arquivo arquivo) throws Exception {
        File file;
        FileInputStream fileStream;
        byte[] buffer;
        int tam;
        int bytesLidos = 0;
        int totalEnviado = 0;
        
        // Seta um objeto stream do arquivo
        file = new File(arquivo.getCaminhoNome());
        fileStream = new FileInputStream(file);
        
        //Enviar arquivo
        // Define o tamanho do pacote
        if (arquivo.getTamanho() < pacotesArquivo) {
            buffer = new byte[(int)arquivo.getTamanho()];
            tam = (int)arquivo.getTamanho();
        } else {
           buffer = new byte[pacotesArquivo];
           tam = pacotesArquivo;
        }
        
        if (objectOut == null) {
            this.objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        }
        // Envia pacotes do arquivo
        while ((bytesLidos = fileStream.read(buffer, 0, tam)) != -1) {
            this.objectOut.write(buffer, 0, bytesLidos);
            this.objectOut.flush();
            totalEnviado += bytesLidos;
            System.err.println(totalEnviado + " de " + arquivo.getTamanho());
            if ((arquivo.getTamanho() - totalEnviado) < pacotesArquivo) {
                buffer = new byte[(int)arquivo.getTamanho() - totalEnviado];
                tam = (int)arquivo.getTamanho() - totalEnviado;
            }
            if (tam == 0) {
                break;
            }
        }
        
        // Envia informação que acabou o envio do arquivo
        this.objectOut.write("@FIM@".getBytes());
        this.objectOut.flush();
        
        return true;
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
     * Requisita um arquivo ao cliente/servidor
     * @param arq
     * @return TRUE se operação for executada com sucesso
     * @throws Exception 
     */
    private boolean Requisitar() throws Exception {
        
        if (objectOut == null) {
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        }
        objectOut.writeUTF("5");
        objectOut.flush();
        return true;
    }
    
    /**
     * Aguarda a resposta do SERVIDOR
     * @return
     * @throws Exception 
     */
    private boolean AguardarResposta() throws Exception {
        
        if (objectIn == null) {
            objectIn = new ObjectInputStream(this.socket.getInputStream());
        }
        boolean resposta = objectIn.readBoolean();
        return resposta;
    }
    
}
