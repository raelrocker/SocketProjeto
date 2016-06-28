package Arquivos;

import Classes.Util;
import com.sun.corba.se.impl.ior.ByteBuffer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.function.ObjDoubleConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import socketprojeto.SocketProjeto;

public class BaixarArquivo {
    
    private Socket socket;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private int pacotesArquivo = 51200;
    private boolean servidor;
   
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
    
    public boolean Baixar(Arquivo arq) {
        Arquivo arquivo;
        ByteArrayOutputStream byteArrayStream;
        try { 
            
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
                this.ResponderRequisicao();
            }
            this.EscreverLog("Arquivo recebido com sucesso");
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
    
    private Arquivo ReceberInformacoesArquivo() throws Exception {
        boolean resposta = false;
        if (objectIn == null) {
            objectIn = new ObjectInputStream(this.socket.getInputStream());
        }
        Arquivo arquivo = (Arquivo)objectIn.readObject();
        return arquivo;
    }
    
    private boolean Requisitar(Arquivo arq) throws Exception {
        if (objectOut == null) {
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        }
        objectOut.writeUTF("4@" + arq.getCaminhoNome());
        objectOut.flush();
        return true;
    }
    
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
            if (buffer[0] == 64 && buffer[1] == 70 && buffer[2] == 73 && buffer[3] == 77 && buffer[4] == 64)
            {
                break;
            }
            byteArrayOS.write(buffer, 0, bytesLidos);
            byteArrayOS.flush();                
        }
        return byteArrayOS;
    }
    
    private void EscreverLog(String msg) {
        if (this.servidor)
            SocketProjeto.AdicionarMensagem(msg);
    }
    
    private boolean ResponderRequisicao() throws Exception {
        
        if (objectOut == null) {
            objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        }
        objectOut.writeBoolean(true);
        objectOut.flush();
        return true;        
    }
}
