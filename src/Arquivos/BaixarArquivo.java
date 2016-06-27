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
    
    public boolean ReceberArquivo() throws Exception {
        ObjectOutputStream resposta = null; // Envia as mensagens resposta
        InputStream inputStream = null; // Recebe a dados do socket
        ByteBuffer bytes;
        ByteArrayOutputStream byteArrayOS;
        int tamanhoPacote = 1024;
        int tamanhoPacoteArquivo = 4096;
        int bytesLidos = 0;
        int byteIndex = 0;
        String msg = "Solicitação de upload: ";
        BufferedInputStream bufInput = null;
        
        try {
            // Responde à requisição para proceguir com o envio dos dados do arquivo
            resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeBoolean(true);
            resposta.flush();
            
            // Recebe as informações do arquivo
            bytes = new ByteBuffer(tamanhoPacote, tamanhoPacote);
            byteArrayOS = new ByteArrayOutputStream();
            SocketProjeto.AdicionarMensagem(msg + "Recebendo informações do arquivo ...");
            
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
            resposta.writeBoolean(true);
            resposta.flush();
            
            // Recebe os pacotes do arquivo
            SocketProjeto.AdicionarMensagem(msg + "Recebendo arquivo ...");            
            byte[] cbuffer2 = new byte[tamanhoPacoteArquivo];
            
            byteArrayOS = new ByteArrayOutputStream();
            //bufInput = new BufferedInputStream(this.socket.getInputStream());
            bytesLidos = 0;
            
            while ((bytesLidos = inputStream.read(cbuffer2)) != -1) {
                if (cbuffer[0] == 64 && cbuffer[1] == 65)
                {
                    break;
                }
                byteArrayOS.write(cbuffer2, 0, bytesLidos);
                byteArrayOS.flush();                
            }
            arquivo.setConteudo(byteArrayOS.toByteArray());
            if (!arquivo.VerificarArquivo()) {
                SocketProjeto.AdicionarMensagem(msg + "Erro no envio do arquivo");
            }
            
            // Salva arquivo na pasta de destino
            if (!arquivo.SalvarArquivo()) {
                SocketProjeto.AdicionarMensagem(msg + "Erro ao salvar arquivo na pasta de destino");
            }
            //resposta = new ObjectOutputStream(this.socket.getOutputStream());
            resposta.writeBoolean(true);
            resposta.flush();
            
            SocketProjeto.AdicionarMensagem(msg + "Arquivo recebido com sucesso.");
            return true;
        } catch (Exception ex) {
            SocketProjeto.AdicionarMensagem(msg + "Erro - " + ex.getMessage());
            resposta.writeBoolean(false);
            resposta.flush();
            return false;
        } finally {
            resposta.close();
            inputStream.close();
            bufInput.close();
        }
    }
    
    public boolean EnviarArquivo(Arquivo arq) throws Exception {
        File file;
        Arquivo arquivo;
        FileInputStream fileStream = null;
        ObjectOutputStream objectOutput = null;
        ObjectInputStream objectInput = null;
        byte[] arquivoSerializado;
        ByteArrayInputStream byteStream = null;
        int bytesEnviados = 0;
        int pacotesDadosArquivo = 1024;
        int pacotesArquivo = 4096;
        byte[] byteE;
        OutputStream out = null;
        BufferedOutputStream bufferedOut = null;
        int read = 0;
        int l = 0;
        int tam;
        byte[] buff;
        try {
            // Seta as informações do arquivo
            arquivo = arq;
            if (arquivo == null) {
                return false;
            }
            file = new File(arquivo.getCaminhoNome());
            fileStream = new FileInputStream(file);
            arquivo.setTamanhoKB(file.length());
            arquivo.setDataHoraUpload(new Date());
            arquivo.setDiretorioDestino("d:/");
            
            
            // Envia solictação
            objectOutput = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            objectOutput.writeUTF("5");
            objectOutput.flush();
            
            // Recebe resposta da requisição
            objectInput = new ObjectInputStream(socket.getInputStream());
            if (!objectInput.readBoolean()) {
                return false;
            }
            
            // Envia informação do arquivo
            arquivoSerializado = arquivo.SerializarArquivo();
            byteStream = new ByteArrayInputStream(arquivoSerializado);
            byteE = new byte[pacotesDadosArquivo];
            out = socket.getOutputStream();
            
            // Envia os pacotes
            while (byteStream.read(byteE) != -1) {
                out.write(byteE);
                out.flush();
            }
            
            // Envia informação que acabou o envio dos dados do arquivo
            out.write("@A".getBytes());
            out.flush();
            
            // Recebe resposta para enviar arquivo
            if (!objectInput.readBoolean()) {
                throw new Exception("Erro ao enviar dados do arquivo.");
            }
            
            //Enviar arquivo
            //bufferedOut = new BufferedOutputStream(socket.getOutputStream());
            // Define o tamanho do pacote
            if (arquivo.getTamanhoKB() < pacotesArquivo) {
                buff = new byte[(int)arquivo.getTamanhoKB()];
                tam = (int)arquivo.getTamanhoKB();
            } else {
               buff = new byte[pacotesArquivo];
               tam = pacotesArquivo;
            }
            
            // Envia pacotes do arquivo
            while ((read = fileStream.read(buff, 0, tam)) != -1) {
                out.write(buff);
                out.flush();
                l += read;
                System.err.println(l + " de " + arquivo.getTamanhoKB());
                if ((arquivo.getTamanhoKB() - l) < 4096) {
                    buff = new byte[(int)arquivo.getTamanhoKB() - l];
                    tam = (int)arquivo.getTamanhoKB() - l;
                }

                if (tam == 0) {
                    break;
                }
            }       
            
            // Envia informação que acabou o envio do arquivo
            out.write("@A".getBytes());
            out.flush();
            
            // Recebe a resposta
            if (!objectInput.readBoolean()) {
                throw new Exception("Erro ao enviar arquivo.");
            } 
            return true;
        } catch (Exception ex) {
            throw new Exception("Erro: " + ex.getMessage());
        } finally {
            try {
                out.close();
                bufferedOut.close();
                byteStream.close();
            } catch (IOException ex) {
            }
        }
    }
    
    
    
    public boolean Baixar(Arquivo arq) {
        Arquivo arquivo;
        ByteArrayOutputStream byteArrayStream;
        try {            
            if (!this.Requisitar(arq)) {
                return false;
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
            arquivo.setDiretorioDestino("d:/");
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
}
