package Arquivos;

import Classes.Util;
import com.sun.corba.se.impl.ior.ByteBuffer;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JTextArea;
import socketprojeto.SocketProjeto;

public class Transferencia {
    private Socket socket;
    
    public Transferencia(Socket socket) {
        this.socket = socket;
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
            bufInput = new BufferedInputStream(this.socket.getInputStream());
            bytesLidos = 0;
            
            while ((bytesLidos = bufInput.read(cbuffer2)) != -1) {
                if (cbuffer2[0] == 64 && cbuffer2[1] == 65)
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
            return false;
        } finally {
            resposta.close();
            inputStream.close();
            bufInput.close();
        }
    }
    
    public boolean EnviarArquivo() {
        return true;
    }
}
