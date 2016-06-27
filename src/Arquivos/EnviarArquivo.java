package Arquivos;

import Classes.Util;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import socketprojeto.SocketProjeto;

public class EnviarArquivo {
    private Socket socket;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private int pacotesArquivo = 51200;
    private boolean servidor;

    public EnviarArquivo(Socket socket, boolean servidor){
        this.socket = socket;
        this.objectIn = null;
        this.objectOut = null;
        this.servidor = servidor;
    }
    
    public boolean Enviar(String nomeArquivo) throws Exception {
        File file;
        Arquivo arquivo;
        try {
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
            arquivo.setTamanhoKB(file.length());
            return arquivo;
            
        } catch (Exception ex) {
            throw ex;
        }
    }
    
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
        if (arquivo.getTamanhoKB() < pacotesArquivo) {
            buffer = new byte[(int)arquivo.getTamanhoKB()];
            tam = (int)arquivo.getTamanhoKB();
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
            System.err.println(totalEnviado + " de " + arquivo.getTamanhoKB());
            if ((arquivo.getTamanhoKB() - totalEnviado) < pacotesArquivo) {
                buffer = new byte[(int)arquivo.getTamanhoKB() - totalEnviado];
                tam = (int)arquivo.getTamanhoKB() - totalEnviado;
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
    
    private void EscreverLog(String msg) {
        if (this.servidor)
            SocketProjeto.AdicionarMensagem(msg);
    }
    
}
